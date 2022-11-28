package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import org.springframework.stereotype.Service

//@Service
class BusCalculatorDijkstraRouteCurve2(
    private val busStopInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce,
    private val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
): BusCalculatorDijkstra(busStopInfoService, busStopStationService, busThroughInfoService, busTrafficService) {
    override val type: String
        get() = "DijkstraRouteCurve2"

    override fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int> {
        TODO("Not yet implemented")
    }
}