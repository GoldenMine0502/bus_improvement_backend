package kr.goldenmine.bus_improvement_backend.calc.data

import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce

class DijkstraData(
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoSerivce: BusThroughInfoSerivce,
) {
    val stations = busStopStationService.list()
    val throughs = busThroughInfoSerivce.list()
    val busThroughMap: HashMap<String, ArrayList<BusThroughInfo>> = HashMap() // route id to list of bus through info

    init {
        for (through in throughs) {
            if (through.routeId != null && through.busStopStationId != null) {
                val station = stations[BusCalculator.stationsIdToIndexMap[through.busStopStationId]!!]

                if (station.id != null) {
                    val list = busThroughMap[through.routeId]
                    if (list != null) {
                        list.add(through)
                    } else {
                        busThroughMap[through.routeId] = ArrayList(listOf(through))
                    }
                }
            }
        }
    }
}