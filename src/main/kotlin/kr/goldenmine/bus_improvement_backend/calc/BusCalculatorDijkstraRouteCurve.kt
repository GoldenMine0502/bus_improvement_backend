package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.springframework.stereotype.Service
import java.util.*

@Service
class BusCalculatorDijkstraRouteCurve(
    private val busStopInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce,
    private val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
): BusCalculatorDijkstra(busStopInfoService, busStopStationService, busThroughInfoService, busTrafficService) {

    override val type: String
        get() = "DijkstraRouteCurve"

    override fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int> {
        val minimumDistanceFromStartToEnd = getDistance(busCalculatorDijkstraMinimumDistance.executeDijkstra(startIndex, endIndex))

        val nodes = ArrayList<ArrayList<NodeInfo>>()
        // 노드 생성
        repeat(stations.size + 1) {
            nodes.add(ArrayList())
        }

        for (i in 0 until throughs.size - 1) {
            val start = stationsIdToIndexMap[throughs[i].busStopStationId]
            val finish = stationsIdToIndexMap[throughs[i + 1].busStopStationId]

            if (start != null && finish != null) {
                val stationStart = stations[start]
                val stationFinish = stations[finish]

                val stationEnd = stations[endIndex]

                if (stationStart.posX != null && stationStart.posY != null &&
                    stationFinish.posX != null && stationFinish.posY != null &&
                            stationEnd.posX != null && stationEnd.posY != null
                ) {
                    val distance = distanceTM127(
                        Point(stationStart.posX, stationStart.posY),
                        Point(stationFinish.posX, stationFinish.posY)
                    )
                    val distanceToEnd = distanceTM127(
                        Point(stationFinish.posX, stationFinish.posY),
                        Point(stationEnd.posX, stationEnd.posY)
                    )
//                    val minimumDistanceToEnd = getDistance(busCalculatorDijkstraMinimumDistance.executeDijkstra(finish, endIndex))
                    val traffic = trafficIdToTrafficAmountMap[start] ?: 1

                    nodes[start].add(NodeInfo(finish, distanceToEnd, traffic, distance))
                }
            }
        }

        val minimumCost = DoubleArray(stations.size)
        val previousIndices = IntArray(stations.size)

        // cost = routeCurve * dist / users 이므로
        // dist는 무한, users는 0이여야 가장 큰 cost를 얻음.
        for (i in minimumCost.indices) {
            minimumCost[i] = Double.MAX_VALUE
            previousIndices[i] = i
        }

        val queue = PriorityQueue<NodeRouteCurve>(Comparator.comparingDouble { o -> o.calculateCost(minimumDistanceFromStartToEnd) })
        queue.offer(NodeRouteCurve(startIndex, minimumDistanceFromStartToEnd, 0, 0.0))
        minimumCost[startIndex] = 0.0

        while (!queue.isEmpty()) {
            val current = queue.poll()

//            println(queue.size)

            if (minimumCost[current.index] < current.calculateCost(minimumDistanceFromStartToEnd))
                continue

            val nexts = nodes[current.index]

            for (i in nexts.indices) {
                val next = nexts[i] // 얘가 sum을 저장할 가능성은 0

                val nextCost = NodeRouteCurve(next.index, next.minimumDistanceToEnd, current.usersSum + next.users, current.distanceSum + next.distance)
                val nextCostCalculated = nextCost.calculateCost(minimumDistanceFromStartToEnd)

                if (minimumCost[next.index] > nextCostCalculated) {
                    minimumCost[next.index] = nextCostCalculated
                    previousIndices[next.index] = current.index

                    queue.add(nextCost)
                }
            }
        }

        return getTraceBack(previousIndices, startIndex, endIndex)
    }

    inner class NodeInfo(
        val index: Int,
        val minimumDistanceToEnd: Double,
        val users: Int,
        val distance: Double,
    )

    // 얘는 합을 얻으면 안됨.
    inner class NodeRouteCurve(
        val index: Int,
        private val minimumDistanceToEnd: Double,
        val usersSum: Int,
        val distanceSum: Double,
    ) {
        private val totalDistanceEstimated = distanceSum + minimumDistanceToEnd

        fun calculateCost(minimumDistance: Double) =
            (totalDistanceEstimated / (minimumDistance)) * distanceSum / usersSum
    }
}