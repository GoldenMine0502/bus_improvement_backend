package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.springframework.stereotype.Service

class BusSummaryDijkstra(
    private val busCalculatorDijkstra: BusCalculatorDijkstra
): BusSummary {
    override fun getSummary(): HashMap<String, List<Int>> {
        val list = busCalculatorDijkstra.endPoints
            .asSequence()
            .map { Pair(it.key, it.value) }
            .sortedBy {
                val busInfo = busCalculatorDijkstra.routeIdToBusInfo[it.first]
                val start = busCalculatorDijkstra.stations[busCalculatorDijkstra.stationsIdToIndexMap[busInfo?.originBusStopId]!!]
                val finish = busCalculatorDijkstra.stations[it.second]

                distanceTM127(
                    Point(start.posX!!, start.posY!!),
                    Point(finish.posX!!, finish.posY!!)
                )
            }.toList()

        val results = HashMap<String, List<Int>>()

        for(pair in list) {
            // 그리디한 접근
            val busInfo = busCalculatorDijkstra.routeIdToBusInfo[pair.first]
            val start = busCalculatorDijkstra.stationsIdToIndexMap[busInfo?.originBusStopId]!!
            val finish = pair.second

            val result = busCalculatorDijkstra.executeDijkstra(start, finish)
            results[pair.first] = result
        }

        return results
    }
}