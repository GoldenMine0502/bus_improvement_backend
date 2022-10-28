package kr.goldenmine.bus_improvement_backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusStopStationSerivce @Autowired constructor(
    private val busStopStationRepository: BusStopStationRepository
) {
//    fun add(info: BusStopStationInfo): BusStopStationInfo {
//        return busStopStationRepository.saveAndFlush(info)
//    }

    fun list(): List<BusStopStationInfo> {
        return busStopStationRepository.findAll()
    }

    operator fun get(id: Int): BusStopStationInfo? {
        return busStopStationRepository.findById(id).orElseGet { null }
    }
}