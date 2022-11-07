package kr.goldenmine.bus_improvement_backend.models.traffic

import com.google.gson.annotations.SerializedName
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "bus_traffic_info")
class BusTrafficInfo(
    @Id
    @SerializedName("id")
    @Column(name = "id")
    var id: Int? = null,

    @SerializedName("shortId")
    @Column(name = "short_id")
    var shortId: Int? = null,

    @SerializedName("date")
    @Column(name = "date")
    var date: String? = null,

    @SerializedName("userType")
    @Column(name = "user_type")
    var userType: String? = null,

    @SerializedName("routeNo")
    @Column(name = "route_no")
    var routeNo: String? = null,

    @SerializedName("time00On")
    @Column(name = "time00_on")
    var time00On: Int? = null,

    @SerializedName("time00Off")
    @Column(name = "time00_off")
    var time00Off: Int? = null,

    @SerializedName("time01On")
    @Column(name = "time01_on")
    var time01On: Int? = null,

    @SerializedName("time01Off")
    @Column(name = "time01_off")
    var time01Off: Int? = null,

    @SerializedName("time02On")
    @Column(name = "time02_on")
    var time02On: Int? = null,

    @SerializedName("time02Off")
    @Column(name = "time02_off")
    var time02Off: Int? = null,

    @SerializedName("time03On")
    @Column(name = "time03_on")
    var time03On: Int? = null,

    @SerializedName("time03Off")
    @Column(name = "time03_off")
    var time03Off: Int? = null,

    @SerializedName("time04On")
    @Column(name = "time04_on")
    var time04On: Int? = null,

    @SerializedName("time04Off")
    @Column(name = "time04_off")
    var time04Off: Int? = null,

    @SerializedName("time05On")
    @Column(name = "time05_on")
    var time05On: Int? = null,

    @SerializedName("time05Off")
    @Column(name = "time05_off")
    var time05Off: Int? = null,

    @SerializedName("time06On")
    @Column(name = "time06_on")
    var time06On: Int? = null,

    @SerializedName("time06Off")
    @Column(name = "time06_off")
    var time06Off: Int? = null,

    @SerializedName("time07On")
    @Column(name = "time07_on")
    var time07On: Int? = null,

    @SerializedName("time07Off")
    @Column(name = "time07_off")
    var time07Off: Int? = null,

    @SerializedName("time08On")
    @Column(name = "time08_on")
    var time08On: Int? = null,

    @SerializedName("time08Off")
    @Column(name = "time08_off")
    var time08Off: Int? = null,

    @SerializedName("time09On")
    @Column(name = "time09_on")
    var time09On: Int? = null,

    @SerializedName("time09Off")
    @Column(name = "time09_off")
    var time09Off: Int? = null,

    @SerializedName("time10On")
    @Column(name = "time10_on")
    var time10On: Int? = null,

    @SerializedName("time10Off")
    @Column(name = "time10_off")
    var time10Off: Int? = null,

    @SerializedName("time11On")
    @Column(name = "time11_on")
    var time11On: Int? = null,

    @SerializedName("time11Off")
    @Column(name = "time11_off")
    var time11Off: Int? = null,

    @SerializedName("time12On")
    @Column(name = "time12_on")
    var time12On: Int? = null,

    @SerializedName("time12Off")
    @Column(name = "time12_off")
    var time12Off: Int? = null,

    @SerializedName("time13On")
    @Column(name = "time13_on")
    var time13On: Int? = null,

    @SerializedName("time13Off")
    @Column(name = "time13_off")
    var time13Off: Int? = null,

    @SerializedName("time14On")
    @Column(name = "time14_on")
    var time14On: Int? = null,

    @SerializedName("time14Off")
    @Column(name = "time14_off")
    var time14Off: Int? = null,

    @SerializedName("time15On")
    @Column(name = "time15_on")
    var time15On: Int? = null,

    @SerializedName("time15Off")
    @Column(name = "time15_off")
    var time15Off: Int? = null,

    @SerializedName("time16On")
    @Column(name = "time16_on")
    var time16On: Int? = null,

    @SerializedName("time16Off")
    @Column(name = "time16_off")
    var time16Off: Int? = null,

    @SerializedName("time17On")
    @Column(name = "time17_on")
    var time17On: Int? = null,

    @SerializedName("time17Off")
    @Column(name = "time17_off")
    var time17Off: Int? = null,

    @SerializedName("time18On")
    @Column(name = "time18_on")
    var time18On: Int? = null,

    @SerializedName("time18Off")
    @Column(name = "time18_off")
    var time18Off: Int? = null,

    @SerializedName("time19On")
    @Column(name = "time19_on")
    var time19On: Int? = null,

    @SerializedName("time19Off")
    @Column(name = "time19_off")
    var time19Off: Int? = null,

    @SerializedName("time20On")
    @Column(name = "time20_on")
    var time20On: Int? = null,

    @SerializedName("time20Off")
    @Column(name = "time20_off")
    var time20Off: Int? = null,

    @SerializedName("time21On")
    @Column(name = "time21_on")
    var time21On: Int? = null,

    @SerializedName("time21Off")
    @Column(name = "time21_off")
    var time21Off: Int? = null,

    @SerializedName("time22On")
    @Column(name = "time22_on")
    var time22On: Int? = null,

    @SerializedName("time22Off")
    @Column(name = "time22_off")
    var time22Off: Int? = null,

    @SerializedName("time23On")
    @Column(name = "time23_on")
    var time23On: Int? = null,

    @SerializedName("time23Off")
    @Column(name = "time23_off")
    var time23Off: Int? = null,

) {
    fun totalOn(): Int {

        if(time00On != null && time01On != null && time02On != null && time03On != null && time04On != null && time05On != null
            && time06On != null && time07On != null && time08On != null && time09On != null && time10On != null && time11On != null
            && time12On != null && time13On != null && time14On != null && time15On != null && time16On != null && time17On != null
            && time18On != null && time19On != null && time20On != null && time21On != null && time22On != null && time23On != null
        ) {
            val total = time00On!! + time01On!! + time02On!! + time03On!! + time04On!! + time05On!!
            + time06On!! + time07On!! + time08On!! + time09On!! + time10On!! + time11On!!
            + time12On!! + time13On!! + time14On!! + time15On!! + time16On!! + time17On!!
            + time18On!! + time19On!! + time20On!! + time21On!! + time22On!! + time23On!!

            return total
        }

        return 0
    }

    fun totalOff(): Int {

        if(time00Off != null && time01Off != null && time02Off != null && time03Off != null && time04Off != null && time05Off != null
            && time06Off != null && time07Off != null && time08Off != null && time09Off != null && time10Off != null && time11Off != null
            && time12Off != null && time13Off != null && time14Off != null && time15Off != null && time16Off != null && time17Off != null
            && time18Off != null && time19Off != null && time20Off != null && time21Off != null && time22Off != null && time23Off != null
        ) {
            val total = time00Off!! + time01Off!! + time02Off!! + time03Off!! + time04Off!! + time05Off!!
            + time06Off!! + time07Off!! + time08Off!! + time09Off!! + time10Off!! + time11Off!!
            + time12Off!! + time13Off!! + time14Off!! + time15Off!! + time16Off!! + time17Off!!
            + time18Off!! + time19Off!! + time20Off!! + time21Off!! + time22Off!! + time23Off!!

            return total
        }

        return 0
    }
}