package kr.goldenmine.bus_improvement_backend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusStopStationRepository : JpaRepository<BusStopStationInfo, Int> { //    User findById(String id);
}