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
    val busThroughInfoSerivce: BusThroughInfoSerivce,
    val busStopStationSerivce: BusStopStationSerivce,

    val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
    val busCalculatorDijkstraNodeTotal: BusCalculatorDijkstraNodeTotal,
) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculatorController::class.java)

    init {
//        calculators.forEach {
//            log.info("calculating ${it.type}")
//            it.calculate()
//        }

//        calculators.first { it.type == "DijkstraMinimumDistance" }.calculate()
//        calculators.first { it.type == "DijkstraRouteCurve" }.calculate()

        busCalculatorDijkstraMinimumDistance.calculate()
        busCalculatorDijkstraNodeTotal.calculate()

        log.info("all calculated.")

//        summaryDijkstraShortest = (calculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).routes
//        summaryDijkstraRouteCurve = (calculators.first { it.type == "DijkstraRouteCurve" } as BusCalculatorDijkstraRouteCurve).routes

//        summaryDijkstraShortest = BusSummaryDijkstra(calculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).getSummary()
        log.info("summary shortest completed.")
    }

    @RequestMapping(
        value = ["/optimizedroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOptimizedRoute(routeId: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return busCalculatorDijkstraNodeTotal.routes[routeId]
    }


    @RequestMapping(
        value = ["/originalroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOriginal(routeId: String): List<Int>? {
        val sequences = busThroughInfoSerivce.getThroughSequencesFromRouteId(routeId)

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