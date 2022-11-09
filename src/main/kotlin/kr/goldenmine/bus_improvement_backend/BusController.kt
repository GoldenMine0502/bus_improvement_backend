package kr.goldenmine.bus_improvement_backend

import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.models.path.BusStopPathSerivce
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughPositionInfo
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.convertWGS84toTM127
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bus")
class BusController @Autowired constructor(
    private val busStopStationSerivce: BusStopStationSerivce,
    private val busThroughInfoSerivce: BusThroughInfoSerivce,
    private val busStopPathSerivce: BusStopPathSerivce,
    private val busCalculator: BusCalculator
){
    init {
        busCalculator.calculate()
    }

    private val log: Logger = LoggerFactory.getLogger(BusController::class.java)

    @RequestMapping(
        value = ["/station"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllBusStations(x: Double, y: Double, rangeX: Double, rangeY: Double): List<BusStopStationInfo> {
//        val center = convertWGS84toTM127(Point(x, y))
        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))

//        log.info(center.toString())
//        log.info(start.toString())
//        log.info(finish.toString())

        return busStopStationSerivce.getAllFromTo(start, finish)
    }

    @RequestMapping(
        value = ["/through"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllBusThroughInfos(x: Double, y: Double, rangeX: Double, rangeY: Double): List<BusThroughPositionInfo> {
//        val center = convertWGS84toTM127(Point(x, y))
        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))

//        log.info(center.toString())
//        log.info(start.toString())
//        log.info(finish.toString())

        return busThroughInfoSerivce.getAllFromTo(start, finish)
    }

//    @RequestMapping(
//        value = ["/path"],
//        method = [RequestMethod.GET],
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    fun getAllPaths(x: Double, y: Double, rangeX: Double, rangeY: Double): List<BusPathInfo> {
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
//
//        return busStopPathSerivce.getAllFromTo(start, finish)
//    }
//
//    @RequestMapping(
//        value = ["/pathspec"],
//        method = [RequestMethod.GET],
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    fun getAllPaths(routeNo: String): List<BusPathInfo> {
////        log.info("route: $routeNo")
//        return busStopPathSerivce.getAllPathFromRouteNo(routeNo)
//    }
}