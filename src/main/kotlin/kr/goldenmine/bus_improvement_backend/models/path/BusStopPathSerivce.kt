package kr.goldenmine.bus_improvement_backend.models.path

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusStopPathSerivce @Autowired constructor(
    private val busStopPathRepository: BusStopPathRepository
) {
//    fun add(info: BusStopStationInfo): BusStopStationInfo {
//        return busStopStationRepository.saveAndFlush(info)
//    }

    fun list(): List<BusPathInfo> {
        return busStopPathRepository.findAll()
    }

    operator fun get(id: Int): BusPathInfo? {
        return busStopPathRepository.findById(id).orElseGet { null }
    }
}