package kr.goldenmine.bus_improvement_backend.controllers

import com.google.gson.JsonObject
import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraMinimumDistance
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stat")
class BusStatisticsCalculator(
    val busThroughInfoSerivce: BusThroughInfoSerivce,
    val busCalculatorDijkstraMinimumDistance: BusCalculatorDijkstraMinimumDistance,
) {

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