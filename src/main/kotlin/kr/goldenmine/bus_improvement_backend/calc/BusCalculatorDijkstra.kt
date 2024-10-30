package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfo
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

abstract class BusCalculatorDijkstra(
    private val busInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce
) : BusCalculatorRouteCurve(busInfoService, busStopStationService, busThroughInfoService, busTrafficService) {

    private val log: Logger = LoggerFactory.getLogger(BusCalculatorDijkstra::class.java)

    val routes = HashMap<String, List<Int>>()
    val routeSequences = ArrayList<String>()

    /*
    2022-11-30 18:02:39.981  INFO 12751 --- [           main] k.g.b.calc.BusCalculatorDijkstra         : calculating 165000452 3280 3927 3927
2022-11-30 18:02:40.098  INFO 12751 --- [           main] k.g.b.c.BusCalculatorDijkstraNodeTotal   : minimumDistance first: 10131.15722971598
2022-11-30 18:02:40.559  INFO 12751 --- [           main] k.g.b.c.BusCalculatorDijkstraNodeTotal   : minimumDistance first: 0.0
     */

    /*
    2022-11-30 18:02:50.053  INFO 12751 --- [           main] k.g.b.calc.BusCalculatorDijkstra         : calculating 165000315 603 148 148
2022-11-30 18:02:50.174  INFO 12751 --- [           main] k.g.b.c.BusCalculatorDijkstraNodeTotal   : minimumDistance first: 12525.20486447588
2022-11-30 18:02:50.647  INFO 12751 --- [           main] k.g.b.c.BusCalculatorDijkstraNodeTotal   : minimumDistance first: 0.0
     */

    /*
    2022-11-30 18:05:09.580  INFO 12751 --- [           main] k.g.b.calc.BusCalculatorDijkstra         : calculating 168000006 3881 275 275
2022-11-30 18:05:09.700  INFO 12751 --- [           main] k.g.b.c.BusCalculatorDijkstraNodeTotal   : minimumDistance first: 26224.05808187709
2022-11-30 18:05:10.159  INFO 12751 --- [           main] k.g.b.c.BusCalculatorDijkstraNodeTotal   : minimumDistance first: 0.0
     */
    override fun calculate() {
        super.calculate()

        routeSequences.clear()
        routeSequences.addAll(turnPoints.keys)

        calculatePre()

        var c = 0
        routeSequences.forEach { k ->
            val v = turnPoints[k]

            val busInfo = routeIdToBusInfo[k]

            if (busInfo != null) {
                val stationStart = stationsIdToIndexMap[busInfo.originBusStopId!!]
                val stationTurn = stationsIdToIndexMap[v]
                val stationEnd = stationsIdToIndexMap[endPoints[k]]

                c++
                if(c >= 10) {
                    log.info("calculating $k $stationStart $stationTurn $stationEnd")
                    c = 0
                }

                if (stationStart != null && stationTurn != null && stationEnd != null) {
                    val result = ArrayList(executeDijkstra(stationStart, stationTurn))
                    val result2 = ArrayList(executeDijkstra(stationTurn, stationEnd))

                    result.addAll(result2)

                    processAfter(busInfo, result)

                    routes[k] = result
                }
            }
        }
    }

    open fun calculatePre() {

    }

    open fun processAfter(busInfo: BusInfo, trace: List<Int>) {

    }

    abstract fun executeDijkstra(startIndex: Int, endIndex: Int): List<Int>

    fun getTraceBack(previousIndices: IntArray, startIndex: Int, endIndex: Int): List<Int> {
        val previousNodes = ArrayList<Int>()

        var current = endIndex

        while (true) {
            val previous = previousIndices[current]

//            log.info("$previous, $current, $startIndex, $endIndex")

            // 시작노드까지 싹싹 긁어서 add
            previousNodes.add(current)

            // 시작 노드에 도달했다는 뜻이므로 break
            if (previous == startIndex || previous == current) break

//            if(previous == current) sleep(1000L)

            current = previous
        }

        previousNodes.add(startIndex)

        return previousNodes.reversed()
    }

    fun getDistance(traceBack: List<Int>): Double {
        var totalDistance = 0.0

        for (index in 0 until traceBack.size - 1) {
            val start = traceBack[index]
            val finish = traceBack[index + 1]

            val stationStart = stations[start]
            val stationFinish = stations[finish]

            val distance = distanceTM127(
                Point(stationStart.posX!!, stationStart.posY!!),
                Point(stationFinish.posX!!, stationFinish.posY!!)
            )

            totalDistance += distance
        }

        return totalDistance
    }

    fun parseBusTimeHour(busTime: String): Int {
        return when(busTime.length) {
            1 -> 0
            2 -> 0
            3 -> busTime.substring(0, 1).toInt()
            4 -> busTime.substring(0, 2).toInt()
            else -> throw RuntimeException("cannot parse bus time(h)")
        }
    }

    fun parseBusTimeMinute(busTime: String): Int {
        return when(busTime.length) {
            1 -> busTime.substring(0, 1).toInt()
            2 -> busTime.substring(0, 2).toInt()
            3 -> busTime.substring(1, 3).toInt()
            4 -> busTime.substring(2, 4).toInt()
            else -> throw RuntimeException("cannot parse bus time(m)")
        }
    }
}