package kr.goldenmine.bus_improvement_backend.models.path

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
import kr.goldenmine.bus_improvement_backend.util.Point
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

    fun getAllFromTo(start: Point, finish: Point): List<BusPathInfo> {
        return busStopPathRepository.getAllPathFromTo(start.x, start.y, finish.x, finish.y)
    }

    fun getAllPathFromRouteNo(routeNo: String): List<BusPathInfo> {
        return busStopPathRepository.getAllPathFromRouteNo(routeNo)
    }

    operator fun get(id: Int): BusPathInfo? {
        return busStopPathRepository.findById(id).orElseGet { null }
    }
}