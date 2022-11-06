package kr.goldenmine.bus_improvement_backend.models.path

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BusStopPathRepository : JpaRepository<BusPathInfo, Int> {
    @Query("FROM BusPathInfo as busPathInfo WHERE busPathInfo.fromId IN (" +
                "SELECT busStopStationInfo.id FROM BusStopStationInfo as busStopStationInfo WHERE busStopStationInfo.id IN (" +
                    "SELECT busThroughInfo.busStopStationId FROM BusThroughInfo as busThroughInfo WHERE busThroughInfo.routeId IN (" +
                        "SELECT busInfo.routeId FROM BusStopInfo as busInfo WHERE busInfo.routeId = :routeNo" +
                    ")" +
                ")" +
            ")"
    )
    fun getAllPathFromRouteNo(routeNo: String): List<BusPathInfo>

    @Query("FROM BusPathInfo busPathInfo WHERE busPathInfo.fromId = ?1 AND busPathInfo.toId = ?2",)
    fun getAllPathFromTo(from: String, to: String): List<BusPathInfo>
}