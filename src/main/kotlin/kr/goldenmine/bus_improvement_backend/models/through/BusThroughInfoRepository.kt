package kr.goldenmine.bus_improvement_backend.models.through

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

// Spring JPA
@Repository
interface BusThroughInfoRepository : JpaRepository<BusThroughInfo, Int> { //    User findById(String id);

}