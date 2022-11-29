package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.node.BusTrafficNodeInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.springframework.stereotype.Service
import java.util.*

@Service
class BusCalculatorDijkstraNodeTotal(
    private val busStopInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce,
    private val busTrafficNodeInfoSerivce: BusTrafficNodeInfoSerivce,
    private val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
): BusCalculatorDijkstra(busStopInfoService, busStopStationService, busThroughInfoService, busTrafficService) {
    override val type: String
        get() = "DijkstraNodeTotal"

    lateinit var adjointMatrixUsers: Array<IntArray>

    override fun calculate() {
        super.calculate()

        val stationsSize = stations.size
        adjointMatrixUsers = Array(stationsSize) { IntArray(stationsSize) }

        val busTrafficNodeInfoList = busTrafficNodeInfoSerivce.list()

        for(index in 0 until busTrafficNodeInfoList.size - 1) {
            val start = busTrafficNodeInfoList[index]
            val finish = busTrafficNodeInfoList[index + 1]

            if(start.routeNo == finish.routeNo) {
                val total = start.getTotal()

                val routeId = routeNoToBusInfo[start.routeNo!!]?.routeId!!
                val sequences = routes[routeId]!!
                val startIndex = sequences[start.sequence!!]
                val finishIndex = sequences[start.sequence + 1]

                adjointMatrixUsers[startIndex][finishIndex] += total
            }
        }
    }

    override fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int> {
        val minimumDistance = getDistance(busCalculatorDijkstraMinimumDistance.executeDijkstra(startIndex, endIndex))

        val nodes = ArrayList<ArrayList<Node>>()
        // 노드 생성
        repeat(stations.size + 1) {
            nodes.add(ArrayList())
        }

        busThroughMap.forEach { (t, u) ->
            for (i in 0 until u.size) {
                val startThroughIndex = i
                val finishThroughIndex = if(i != u.size - 1) i + 1 else 0
                val start = stationsIdToIndexMap[u[startThroughIndex].busStopStationId]
                val finish = stationsIdToIndexMap[u[finishThroughIndex].busStopStationId]

                if (start != null && finish != null) {
                    val stationStart = stations[start]
                    val stationFinish = stations[finish]
                    val stationEnd = stations[endIndex]

                    if (stationStart.posX != null && stationStart.posY != null &&
                        stationFinish.posX != null && stationFinish.posY != null &&
                        stationEnd.posX != null && stationEnd.posY != null &&
                        u[startThroughIndex].routeId == u[finishThroughIndex].routeId
                    ) {
                        val distancePointToPoint = distanceTM127(
                            Point(stationStart.posX, stationStart.posY),
                            Point(stationFinish.posX, stationFinish.posY)
                        )
                        val distanceToEnd = distanceTM127(
                            Point(stationFinish.posX, stationFinish.posY),
                            Point(stationEnd.posX, stationEnd.posY)
                        )

                        nodes[start].add(Node(finish, distanceToEnd, distancePointToPoint, 0))
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

        val queue = PriorityQueue<Node>(Comparator.comparingDouble { o -> o.calculateCost(minimumDistance) })
        queue.offer(Node(startIndex, minimumDistance, 0.0, 1))
        dist[startIndex] = 0.0

        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (dist[current.index] < current.calculateCost(minimumDistance))
                continue

            val nexts = nodes[current.index]

            for (i in nexts.indices) {
                val next = nexts[i]
//                val nextNode = Node(next.index, next.distanceToEnd, next.distance, next.users)
                val nextCost = current.calculateCost(minimumDistance) + next.calculateCost(minimumDistance)
                if (dist[next.index] > nextCost) {
                    dist[next.index] = nextCost
                    previous[next.index] = current.index
                    queue.add(Node(next.index, next.distanceToEnd, next.distance, next.users))
                }
            }
        }

        return getTraceBack(previous, startIndex, endIndex)
    }

    inner class Node(
        val index: Int,
        val distanceToEnd: Double,
        val distance: Double,
        val users: Int,
    ) {
        fun calculateCost(minimumDistance: Double) = ((distanceToEnd + distance) / (minimumDistance)) * distance / users

        override fun toString(): String {
            return "Node(" +
                    "index=$index, " +
                    "distance=$distance, " +
                    "users=$users" +
                    ")"
        }
    }
}