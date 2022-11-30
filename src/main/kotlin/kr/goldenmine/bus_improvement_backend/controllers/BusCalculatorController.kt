package kr.goldenmine.bus_improvement_backend.controllers

import kr.goldenmine.bus_improvement_backend.calc.*
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calculate")
class BusCalculatorController(
    val busThroughInfoService: BusThroughInfoSerivce,
    val busStopStationService: BusStopStationSerivce,

    val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
    val busCalculatorDijkstraNodeTotal: BusCalculatorDijkstraNodeTotal,
    val busCalculatorDijkstraNodeTotalGreedy: BusCalculatorDijkstraNodeTotalGreedy,
) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculatorController::class.java)

    init {
//        calculators.forEach {
//            log.info("calculating ${it.type}")
//            it.calculate()
//        }

//        calculators.first { it.type == "DijkstraMinimumDistance" }.calculate()
//        calculators.first { it.type == "DijkstraRouteCurve" }.calculate()

        log.info("calculating minimumdistance")
        busCalculatorDijkstraMinimumDistance.calculate()

        val t1 = Thread {
            log.info("calculating nodetotal")
            busCalculatorDijkstraNodeTotal.calculate()
        }

        val t2 = Thread {
            log.info("calculating nodetotalgreedy")
            busCalculatorDijkstraNodeTotalGreedy.calculate()
        }

        t1.start()
        t2.start()
        t1.join()
        t2.join()

        log.info("all calculated.")

//        summaryDijkstraShortest = (calculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).routes
//        summaryDijkstraRouteCurve = (calculators.first { it.type == "DijkstraRouteCurve" } as BusCalculatorDijkstraRouteCurve).routes

//        summaryDijkstraShortest = BusSummaryDijkstra(calculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).getSummary()
//        log.info("summary shortest completed.")
    }

    @RequestMapping(
        value = ["/dijkstraroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getDijkstraRoute(routeId: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotal.routes[routeId]
    }

    @RequestMapping(
        value = ["/alldijkstraroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllDijkstraRoute(): HashMap<String, List<Int>> {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotal.routes
    }

    @RequestMapping(
        value = ["/dijkstragreedyroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getDijkstraGreedyRoute(routeId: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotalGreedy.routes[routeId]
    }

    @RequestMapping(
        value = ["/alldijkstragreedyroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllDijkstraGreedyRoute(): HashMap<String, List<Int>> {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotalGreedy.routes
    }

    @RequestMapping(
        value = ["/originalroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOriginal(routeId: String): List<Int>? {
        val sequences = busThroughInfoService.getThroughSequencesFromRouteId(routeId)

        return sequences
            .asSequence()
            .map {
                BusCalculator.stationsIdToIndexMap[it.busStopStationId]
            }
            .filterNotNull()
            .toList()
    }

    @RequestMapping(
        value = ["/shortestroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getShortest(routeId: String): List<Int>? {
        return busCalculatorDijkstraMinimumDistance.routes[routeId]
    }

    @RequestMapping(
        value = ["/allshortestroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getShortestAll(): HashMap<String, List<Int>> {
        return busCalculatorDijkstraMinimumDistance.routes
    }

    @RequestMapping(
        value = ["/dijkstra"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun executeDijkstra(startIndex: Int, finishIndex: Int): List<Int> {
        return busCalculatorDijkstraMinimumDistance.executeDijkstra(startIndex, finishIndex)
    }
}