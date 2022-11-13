package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.distance
import org.springframework.stereotype.Service
import java.util.*

@Service
class BusCalculatorDijkstraMinimumDistance(
    private val busStopInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoSerivce: BusThroughInfoSerivce,
    private val busTrafficSerivce: BusTrafficSerivce
): BusCalculatorDijkstra(busStopInfoService, busStopStationService, busThroughInfoSerivce, busTrafficSerivce) {


//    fun getMinimumDistance(nodes: ArrayList<ArrayList<Node>>, previousNodes: IntArray, startIndex: Int, endIndex: Int): Double {
//        var current = endIndex
//        var distanceSum = 0.0
//
//        while(current != startIndex) {
//            val previous = previousNodes[current]
//            if(previous == startIndex) break
//
//            val stationStart = stations[previous]
//            val stationFinish = stations[current]
//
//            val distance = distance(
//                stationStart.posX!!, stationFinish.posX!!,
//                stationStart.posY!!, stationFinish.posY!!,
//                0.0, 0.0
//            )
//
//            distanceSum += distance
//
//            current = previous
//        }
//
//        return distanceSum
//    }

    override fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int> {
        val nodes = ArrayList<ArrayList<Node>>()
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

                if (stationStart.posX != null && stationStart.posY != null &&
                    stationFinish.posX != null && stationFinish.posY != null
                ) {
                    val distance = distance(
                        stationStart.posX, stationFinish.posX,
                        stationStart.posY, stationFinish.posY,
                        0.0, 0.0
                    )


                    nodes[start].add(Node(finish, distance))
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

    // 다음 노드의 인덱스와, 그 노드로 가는데 필요한 비용을 담고 있다.
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