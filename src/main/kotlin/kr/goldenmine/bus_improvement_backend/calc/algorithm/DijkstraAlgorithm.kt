package kr.goldenmine.bus_improvement_backend.calc.algorithm

import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.calc.data.DijkstraData
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import java.util.*

abstract class DijkstraAlgorithm(
    open val dijkstraData: DijkstraData
): IAlgorithm<List<Int>> {

    abstract fun getCost(startIndex: Int, finishIndex: Int): Double?

    override fun pre() {

    }

    override fun executeAlgorithm(startIndex: Int, endIndex: Int): List<Int> {
        val nodes = ArrayList<ArrayList<Node>>()
        // 노드 생성
        repeat(dijkstraData.stations.size + 1) {
            nodes.add(ArrayList())
        }

        dijkstraData.busThroughMap.forEach { (t, u) ->
            for (i in 0 until u.size) {
                val startThroughIndex = i
                val finishThroughIndex = if(i != u.size - 1) i + 1 else 0
                val start = BusCalculator.stationsIdToIndexMap[u[startThroughIndex].busStopStationId]
                val finish = BusCalculator.stationsIdToIndexMap[u[finishThroughIndex].busStopStationId]

                if (start != null && finish != null &&
                    u[startThroughIndex].routeId == u[finishThroughIndex].routeId) {

                    val cost = getCost(startIndex, endIndex)

                    if(cost != null) {
                        nodes[start].add(Node(finish, cost))
                    }
                }
            }
        }

        val dist = DoubleArray(dijkstraData.stations.size)
        val previous = IntArray(dijkstraData.stations.size)

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

    fun getDistance(traceBack: List<Int>): Double {
        var totalDistance = 0.0

        for (index in 0 until traceBack.size - 1) {
            val start = traceBack[index]
            val finish = traceBack[index + 1]

            val stationStart = dijkstraData.stations[start]
            val stationFinish = dijkstraData.stations[finish]

            val distance = distanceTM127(
                Point(stationStart.posX!!, stationStart.posY!!),
                Point(stationFinish.posX!!, stationFinish.posY!!)
            )

            totalDistance += distance
        }

        return totalDistance
    }
}