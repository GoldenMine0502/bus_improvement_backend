package kr.goldenmine.bus_improvement_backend.calc.data

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfo
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce

open class DatabaseIndexData(
    private val busInfoService: BusInfoSerivce,
) {
    val routeIdToBusInfo = HashMap<String, BusInfo>()
    val routeNoToBusInfo = HashMap<String, BusInfo>()

    init {
        busInfoService.list().forEach {
            routeIdToBusInfo[it.routeId!!] = it
            routeNoToBusInfo[it.routeNo!!] = it
        }
    }
}