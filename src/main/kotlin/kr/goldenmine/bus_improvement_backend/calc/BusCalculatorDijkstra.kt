package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.distance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

abstract class BusCalculatorDijkstra(
    private val busInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce
): BusCalculatorRouteCurve(busInfoService, busStopStationService, busThroughInfoService, busTrafficService) {

    private val log: Logger = LoggerFactory.getLogger(BusCalculatorDijkstra::class.java)

    private val routes = HashMap<String, List<Int>>()

    override fun calculate() {
        super.calculate()

        endPoints.forEach { (k, v) ->
            val busInfo = routeIdToBusInfo[k]

            if(busInfo != null) {
                val stationStart = stationsIdToIndexMap[busInfo.originBusStopId!!]
                val stationFinish = stationsIdToIndexMap[v]

                if(stationStart != null && stationFinish != null) {
                    val result = executeDijkstra(stationStart, stationFinish)

                    routes[k] = result
                }
            }
        }
    }

    abstract fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int>

    fun getTraceBack(previousIndices: IntArray, startIndex: Int, endIndex: Int): List<Int> {
        val previousNodes = ArrayList<Int>()

        var current = endIndex

        while(true) {
            val previous = previousIndices[current]

            // 시작노드까지 싹싹 긁어서 add
            previousNodes.add(current)

            // 시작 노드에 도달했다는 뜻이므로 break
            if(previous == current) break
            current = previous
        }

        return previousNodes.reversed()
    }
    
    fun getDistance(traceBack: List<Int>): Double {
        var totalDistance = 0.0
        
        for(index in 0 until traceBack.size - 1) {
            val start = traceBack[index]
            val finish = traceBack[index + 1]

            val stationStart = stations[start]
            val stationFinish = stations[finish]

            val distance = distance(
                stationStart.posX!!, stationFinish.posX!!,
                stationStart.posY!!, stationFinish.posY!!,
                0.0, 0.0
            )

            totalDistance += distance
        }

        return totalDistance
    }
}