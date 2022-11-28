package kr.goldenmine.bus_improvement_backend.models.node

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusTrafficNodeInfoSerivce @Autowired constructor(
    private val busInfoRepository: BusTrafficNodeInfoRepository
) {
    fun list(): List<BusTrafficNodeInfo> {
        return busInfoRepository.findAll()
    }

    operator fun get(id: Int): BusTrafficNodeInfo? {
        return busInfoRepository.findById(id).orElseGet { null }
    }
}