package kr.goldenmine.bus_improvement_backend.models.through

import com.google.gson.annotations.SerializedName
import javax.persistence.Column
import javax.persistence.Id

class BusThroughPositionInfo(
    @SerializedName("routeId")
    @Column(name = "route_id")
    val routeId: String? = null,

    @SerializedName("busStopStationId")
    @Column(name = "bus_stop_station_id")
    val busStopStationId: Int? = null,

    @SerializedName("busStopSequence")
    @Column(name = "bus_stop_sequence")
    val busStopSequence: Int? = null,

    @SerializedName("posX")
    @Column(name = "pos_x")
    val posX: Double? = null,

    @SerializedName("posY")
    @Column(name = "pos_y")
    val posY: Double? = null,
)