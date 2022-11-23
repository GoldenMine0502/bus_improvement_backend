package kr.goldenmine.bus_improvement_backend

import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraMinimumDistance
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class BusCalculatorMinimumDistanceTest {

    val routeId = "161000007"

    @Autowired
    private lateinit var busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance

    @Test
    fun calculateMinimumDistance() {
        busCalculatorDijkstraMinimumDistance.calculate()
        val routes = busCalculatorDijkstraMinimumDistance.routes

        println(routes[routeId])
    }
}