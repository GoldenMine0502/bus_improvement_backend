package kr.goldenmine.bus_improvement_backend.models.bus

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusInfoRepository : JpaRepository<BusInfo, Int> { //    User findById(String id);
    @Query(
        "SELECT info.routeId FROM BusInfo as info WHERE info.routeNo = :routeNo"
    )
    fun getIdFromNo(routeNo: String): List<String>

    fun getBusInfoByRouteNo(routeNo: String): List<BusInfo>
}