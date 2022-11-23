package kr.goldenmine.bus_improvement_backend

import kr.goldenmine.bus_improvement_backend.calc.*
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
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
@RequestMapping("/calculate")
class BusCalculatorController(
    val calculators: List<BusCalculator>
) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculatorController::class.java)

    val summaryDijkstraShortest: HashMap<String, List<Int>>
    val summaryDijkstraRouteCurve: HashMap<String, List<Int>>

    init {
//        calculators.forEach {
//            log.info("calculating ${it.type}")
//            it.calculate()
//        }

        calculators.first { it.type == "DijkstraMinimumDistance" }.calculate()
//        calculators.first { it.type == "DijkstraRouteCurve" }.calculate()

        log.info("all calculated.")

        summaryDijkstraShortest = (calculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).routes
        summaryDijkstraRouteCurve = (calculators.first { it.type == "DijkstraRouteCurve" } as BusCalculatorDijkstraRouteCurve).routes

//        summaryDijkstraShortest = BusSummaryDijkstra(calculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).getSummary()
        log.info("summary shortest completed.")
    }

    @RequestMapping(
        value = ["/optimizedroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOptimizedRoute(routeNo: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return summaryDijkstraRouteCurve[routeNo]
    }

    @RequestMapping(
        value = ["/shortestroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getShortest(routeNo: String): List<Int>? {
//        val center = convertWGS84toTM127(Point(x, y))
//        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
//        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))
        return summaryDijkstraShortest[routeNo]
    }
}