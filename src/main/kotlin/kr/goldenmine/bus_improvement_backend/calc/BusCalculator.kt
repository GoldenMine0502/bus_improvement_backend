package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

abstract class BusCalculator(
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoSerivce: BusThroughInfoSerivce,
    private val busTrafficSerivce: BusTrafficSerivce
) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculator::class.java)

    val stations = busStopStationService.list()
    val throughs = busThroughInfoSerivce.list()
    val traffics = busTrafficSerivce.list()

    val stationsIdToIndexMap = HashMap<Int, Int>()
    val stationsShortIdToIdMap = HashMap<Int, Int>()
    val trafficIdToTrafficAmountMap = HashMap<Int, Int>()

    open fun calculate() {
        // 버스정류장 인덱스를 얻기 위한 맵 id to index
        for (stationIndex in stations.indices) {
            val station = stations[stationIndex]

            if (station.id != null)
                stationsIdToIndexMap[station.id] = stationIndex
        }

        for (station in stations) {
            if (station.shortId != null && station.id != null)
                stationsShortIdToIdMap[station.shortId] = station.id
        }

        // 트래픽 정보를 얻기 위한 map
        for (traffic in traffics) {
            if (traffic.shortId != null) {
                val id = stationsShortIdToIdMap[traffic.shortId]!!
                val previous = trafficIdToTrafficAmountMap[id] ?: 0
                trafficIdToTrafficAmountMap[id] = previous + traffic.totalOn() + traffic.totalOff()
            }
        }
    }

    abstract val type: String
}