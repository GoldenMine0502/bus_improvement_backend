package kr.goldenmine.bus_improvement_backend

import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstra
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraRouteCurve
import kr.goldenmine.bus_improvement_backend.calc.BusSummaryDijkstra
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.convertWGS84toTM127
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/calculator")
class BusCalculatorController(
    val busCalculators: List<BusCalculator>
) {
    val summaryDijkstraShortest: HashMap<String, List<Int>> = BusSummaryDijkstra(busCalculators.first { it.type == "DijkstraMinimumDistance" } as BusCalculatorDijkstra).getSummary()

    @RequestMapping(
        value = ["/shortest"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllBusStations(x: Double, y: Double, rangeX: Double, rangeY: Double): HashMap<String, List<Int>> {
//        val center = convertWGS84toTM127(Point(x, y))
        val start = convertWGS84toTM127(Point(x - rangeX, y - rangeY))
        val finish = convertWGS84toTM127(Point(x + rangeX, y + rangeY))

        return summaryDijkstraShortest
    }
}