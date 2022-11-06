package kr.goldenmine.bus_improvement_backend.models.station

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusStopStationRepository : JpaRepository<BusStopStationInfo, Int> { //    User findById(String id);
    // SELECT * FROM bus_stop_station_info WHERE id IN (
    //	SELECT bus_stop_station_id FROM bus_through_info WHERE route_id IN (
    //		SELECT route_id FROM bus_info WHERE route_no = '58'
    //	) ORDER BY bus_stop_sequence ASC
    //);
    @Query(
        value = "SELECT * FROM bus_stop_station_info WHERE id IN ( " +
                    "SELECT bus_stop_station_id FROM bus_through_info WHERE route_id IN ( " +
                        "SELECT route_id FROM bus_info WHERE route_no = ?1 " +
                    ") ORDER BY bus_stop_sequence ASC" +
                ")",
        nativeQuery = true
    )
    fun getBusStopStationInfoByRouteNo(routeNo: String): List<BusStopStationInfo>

    @Query(
        "FROM BusStopStationInfo as info WHERE info.posX >= :startX AND info.posY >= :startY AND info.posX <= :finishX AND info.posY <= :finishY"
    )
    fun getAllFromTo(startX: Double, startY: Double, finishX: Double, finishY: Double): List<BusStopStationInfo>
}