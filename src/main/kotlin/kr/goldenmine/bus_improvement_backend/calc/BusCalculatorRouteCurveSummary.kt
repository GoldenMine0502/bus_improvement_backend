package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import org.springframework.stereotype.Service

@Service
class BusCalculatorRouteCurveSummary(
    private val busInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce
): BusCalculatorRouteCurve(busInfoService, busStopStationService, busThroughInfoService, busTrafficService) {

    override fun calculate() {
        super.calculate()


    }

    override val type: String
        get() = "RouteCurveSummary"
}