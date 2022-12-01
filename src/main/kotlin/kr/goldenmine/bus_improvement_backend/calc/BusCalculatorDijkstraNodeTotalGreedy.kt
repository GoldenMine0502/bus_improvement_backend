package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.controllers.BusCalculatorController
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfo
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.node.BusTrafficNodeInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.max

@Service
class BusCalculatorDijkstraNodeTotalGreedy(
    private val busStopInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce,
    private val busTrafficNodeInfoSerivce: BusTrafficNodeInfoSerivce,
    private val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
) : BusCalculatorDijkstra(busStopInfoService, busStopStationService, busThroughInfoService, busTrafficService) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculatorDijkstraNodeTotalGreedy::class.java)

    // 2시 7분 30초
    override val type: String
        get() = "DijkstraNodeTotal"

    lateinit var adjointMatrixUsers: Array<IntArray>
    var totalUsage = 0
    var busUsageArray = ArrayList<String>()
    var busTransfer = 50
    val busTimes = HashMap<String, Int>()

    override fun calculatePre() {
        totalUsage = 0

        val stationsSize = stations.size
        adjointMatrixUsers = Array(stationsSize) { IntArray(stationsSize) }

        val busTrafficNodeInfoList = busTrafficNodeInfoSerivce.list()

        val lastSequences = mutableListOf<Int>()

        fun sequenceList(routeId: String): MutableList<Int> {
            return busThroughInfoService.getThroughSequencesFromRouteId(routeId)
                .asSequence()
                .map {
                    stationsIdToIndexMap[it.busStopStationId]
                }
                .filterNotNull()
                .toMutableList()
        }

        try {
            log.info("routeNo: ${busTrafficNodeInfoList[0].routeNo} ${routeNoToBusInfo[busTrafficNodeInfoList[0].routeNo!!]?.routeId}")
            lastSequences.addAll(sequenceList(routeNoToBusInfo[busTrafficNodeInfoList[0].routeNo!!]?.routeId!!))
        } catch(ex: Exception) {
            log.info("first route is null")
        }
        for (index in 0 until busTrafficNodeInfoList.size - 1) {
            val start = busTrafficNodeInfoList[index]
            val finish = busTrafficNodeInfoList[index + 1]

            if (start.routeNo == finish.routeNo && finish.sequence != 0) {

                // 일부 인천-부천 노선이나 광역버스들
                if(lastSequences.size == 0) continue

                val total = start.getTotal()

                val routeId = routeNoToBusInfo[start.routeNo!!]?.routeId!!
//                log.info("${lastSequences.size}")
//                log.info("$start")
//                log.info("$finish")

                if(start.sequence!! >= lastSequences.size - 1) continue

                val startIndex = lastSequences[start.sequence]
                val finishIndex = lastSequences[start.sequence + 1]

                adjointMatrixUsers[startIndex][finishIndex] += total
                totalUsage += total
            } else {
                lastSequences.clear()
                log.info("routeNo: ${finish.routeNo} ${routeNoToBusInfo[finish.routeNo!!]?.routeId} ${start.id}")
                val routeId = routeNoToBusInfo[finish.routeNo!!]?.routeId
                if(routeId != null) {
                    lastSequences.addAll(sequenceList(routeNoToBusInfo[finish.routeNo]?.routeId!!))
                }
            }
        }

        // 버스당 통행
        // http://m.tf.co.kr/sa2da/newsread?idx=1736033
        busList.forEach { busInfo ->
            if (busInfo.busStartTime != null && busInfo.busFinishTime != null &&
                busInfo.maxAllocationGap != null && busInfo.minAllocationGap != null &&
                busInfo.routeId != null
            ) {
                val startH = if (busInfo.busStartTime.length == 4) busInfo.busStartTime.substring(0, 2)
                    .toInt() else busInfo.busStartTime.substring(0, 1).toInt()
                val startM = if (busInfo.busStartTime.length == 4) busInfo.busStartTime.substring(3, 4)
                    .toInt() else busInfo.busStartTime.substring(1, 3).toInt()
                val finishH = if (busInfo.busFinishTime.length == 4) busInfo.busFinishTime.substring(0, 2)
                    .toInt() else busInfo.busFinishTime.substring(0, 1).toInt()
                val finishM = if (busInfo.busFinishTime.length == 4) busInfo.busFinishTime.substring(3, 4)
                    .toInt() else busInfo.busFinishTime.substring(1, 3).toInt()

                val startTotal = startH * 60 + startM
                val finishTotal = (finishH * 60 + finishM).let { if (it < startTotal) it + 24 * 60 else it }

                val averageAllocation = (busInfo.maxAllocationGap + busInfo.minAllocationGap) / 2

                val count = (finishTotal - startTotal) * averageAllocation

                busTimes[busInfo.routeId] = count
            }
        }

        routeSequences.sortBy { u1 ->
            val value1 = routeIdToBusInfo[u1]!!
//            val value2 = routeIdToBusInfo[u1]!!

//            return value1.minAllocationGap - value2.minAllocationGap!!
            value1.minAllocationGap
        }
    }

    override fun processAfter(busInfo: BusInfo, trace: List<Int>) {
        for(index in 0 until trace.size - 1) {
            val start = trace[index]
            val finish = trace[index + 1]
            val busTime = busTimes[busInfo.routeId]!!

            adjointMatrixUsers[start][finish] -= busTransfer * busTime

            // 1 이하로 안떨어지게(코스트 무한 X)
            if(adjointMatrixUsers[start][finish] < 1) {
                adjointMatrixUsers[start][finish] = 1
            }
        }
    }

    override fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int> {
        val minimumDistance = getDistance(busCalculatorDijkstraMinimumDistance.executeDijkstra(startIndex, endIndex))

        log.info("minimumDistance: $minimumDistance")

        val nodes = ArrayList<ArrayList<Node>>()
        // 노드 생성
        repeat(stations.size + 1) {
            nodes.add(ArrayList())
        }

        busThroughMap.forEach { (t, u) ->
            for (i in 0 until u.size) {
                val startThroughIndex = i
                val finishThroughIndex = if (i != u.size - 1) i + 1 else 0
                val start = stationsIdToIndexMap[u[startThroughIndex].busStopStationId]
                val finish = stationsIdToIndexMap[u[finishThroughIndex].busStopStationId]

                if (start != null && finish != null) {
                    val stationFirst = stations[startIndex]
                    val stationStart = stations[start]
                    val stationFinish = stations[finish]
                    val stationEnd = stations[endIndex]

                    if (stationStart.posX != null && stationStart.posY != null &&
                        stationFinish.posX != null && stationFinish.posY != null &&
                        stationFirst.posX != null && stationFirst.posY != null &&
                        stationEnd.posX != null && stationEnd.posY != null &&
                        u[startThroughIndex].routeId == u[finishThroughIndex].routeId
                    ) {
                        val distancePointToPoint = distanceTM127(
                            Point(stationStart.posX, stationStart.posY),
                            Point(stationFinish.posX, stationFinish.posY)
                        )
                        val distanceFromStart = distanceTM127(
                            Point(stationFirst.posX, stationFirst.posY),
                            Point(stationStart.posX, stationStart.posY)
                        )
                        val distanceToEnd = distanceTM127(
                            Point(stationFinish.posX, stationFinish.posY),
                            Point(stationEnd.posX, stationEnd.posY)
                        )

                        val users = adjointMatrixUsers[start][finish]

                        val routeCurve = ((distanceFromStart + distanceToEnd + distancePointToPoint) / minimumDistance)

                        //  지금 상황에선 충분히 1.0 아래로 내려갈 수 있음
//                        if(routeCurve < 1.0) {
//                            log.warn("routeCurve is smaller than 1.0 $start $finish $routeCurve")
//                        }

                        if(routeCurve * distancePointToPoint / users < 0) {
                            log.warn("cost is smaller than 0 $start $finish $routeCurve")
                        }

                        nodes[start].add(Node(finish, routeCurve * distancePointToPoint / users))
                    }
                }
            }
        }

        val dist = DoubleArray(stations.size)
        val previous = IntArray(stations.size)

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

//            log.info("$current ${queue.size}")

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

//        val queue = PriorityQueue<Node>(Comparator.comparingDouble { o -> o.cost })
//        queue.offer(Node(startIndex, 0.0, 1))
//        dist[startIndex] = 0.0
//
//        while (!queue.isEmpty()) {
//            val current = queue.poll()
//
//            if (dist[current.index] < current.cost)
//                continue
//
//            val nexts = nodes[current.index]
//
//            for (i in nexts.indices) {
//                val next = nexts[i]
////                val nextNode = Node(next.index, next.distanceToEnd, next.distance, next.users)
//                val nextCost = current.cost + next.cost
//                if (dist[next.index] > nextCost) {
//                    dist[next.index] = nextCost
//                    previous[next.index] = current.index
//                    queue.add(Node(next.index, next.distance, next.users))
//                }
//            }
//        }

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
}