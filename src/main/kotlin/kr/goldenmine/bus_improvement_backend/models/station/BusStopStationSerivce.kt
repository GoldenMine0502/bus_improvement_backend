package kr.goldenmine.bus_improvement_backend.models.station

import kr.goldenmine.bus_improvement_backend.util.Point
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.sin

@Service
class BusStopStationSerivce @Autowired constructor(
    private val busStopStationRepository: BusStopStationRepository
) {
    private var singleton: List<BusStopStationInfo>? = null

    fun listSingleton(): List<BusStopStationInfo> {
        if(singleton == null) {
            singleton = list()
        }
        return singleton!!
    }

    fun list(): List<BusStopStationInfo> {
        return busStopStationRepository.findAll()
    }

    fun getAllFromTo(start: Point, finish: Point): List<BusStopStationInfo> {
        return busStopStationRepository.getAllFromTo(start.x, start.y, finish.x, finish.y)
    }

    operator fun get(id: Int): BusStopStationInfo? {
        return busStopStationRepository.findById(id).orElseGet { null }
    }
}