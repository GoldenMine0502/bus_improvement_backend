package kr.goldenmine.bus_improvement_backend.models.bus

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusInfoRepository : JpaRepository<BusInfo, Int> { //    User findById(String id);

}