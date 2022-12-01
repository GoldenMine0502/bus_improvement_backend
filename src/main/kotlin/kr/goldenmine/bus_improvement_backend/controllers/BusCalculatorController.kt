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
    val busCalculatorDijkstraNodeTotalGreedy5: BusCalculatorDijkstraNodeTotalGreedy5,
    val busCalculatorDijkstraNodeTotalGreedy25: BusCalculatorDijkstraNodeTotalGreedy25,
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
//            for(i in 1..50) {
//                log.info("calculating nodetotalgreedy $i")
//                busCalculatorDijkstraNodeTotalGreedy.busTransfer = i
            busCalculatorDijkstraNodeTotalGreedy5.calculate()

//            }
        }

        val t3 = Thread {
//            for(i in 1..50) {
//                log.info("calculating nodetotalgreedy $i")
//                busCalculatorDijkstraNodeTotalGreedy.busTransfer = i
            busCalculatorDijkstraNodeTotalGreedy25.calculate()

//            }
        }

        t1.start()
        t2.start()
        t3.start()
        t1.join()
        t2.join()
        t3.join()

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
        value = ["/dijkstragreedy5route"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getDijkstraGreedyRoute5(routeId: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotalGreedy5.routes[routeId]
    }

    @RequestMapping(
        value = ["/dijkstragreedy25route"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getDijkstraGreedyRoute25(routeId: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotalGreedy25.routes[routeId]
    }

    @RequestMapping(
        value = ["/alldijkstragreedy5route"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllDijkstraGreedyRoute5(): HashMap<String, List<Int>> {
        return busCalculatorDijkstraNodeTotalGreedy5.routes
    }

    @RequestMapping(
        value = ["/alldijkstragreedy25route"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllDijkstraGreedyRoute25(): HashMap<String, List<Int>> {
        return busCalculatorDijkstraNodeTotalGreedy25.routes
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