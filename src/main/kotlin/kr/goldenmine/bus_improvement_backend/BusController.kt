package kr.goldenmine.bus_improvement_backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bus")
class BusController @Autowired constructor(
    private val busStopStationSerivce: BusStopStationSerivce
){
    @RequestMapping(
        value = ["/station"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllBusStations(): List<BusStopStationInfo> {
        return busStopStationSerivce.list()
    }
}