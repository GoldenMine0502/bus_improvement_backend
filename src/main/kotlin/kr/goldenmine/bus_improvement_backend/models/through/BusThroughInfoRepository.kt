package kr.goldenmine.bus_improvement_backend.models.through

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusThroughInfoRepository : JpaRepository<BusThroughInfo, Int> { //    User findById(String id);

    // info.posX >= :startX AND info.posY >= :startY AND info.posX <= :finishX AND info.posY <= :finishY
//    @Query(
//        "FROM BusThroughInfo as info WHERE info.busStopStationId IN (" +
//                "SELECT busStopStationInfo.id FROM BusStopStationInfo as busStopStationInfo WHERE " +
//                "busStopStationInfo.posX >= :startX AND busStopStationInfo.posY >= :startY AND " +
//                "busStopStationInfo.posX <= :finishX AND busStopStationInfo.posY <= :finishY" +
//            ")"
//    )

    // @Query("SELECT new my.class.package.Chars(c.name,c.image) FROM characters c")
    @Query(
        "SELECT new kr.goldenmine.bus_improvement_backend.models.through.BusThroughPositionInfo(through.routeId, through.busStopStationId, through.busStopSequence, station.posX, station.posY) " +
                "FROM BusThroughInfo as through INNER JOIN " +
                "BusStopStationInfo as station ON through.busStopStationId = station.id WHERE " +
                "station.posX >= :startX AND station.posY >= :startY AND " +
                "station.posX <= :finishX AND station.posY <= :finishY"
    )
    fun getAllFromTo(startX: Double, startY: Double, finishX: Double, finishY: Double): List<BusThroughPositionInfo>

}