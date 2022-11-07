package kr.goldenmine.bus_improvement_backend.models.path

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BusStopPathRepository : JpaRepository<BusPathInfo, Int> {
    @Query( "FROM BusPathInfo as busPathInfo WHERE busPathInfo.routeNo = :routeNo AND busPathInfo.sequence = 5")
    fun getAllPathFromRouteNo(routeNo: String): List<BusPathInfo>

    @Query("FROM BusPathInfo as busPathInfo WHERE busPathInfo.fromId IN (" +
            "SELECT busStopStationInfo.id FROM BusStopStationInfo as busStopStationInfo WHERE " +
            "busStopStationInfo.posX >= :startX AND busStopStationInfo.posY >= :startY AND " +
            "busStopStationInfo.posX <= :finishX AND busStopStationInfo.posY <= :finishY" +
            ")")
    fun getAllPathFromTo(startX: Double, startY: Double, finishX: Double, finishY: Double): List<BusPathInfo>


    @Query("FROM BusPathInfo busPathInfo WHERE busPathInfo.fromId = ?1 AND busPathInfo.toId = ?2",)
    fun getAllPathFromTo(from: String, to: String): List<BusPathInfo>
}