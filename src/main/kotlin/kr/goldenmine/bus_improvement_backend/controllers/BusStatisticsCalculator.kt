package kr.goldenmine.bus_improvement_backend.controllers

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraMinimumDistance
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraNodeTotal
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraNodeTotalGreedy
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stat")
class BusStatisticsCalculator(
    val busThroughInfoSerivce: BusThroughInfoSerivce,
    val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
    val busCalculatorDijkstraNodeTotal: BusCalculatorDijkstraNodeTotal,
    val busCalculatorDijkstraNodeTotalGreedy: BusCalculatorDijkstraNodeTotalGreedy,
) {
    private val log: Logger = LoggerFactory.getLogger(BusStatisticsCalculator::class.java)

    init {

//        log.info("calculating minimumdistance")
//        busCalculatorDijkstraMinimumDistance.calculate()
//
//        for(i in 1..50) {
//            log.info("calculating nodetotalgreedy $i")
//            busCalculatorDijkstraNodeTotalGreedy.busTransfer = i
//            busCalculatorDijkstraNodeTotalGreedy.calculate()
//            busCalculatorDijkstraNodeTotalGreedy.busUsageArray
//            busCalculatorDijkstraNodeTotalGreedy.busUsageArray.add(getStatForNodeGreedy())
//        }
    }

    @RequestMapping(
        value = ["/allshortestroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getStatForShortest(): String {
        val calculator = busCalculatorDijkstraMinimumDistance

        val stationsArray = IntArray(calculator.stations.size)
        var totalCount = 0

        var distanceSum = 0.0

        calculator.routes.forEach { (t, throughs) ->
            for(index in throughs.indices) {
                val first = throughs[index]
                val second = if(index < throughs.size - 1) throughs[index + 1] else first

                if(index < throughs.size - 1) {
                    val firstStation = calculator.stations[first]
                    val secondStation = calculator.stations[second]
                    distanceSum += distanceTM127(
                        Point(firstStation.posX!!, firstStation.posY!!),
                        Point(secondStation.posX!!, secondStation.posY!!),
                    )
                }
            }

            throughs.forEach {
                stationsArray[it]++
                totalCount++
            }
        }

        val usedStations = stationsArray.asSequence().count { it > 0 }

        val jsonObject = JsonObject()

//        val jsonArray = JsonArray()
//        for(index in stationsArray.indices) {
//            jsonArray.add(stationsArray[index])
//        }
//        jsonObject.add("array", jsonArray)
        jsonObject.addProperty("usedStations", usedStations)
        jsonObject.addProperty("totalDistance", distanceSum)
        jsonObject.addProperty("totalNodes", totalCount)

        return jsonObject.toString()
    }

    @RequestMapping(
        value = ["/allnoderoute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getStatForNode(): String {
        val calculator = busCalculatorDijkstraNodeTotal

        val stationsArray = IntArray(calculator.stations.size)
        var totalCount = 0

        var distanceSum = 0.0

        calculator.routes.forEach { (t, throughs) ->
            for(index in throughs.indices) {
                val first = throughs[index]
                val second = if(index < throughs.size - 1) throughs[index + 1] else first

                if(index < throughs.size - 1) {
                    val firstStation = calculator.stations[first]
                    val secondStation = calculator.stations[second]
                    distanceSum += distanceTM127(
                        Point(firstStation.posX!!, firstStation.posY!!),
                        Point(secondStation.posX!!, secondStation.posY!!),
                    )
                }
            }

            throughs.forEach {
                stationsArray[it]++
                totalCount++
            }
        }

        val usedStations = stationsArray.asSequence().count { it > 0 }

        val jsonObject = JsonObject()

//        val jsonArray = JsonArray()
//        for(index in stationsArray.indices) {
//            jsonArray.add(stationsArray[index])
//        }
//        jsonObject.add("array", jsonArray)
        jsonObject.addProperty("usedStations", usedStations)
        jsonObject.addProperty("totalDistance", distanceSum)
        jsonObject.addProperty("totalNodes", totalCount)

        return jsonObject.toString()
    }

    @RequestMapping(
        value = ["/allnodegreedyroutefromto"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllStatForNodeGreedy(): String {
        val gson = Gson()

        return gson.toJson(busCalculatorDijkstraNodeTotalGreedy.busUsageArray)
    }

    // model 3
    @RequestMapping(
        value = ["/allnodegreedyroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getStatForNodeGreedy(): String {
        val calculator = busCalculatorDijkstraNodeTotalGreedy

        val stationsArray = IntArray(calculator.stations.size)
        var totalCount = 0

        var distanceSum = 0.0

        calculator.routes.forEach { (t, throughs) ->
            for(index in throughs.indices) {
                val first = throughs[index]
                val second = if(index < throughs.size - 1) throughs[index + 1] else first

                if(index < throughs.size - 1) {
                    val firstStation = calculator.stations[first]
                    val secondStation = calculator.stations[second]
                    distanceSum += distanceTM127(
                        Point(firstStation.posX!!, firstStation.posY!!),
                        Point(secondStation.posX!!, secondStation.posY!!),
                    )
                }
            }

            throughs.forEach {
                stationsArray[it]++
                totalCount++
            }
        }

        val usedStations = stationsArray.asSequence().count { it > 0 }

        val jsonObject = JsonObject()

        var remain = 0
        val counts = ArrayList<Int>()
        repeat(51) {
            counts.add(0)
        }
        calculator.adjointMatrixUsers.forEach { array ->
            array.forEach {
                remain += it
                if(it > 0) {
                    val k = (it / 500).coerceAtMost(50)
                    counts[k]++
                }
            }
        }
        val jsonArray = JsonArray()
        for(index in counts.indices) {
            jsonArray.add(counts[index])
        }

        jsonObject.addProperty("totalUsers", calculator.totalUsage)
        jsonObject.addProperty("remainUsers", remain)
        jsonObject.add("histogram500", jsonArray)
        jsonObject.addProperty("usedStations", usedStations)
        jsonObject.addProperty("totalDistance", distanceSum)
        jsonObject.addProperty("totalNodes", totalCount)

        return jsonObject.toString()
    }

    @RequestMapping(
        value = ["/originalroute"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getStatForOriginal(): String {
        val calculator = busCalculatorDijkstraMinimumDistance
        val throughs = busThroughInfoSerivce.listSingleton()

        val stationsArray = IntArray(calculator.stations.size)

        var totalCount = 0

        var distanceSum = 0.0

        for(index in throughs.indices) {
            val first = throughs[index]
            val second = if(index < throughs.size - 1) throughs[index + 1] else first

            if(first.routeId == second.routeId) {
                val stationIndex = BusCalculator.stationsIdToIndexMap[first.busStopStationId]!!
                stationsArray[stationIndex]++
                totalCount++
            }
            if(index < throughs.size - 1) {
                val firstStation = calculator.stations[BusCalculator.stationsIdToIndexMap[first.busStopStationId]!!]
                val secondStation = calculator.stations[BusCalculator.stationsIdToIndexMap[second.busStopStationId]!!]
                distanceSum += distanceTM127(
                    Point(firstStation.posX!!, firstStation.posY!!),
                    Point(secondStation.posX!!, secondStation.posY!!),
                )
            }
        }

        val usedStations = stationsArray.asSequence().count { it > 0 }

        val jsonObject = JsonObject()
        jsonObject.addProperty("usedStations", usedStations)
        jsonObject.addProperty("totalDistance", distanceSum)
        jsonObject.addProperty("totalNodes", totalCount)

        return jsonObject.toString()
    }
}