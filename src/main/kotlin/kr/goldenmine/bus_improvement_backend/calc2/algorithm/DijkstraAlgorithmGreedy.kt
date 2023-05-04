package kr.goldenmine.bus_improvement_backend.calc2.algorithm

import kr.goldenmine.bus_improvement_backend.calc2.path.BusPathOnlyGwangjin
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusInfo
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusStopInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusThroughInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusTrafficInfoDatabase

class DijkstraAlgorithmGreedy(
    gwangjinBusStopInfoDatabase: GwangjinBusStopInfoDatabase,
    gwangjinBusInfoDatabase: GwangjinBusInfoDatabase,
    gwangjinBusThroughInfoDatabase: GwangjinBusThroughInfoDatabase,
    gwangjinBusTrafficInfoDatabase: GwangjinBusTrafficInfoDatabase,
    val greedyCount: Int = 25,
): DijkstraAlgorithmDijkstra(
    gwangjinBusStopInfoDatabase, gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusTrafficInfoDatabase
) {
    override fun getAlgorithmType(): AlgorithmType {
        return if(greedyCount == 25) AlgorithmType.DIJKSTRA_GREEDY_25
        else AlgorithmType.DIJKSTRA_GREEDY_5
    }

    override fun executeAllAlgorithm(): AlgorithmResult {
        val adjointMatrix = createAdjointMatrix()
        val busPath = BusPathOnlyGwangjin(gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusStopInfoDatabase)
        val map = busPath.createAllBusPath()

        val result = HashMap<String, List<Int>>()

        map.forEach { (t, u) ->
            val busInfo = gwangjinBusInfoDatabase.buses[t]
            val first = shortToIndexMap[u.first]
            val second = shortToIndexMap[u.second]

            if(busInfo != null && busInfo.busInterval != null && first != null && second != null) {
                val trace = executeAlgorithm(adjointMatrix, first, second)

                // 감소하는 알고리즘 적용
                val times = calculateTimes(busInfo)
                adjointMatrix[first][second] -= times * greedyCount
                if(adjointMatrix[first][second] < 0) adjointMatrix[first][second] = 0

                result[t] = trace
            }
        }

        return AlgorithmResult(result)
    }

    fun calculateTimes(busInfo: GwangjinBusInfo): Int {
        if(busInfo.busInterval != null && busInfo.firstBusTime != null && busInfo.lastBusTime != null) {
            val startH = busInfo.firstBusTime.substring(8, 9).toInt()
            val startM = busInfo.firstBusTime.substring(10, 11).toInt()
            val finishH = busInfo.lastBusTime.substring(8, 9).toInt()
            val finishM = busInfo.lastBusTime.substring(10, 11).toInt()
            val startTotal = startH * 60 + startM
            val finishTotal = (finishH * 60 + finishM).let { if (it < startTotal) it + 24 * 60 else it }

//            val busInterval = busInfo.busInterval.toInt()
            val averageAllocation = busInfo.busInterval.toInt().coerceAtLeast(3)
//            val averageAllocation = (busInfo.maxAllocationGap + busInfo.minAllocationGap) / 2

            val count = (finishTotal - startTotal) / averageAllocation
            return count
        } else {
            return 0
        }
    }
}