package kr.goldenmine.bus_improvement_backend.models.traffic

import kr.goldenmine.bus_improvement_backend.models.through.BusThroughPositionInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusTrafficRepository : JpaRepository<BusTrafficInfo, Int> {
    @Query(
//        "SELECT new kr.goldenmine.bus_improvement_backend.models.through.BusThroughPositionInfo(through.routeId, through.busStopStationId, through.busStopSequence, station.posX, station.posY) " +
        "FROM BusTrafficInfo as through WHERE through.shortId IN (" +
                "SELECT station.shortId FROM BusStopStationInfo as station " +
                "WHERE station.posX >= :startX AND station.posY >= :startY AND " +
                "station.posX <= :finishX AND station.posY <= :finishY" +
        ")"
    )
    fun getAllFromTo(startX: Double, startY: Double, finishX: Double, finishY: Double): List<BusTrafficInfo>

}