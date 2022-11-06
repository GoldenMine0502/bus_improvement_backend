package kr.goldenmine.bus_improvement_backend.models.through

import com.google.gson.annotations.SerializedName
import lombok.Data
import javax.persistence.*

//@Data
@Entity
@Table(name = "bus_through_info")
//@SequenceGenerator(name = "bus_through_info_sequence_generator", sequenceName = "bus_stop_sequence")
class BusThroughInfo(
    /*
    	id INT(11) NOT NULL PRIMARY KEY,
	route_id VARCHAR(20),
    bus_stop_station_id VARCHAR(20),
    bus_stop_sequence INT(11)
     */
    @Id
//    @GeneratedValue
    @SerializedName("id")
    @Column(name = "id")
    val id: Int? = null,

    @SerializedName("route_id")
    @Column(name = "route_id")
    val routeId: String? = null,

    @SerializedName("bus_stop_station_id")
    @Column(name = "bus_stop_station_id")
    val busStopStationId: Int? = null,

    @SerializedName("bus_stop_sequence")
    @Column(name = "bus_stop_sequence")
    val busStopSequence: Int? = null,
    ) {
}