package kr.goldenmine.bus_improvement_backend.models.node

import com.google.gson.annotations.SerializedName
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

// 노선,                      기종점,정류장순번,    정류장명,04시,05시,06시,07시,08시,09시,10시,11시,12시,13시,14시,15시,16시,17시,18시,19시,20시,21시,22시,23시,00시,01시,02시,03시,
//  0,                          1, 2,              3,  4,5,6,7, 8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27
// 58,�۵���2������ - �۵���2������,21,�۵�ǳ�����̿�2����,  0,3,3,8,11,4, 4,14, 7, 7, 9,11,15,18,20,14,15,15,22,10, 0, 0, 0, 0,
@Entity
@Table(name = "bus_traffic_node_info")
class BusTrafficNodeInfo(
    @Id
    @SerializedName("id")
    @Column(name = "id")
    val id: Int? = null,

    @SerializedName("routeNo")
    @Column(name = "route_no")
    val routeNo: String? = null,

    @SerializedName("sequence")
    @Column(name = "sequence")
    val sequence: Int? = null,

    @SerializedName("time00")
    @Column(name = "time00")
    val time00: Int? = null,

    @SerializedName("time01")
    @Column(name = "time01")
    val time01: Int? = null,

    @SerializedName("time02")
    @Column(name = "time02")
    val time02: Int? = null,

    @SerializedName("time03")
    @Column(name = "time03")
    val time03: Int? = null,

    @SerializedName("time04")
    @Column(name = "time04")
    val time04: Int? = null,

    @SerializedName("time05")
    @Column(name = "time05")
    val time05: Int? = null,

    @SerializedName("time06")
    @Column(name = "time06")
    val time06: Int? = null,

    @SerializedName("time07")
    @Column(name = "time07")
    val time07: Int? = null,

    @SerializedName("time08")
    @Column(name = "time08")
    val time08: Int? = null,

    @SerializedName("time09")
    @Column(name = "time09")
    val time09: Int? = null,

    @SerializedName("time10")
    @Column(name = "time10")
    val time10: Int? = null,

    @SerializedName("time11")
    @Column(name = "time11")
    val time11: Int? = null,

    @SerializedName("time12")
    @Column(name = "time12")
    val time12: Int? = null,

    @SerializedName("time13")
    @Column(name = "time13")
    val time13: Int? = null,

    @SerializedName("time14")
    @Column(name = "time14")
    val time14: Int? = null,

    @SerializedName("time15")
    @Column(name = "time15")
    val time15: Int? = null,

    @SerializedName("time16")
    @Column(name = "time16")
    val time16: Int? = null,

    @SerializedName("time17")
    @Column(name = "time17")
    val time17: Int? = null,

    @SerializedName("time18")
    @Column(name = "time18")
    val time18: Int? = null,

    @SerializedName("time19")
    @Column(name = "time19")
    val time19: Int? = null,

    @SerializedName("time20")
    @Column(name = "time20")
    val time20: Int? = null,

    @SerializedName("time21")
    @Column(name = "time21")
    val time21: Int? = null,

    @SerializedName("time22")
    @Column(name = "time22")
    val time22: Int? = null,

    @SerializedName("time23")
    @Column(name = "time23")
    val time23: Int? = null,

) {
    fun getTotal(): Int {
        return (time00 ?: 0) + (time01 ?: 0) + (time02 ?: 0) + (time03 ?: 0) + (time04 ?: 0) + (time05 ?: 0) +
                (time06 ?: 0) + (time07 ?: 0) + (time08 ?: 0) + (time09 ?: 0) + (time10 ?: 0) + (time11 ?: 0) +
                (time12 ?: 0) + (time13 ?: 0) + (time14 ?: 0) + (time15 ?: 0) + (time16 ?: 0) + (time17 ?: 0) +
                (time18 ?: 0) + (time19 ?: 0) + (time20 ?: 0) + (time21 ?: 0) + (time22 ?: 0) + (time23 ?: 0)
    }

    override fun toString(): String {
        return "BusTrafficNodeInfo(id=$id, routeNo=$routeNo, sequence=$sequence, time00=$time00, time01=$time01, time02=$time02, time03=$time03, time04=$time04, time05=$time05, time06=$time06, time07=$time07, time08=$time08, time09=$time09, time10=$time10, time11=$time11, time12=$time12, time13=$time13, time14=$time14, time15=$time15, time16=$time16, time17=$time17, time18=$time18, time19=$time19, time20=$time20, time21=$time21, time22=$time22, time23=$time23)"
    }

}