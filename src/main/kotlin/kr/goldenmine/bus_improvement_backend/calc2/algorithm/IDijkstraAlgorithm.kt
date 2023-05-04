package kr.goldenmine.bus_improvement_backend.calc2.algorithm

import kr.goldenmine.bus_improvement_backend.calc2.path.BusPathOnlyGwangjin
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusStopInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusThroughInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusTrafficInfoDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.PriorityQueue
import kotlin.math.roundToInt

@Service
abstract class IDijkstraAlgorithm(
    val gwangjinBusStopInfoDatabase: GwangjinBusStopInfoDatabase,
    val gwangjinBusInfoDatabase: GwangjinBusInfoDatabase,
    val gwangjinBusThroughInfoDatabase: GwangjinBusThroughInfoDatabase,
    val gwangjinBusTrafficInfoDatabase: GwangjinBusTrafficInfoDatabase,
    ): IAlgorithm {
    private val log: Logger = LoggerFactory.getLogger(GwangjinBusInfoDatabase::class.java)

    val shortToIndexMap = HashMap<String, Int>()
    val indexToShortMap = HashMap<Int, String>()

    val routes = HashMap<String, List<Int>>()

    inner class Node(
        val index: Int,
        val cost: Double
    ) {
        override fun toString(): String {
            return "Node{" +
                    "idx=" + index +
                    ", cost=" + cost +
                    '}'
        }
    }

    abstract fun getNode(adjointMatrix: Array<IntArray>, busThroughInfos: List<GwangjinBusThroughInfo>, startThroughIndex: Int, finishThroughIndex: Int, startIndex: Int, endIndex: Int): Node

    override fun executeAlgorithm(adjointMatrix: Array<IntArray>, startIndex: Int, endIndex: Int): List<Int> {
        val nodes = ArrayList<ArrayList<Node>>()
        val stations = shortToIndexMap.size

        val dist = DoubleArray(stations)
        val previous = IntArray(stations)

        if(startIndex == endIndex)
            return getTraceBack(previous, startIndex, endIndex)

        // 노드 생성
        repeat(stations + 1) {
            nodes.add(ArrayList())
        }

        gwangjinBusThroughInfoDatabase.busThroughs.forEach { (routeNo, busThroughInfos) ->
            for (index in busThroughInfos.indices) {
                val startThroughIndex = index
                val finishThroughIndex = if(index != busThroughInfos.size - 1) index + 1 else 0 // 연결성
                val start = shortToIndexMap[busThroughInfos[startThroughIndex].arsId]
                val finish = shortToIndexMap[busThroughInfos[finishThroughIndex].arsId]

                if(start != null && finish != null) {
                    nodes[start].add(getNode(adjointMatrix, busThroughInfos, startThroughIndex, finishThroughIndex, start, finish))
                }
            }
        }



        for (i in dist.indices) {
            dist[i] = Double.MAX_VALUE
            previous[i] = i
        }

        val queue = PriorityQueue<Node>(Comparator.comparingDouble { o -> o.cost })
        queue.offer(Node(startIndex, 0.0))
        dist[startIndex] = 0.0

        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (dist[current.index] < current.cost)
                continue

            val nexts = nodes[current.index]

            for (i in nexts.indices) {
                val next = nexts[i]
                val nextCost = current.cost + next.cost
                if (dist[next.index] > nextCost) {
                    dist[next.index] = nextCost
                    previous[next.index] = current.index
                    queue.add(Node(next.index, dist[next.index]))
                }
            }
        }

        return getTraceBack(previous, startIndex, endIndex)
    }

    fun initShortIdIndexMap() {
        gwangjinBusThroughInfoDatabase.busThroughs.forEach { (t, u) ->
            u.forEach {
                if(!shortToIndexMap.containsKey(it.arsId)) {
                    shortToIndexMap[it.arsId] = shortToIndexMap.size
                    indexToShortMap[shortToIndexMap[it.arsId]!!] = it.arsId
                }
            }
        }
    }

    override fun getIndexToShortIdMap(): HashMap<Int, String> {
        return indexToShortMap
    }

    override fun getShortIdToIndexMap(): HashMap<String, Int> {
        return shortToIndexMap
    }

    fun executeAllAlgorithm(): AlgorithmResult {
        val adjointMatrix = createAdjointMatrix()
        val busPath = BusPathOnlyGwangjin(gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusStopInfoDatabase)
        val map = busPath.createAllBusPath()
//        val indexMap = getShortIdToIndexMap()

        val result = HashMap<String, List<Int>>()
        val size = map.size
        var count = 0

        map.forEach { (t, u) ->
            val first = shortToIndexMap[u.first]
            val second = shortToIndexMap[u.second]

            if(first != null && second != null) {
                val trace = executeAlgorithm(adjointMatrix, first, second)
                result[t] = trace
            }
            count++
            if(count % 10 == 0) {
                log.info("${getAlgorithmType()}: ${(count.toDouble() / size * 100).roundToInt()}%")
            }
        }

        return AlgorithmResult(result)
    }

    fun getTraceBack(previousIndices: IntArray, startIndex: Int, endIndex: Int): List<Int> {
        val previousNodes = ArrayList<Int>()

        var current = endIndex

        while (true) {
            val previous = previousIndices[current]

//            log.info("$previous, $current, $startIndex, $endIndex")

            // 시작노드까지 싹싹 긁어서 add
            previousNodes.add(current)

            // 시작 노드에 도달했다는 뜻이므로 break
            if (previous == startIndex || previous == current) break

//            if(previous == current) sleep(1000L)

            current = previous
        }

        previousNodes.add(startIndex)

        return previousNodes.reversed()
    }

    override fun createAdjointMatrix(): Array<IntArray> {
        initShortIdIndexMap()

        val totalStations = shortToIndexMap.size
        val adjointMatrixUsers = Array(totalStations) { IntArray(totalStations) }

        gwangjinBusTrafficInfoDatabase.traffics.forEach { (t, u) ->
            var c = 0

            for(i in 0 until u.size - 1) {
                val start = u[i]
                val finish = u[i + 1]

                val startIndex = shortToIndexMap[start.shortId]
                val finishIndex = shortToIndexMap[finish.shortId]

                val total = start.getTotal()

                if(startIndex != null && finishIndex != null) {
                    adjointMatrixUsers[startIndex][finishIndex] += total
                }
            }
        }

//        println(totalStations)

//        adjointMatrixUsers.forEach {
//            it.forEach { print("$it, ") }
//            println()
//        }

        return adjointMatrixUsers
    }

    override fun getAllRoutes(): HashMap<String, List<Int>> {
        return routes
    }

    inner class AlgorithmResult(
        val path: HashMap<String, List<Int>>
    )
}