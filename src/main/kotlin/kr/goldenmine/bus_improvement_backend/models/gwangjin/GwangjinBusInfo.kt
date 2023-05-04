package kr.goldenmine.bus_improvement_backend.models.gwangjin

import com.google.gson.annotations.SerializedName
import lombok.Data
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/*
			"busRouteAbrv": "110A",
			"busRouteId": "100100016",
			"busRouteNm": "110A고려대",
			"corpNm": "대진여객  02-916-3312",
			"edStationNm": "정릉",
			"firstBusTm": "20230503040000",
			"firstLowTm": "              ",
			"lastBusTm": "20230503224000",
			"lastBusYn": " ",
			"lastLowTm": "20150717224000",
			"length": "36.9",
			"routeType": "3",
			"stStationNm": "정릉",
			"term": "8"
 */
@Data
@Entity
@Table(name = "bus_info_gwangjin")
class GwangjinBusInfo {
    @Id
    @SerializedName("busRouteId")
    val id: String? = null

    @SerializedName("busRouteAbrv")
    var abbreviation: String? = null

    @SerializedName("busRouteNm")
    val name: String? = null

    @SerializedName("corpNm")
    val companyName: String? = null


    @SerializedName("firstBusTm")
    val firstBusTime: String? = null

    @SerializedName("edStationNm")
    val endStationName: String? = null

    @SerializedName("firstLowTm")
    val firstLowFloorBusTime: String? = null

    @SerializedName("lastBusTm")
    val lastBusTime: String? = null

    @SerializedName("lastBusYn")
    val isLastBusLowFloor: String? = null

    @SerializedName("lastLowTm")
    val lastLowFloorBusTime: String? = null

    @SerializedName("length")
    val length: String? = null

    @SerializedName("routeType")
    val routeType: String? = null

    @SerializedName("stStationNm")
    val startStationName: String? = null

    @SerializedName("term")
    val busInterval: String? = null
}