package kr.goldenmine.bus_improvement_backend.controllers

import kr.goldenmine.bus_improvement_backend.calc2.algorithm.AlgorithmType
import kr.goldenmine.bus_improvement_backend.calc2.algorithm.IDijkstraAlgorithm
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinAdministrator
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusStationInfo
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gwangjin/calculate")
class GwangjinController(
    val gwangjinAdministrator: GwangjinAdministrator
) {
    @RequestMapping(
        value = ["/getallroutes"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllRoutes(type: String): IDijkstraAlgorithm.AlgorithmResult? {
        return gwangjinAdministrator.results[AlgorithmType.valueOf(type)]
    }

    @RequestMapping(
        value = ["/getroutes"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getRoutes(type: String, routeNo: String): List<Int>? {
        return gwangjinAdministrator.results[AlgorithmType.valueOf(type)]?.path?.get(routeNo)
    }

    @RequestMapping(
        value = ["/getindices"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getIndices(type: String): HashMap<Int, String>? {
        return gwangjinAdministrator.models[AlgorithmType.valueOf(type)]?.indexToShortMap
    }

    @RequestMapping(
        value = ["/stations"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getStations(): HashMap<String, GwangjinBusStationInfo> {
        return gwangjinAdministrator.gwangjinBusThroughInfoDatabase.busStations
    }
}