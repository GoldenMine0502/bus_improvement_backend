package kr.goldenmine.bus_improvement_backend.models.bus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusInfoSerivce @Autowired constructor(
    private val busInfoRepository: BusInfoRepository
) {
//    fun add(info: BusStopStationInfo): BusStopStationInfo {
//        return busStopStationRepository.saveAndFlush(info)
//    }

    fun list(): List<BusInfo> {
        return busInfoRepository.findAll()
    }

    fun getBusInfoFromRouteNo(routeNo: String): BusInfo {
        return busInfoRepository.getBusInfoByRouteNo(routeNo).first()
    }

    fun getRouteIdFromRouteNumber(routeNo: String): String? {
        return busInfoRepository.getIdFromNo(routeNo).firstOrNull()
    }

    operator fun get(id: Int): BusInfo? {
        return busInfoRepository.findById(id).orElseGet { null }
    }
}