package kr.goldenmine.bus_improvement_backend.models.traffic

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusTrafficRepository : JpaRepository<BusTrafficInfo, Int> {

}