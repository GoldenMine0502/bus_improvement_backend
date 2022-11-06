package kr.goldenmine.bus_improvement_backend.models.stop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusStopInfoSerivce @Autowired constructor(
    private val busStopInfoRepository: BusStopInfoRepository
) {
//    fun add(info: BusStopStationInfo): BusStopStationInfo {
//        return busStopStationRepository.saveAndFlush(info)
//    }

    fun list(): List<BusStopInfo> {
        return busStopInfoRepository.findAll()
    }

    operator fun get(id: Int): BusStopInfo? {
        return busStopInfoRepository.findById(id).orElseGet { null }
    }
}