package kr.goldenmine.bus_improvement_backend.models.traffic

import kr.goldenmine.bus_improvement_backend.models.through.BusThroughPositionInfo
import kr.goldenmine.bus_improvement_backend.util.Point
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusTrafficSerivce @Autowired constructor(
    private val busTrafficRepository: BusTrafficRepository
) {
//    fun add(info: BusStopStationInfo): BusStopStationInfo {
//        return busStopStationRepository.saveAndFlush(info)
//    }

    fun list(): List<BusTrafficInfo> {
        return busTrafficRepository.findAll()
    }

    fun getAllFromTo(start: Point, finish: Point): List<BusTrafficInfo> {
        return busTrafficRepository.getAllFromTo(start.x, start.y, finish.x, finish.y)
    }

    operator fun get(id: Int): BusTrafficInfo? {
        return busTrafficRepository.findById(id).orElseGet { null }
    }
}