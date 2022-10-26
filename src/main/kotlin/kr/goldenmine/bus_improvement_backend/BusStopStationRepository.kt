package kr.goldenmine.bus_improvement_backend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BusStopStationRepository : JpaRepository<BusStopStationInfo, Int> { //    User findById(String id);
}