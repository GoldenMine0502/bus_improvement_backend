package kr.goldenmine.bus_improvement_backend.models.through

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusThroughInfoSerivce @Autowired constructor(
    private val busThroughInfoRepository: BusThroughInfoRepository
) {
//    fun add(info: BusStopStationInfo): BusStopStationInfo {
//        return busStopStationRepository.saveAndFlush(info)
//    }

    fun list(): List<BusThroughInfo> {
        return busThroughInfoRepository.findAll()
    }

    operator fun get(id: Int): BusThroughInfo? {
        return busThroughInfoRepository.findById(id).orElseGet { null }
    }
}