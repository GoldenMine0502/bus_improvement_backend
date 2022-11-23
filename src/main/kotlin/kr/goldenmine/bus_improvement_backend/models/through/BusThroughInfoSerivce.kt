package kr.goldenmine.bus_improvement_backend.models.through

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
import kr.goldenmine.bus_improvement_backend.util.Point
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusThroughInfoSerivce @Autowired constructor(
    private val busThroughInfoRepository: BusThroughInfoRepository
) {
    private var singleton: List<BusThroughInfo>? = null

    fun listSingleton(): List<BusThroughInfo> {
        if(singleton == null) {
            singleton = list()
        }
        return singleton!!
    }

    fun list(): List<BusThroughInfo> {
        return busThroughInfoRepository.findAll()
    }

    fun getAllFromTo(start: Point, finish: Point): List<BusThroughPositionInfo> {
        return busThroughInfoRepository.getAllFromTo(start.x, start.y, finish.x, finish.y)
    }

    operator fun get(id: Int): BusThroughInfo? {
        return busThroughInfoRepository.findById(id).orElseGet { null }
    }
}