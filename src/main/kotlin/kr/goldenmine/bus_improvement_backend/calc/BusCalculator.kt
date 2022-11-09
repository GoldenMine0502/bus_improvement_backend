package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.BusController
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BusCalculator(
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoSerivce: BusThroughInfoSerivce,
    private val busTrafficSerivce: BusTrafficSerivce
) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculator::class.java)

    protected val stations = busStopStationService.list()
    protected val throughs = busThroughInfoSerivce.list()
    protected val traffics = busTrafficSerivce.list()

    protected val stationsMap = HashMap<Int, Int>()
    protected val trafficMap = HashMap<Int, Int>()
    protected val stationsShortIdMap = HashMap<Int, Int>()

    fun calculate() {
        // 버스정류장 인덱스를 얻기 위한 맵 id to index
        for(stationIndex in stations.indices) {
            val station = stations[stationIndex]

            if(station.id != null)
                stationsMap[station.id] = stationIndex
        }

        for(station in stations) {
            if(station.shortId != null && station.id != null)
                stationsShortIdMap[station.shortId] = station.id
        }

        // 트래픽 정보를 얻기 위한 map
        for(traffic in traffics) {
            if(traffic.routeNo != null) {
                val id = stationsShortIdMap[traffic.shortId]!!
                val previous = trafficMap[id] ?: 0
                trafficMap[id] = previous + traffic.totalOn() + traffic.totalOff()
            }
        }

//        trafficMap.forEach { (t, u) ->
//            log.info("key: $t, value: $u")
//        }

        // 노드 생성
        val nodes = ArrayList<ArrayList<Int>>()
        repeat(stations.size + 1) {
            nodes.add(ArrayList())
        }

        for(i in 0 until throughs.size - 1) {
            val start = stationsMap[throughs[i].busStopStationId]
            val finish = stationsMap[throughs[i + 1].busStopStationId]

            if(start != null && finish != null) {
                nodes[start].add(finish)
            }
        }

        // 다익스트라 알고리즘 실행

    }
}