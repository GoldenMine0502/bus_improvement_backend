package kr.goldenmine.bus_improvement_backend.models.path

import com.google.gson.annotations.SerializedName
import javax.persistence.*

/*
CREATE TABLE bus_path_info(
	id INT(11) NOT NULL PRIMARY KEY,
    from_id INT(11),
    to_id INT(11),
    sequence INT(11),
    pos_x DOUBLE,
    pos_y DOUBLE
);
@Entity
@Table(name = "bus_info")
class BusStopInfo(
    @Id
    @SerializedName("route_id")
    @Column(name = "route_id")
    val routeId: String? = null,
 */
@Entity
@Table(name = "bus_path_info")
class BusPathInfo(
    @Id
    @SerializedName("id")
    @Column(name = "id")
    val id: Int? = null,

    @SerializedName("routeNo")
    @Column(name = "route_no")
    var routeNo: String? = null,

    @SerializedName("fromId")
    @Column(name = "from_id")
    val fromId: Int? = null,

    @SerializedName("toId")
    @Column(name = "to_id")
    val toId: Int? = null,

    @SerializedName("sequence")
    @Column(name = "sequence")
    val sequence: Int? = null,

    @SerializedName("posX")
    @Column(name = "pos_x")
    val posX: Double? = null,

    @SerializedName("posY")
    @Column(name = "pos_y")
    val posY: Double? = null,
)