package kr.goldenmine.bus_improvement_backend

import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstra
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class BusImprovementBackendApplicationTests {

    @Autowired
    lateinit var calculator: BusCalculatorDijkstra

    @Test
    fun testCalculator() {
        calculator.calculate()
    }
}
