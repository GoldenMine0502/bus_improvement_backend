package kr.goldenmine.bus_improvement_backend.models.stop

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusStopInfoRepository : JpaRepository<BusStopInfo, Int> { //    User findById(String id);

}