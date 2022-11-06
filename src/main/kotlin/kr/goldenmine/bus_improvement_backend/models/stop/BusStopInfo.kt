package kr.goldenmine.bus_improvement_backend.models.stop

import com.google.gson.annotations.SerializedName
import javax.persistence.*

/*
	route_id VARCHAR(20) NOT NULL PRIMARY KEY,
    route_len INT(11),
    route_no VARCHAR(20),
	origin_bus_stop_id VARCHAR(20),
    dest_bus_stop_id VARCHAR(20),
    bus_start_time VARCHAR(10),
    bus_finish_time VARCHAR(10),
    max_allocation_gap INT(11),
    min_allocation_gap INT(11),
    route_type INT(11),
    turn_bus_stop_id VARCHAR(20)
 */
//@Data
@Entity
@Table(name = "bus_info")
class BusStopInfo(
    @Id
    @SerializedName("route_id")
    @Column(name = "route_id")
    val routeId: String? = null,

    @SerializedName("route_len")
    @Column(name = "route_len")
    val routeLen: Int? = null,

    @SerializedName("route_no")
    @Column(name = "route_no")
    val routeNo: String? = null,

    @SerializedName("origin_bus_stop_id")
    @Column(name = "origin_bus_stop_id")
    val originBusStopId: String? = null,

    @SerializedName("dest_bus_stop_id")
    @Column(name = "dest_bus_stop_id")
    val destBusStopId: String? = null,

    @SerializedName("bus_start_time")
    @Column(name = "bus_start_time")
    val busStartTime: String? = null,

    @SerializedName("bus_finish_time")
    @Column(name = "bus_finish_time")
    val busFinishTime: String? = null,

    @SerializedName("max_allocation_gap")
    @Column(name = "max_allocation_gap")
    val maxAllocationGap: Int? = null,

    @SerializedName("min_allocation_gap")
    @Column(name = "min_allocation_gap")
    val minAllocationGap: Int? = null,

    @SerializedName("route_type")
    @Column(name = "route_type")
    val routeType: Int? = null,

    @SerializedName("turn_bus_stop_id")
    @Column(name = "turn_bus_stop_id")
    val turnBusStopId: String? = null,
) {
    override fun hashCode(): Int {
        return routeId.toString().toInt()
    }

    override fun equals(other: Any?): Boolean {
        if(other is BusStopInfo) {
            return routeId == other.routeId
        }

        return false
    }
}