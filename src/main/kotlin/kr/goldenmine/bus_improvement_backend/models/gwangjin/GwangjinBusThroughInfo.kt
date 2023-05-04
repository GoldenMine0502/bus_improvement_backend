package kr.goldenmine.bus_improvement_backend.models.gwangjin

import com.google.gson.annotations.SerializedName

/*
			"arsId": "09102",
			"beginTm": "04:02",
			"busRouteAbrv": "101",
			"busRouteId": "100100006",
			"busRouteNm": "101",
			"direction": "서소문",
			"gpsX": "127.0128184738",
			"gpsY": "37.6624998842",
			"lastTm": "23:02",
			"posX": "201130.87303927453",
			"posY": "462543.8449762054",
			"routeType": "3",
			"sectSpd": "0",
			"section": "0",
			"seq": "1",
			"station": "108000014",
			"stationNm": "우이동도선사입구.북한산우이역",
			"stationNo": "09102",
			"transYn": "N",
			"fullSectDist": "0",
			"trnstnid": "101000036"
 */
data class GwangjinBusThroughInfo(
    @SerializedName("arsId")
    val arsId: String,
    @SerializedName("beginTm")
    val beginTime: String,
    @SerializedName("busRouteAbrv")
    val busRouteAbrv: String,
    @SerializedName("busRouteId")
    val busRouteId: String,
    @SerializedName("busRouteNm")
    val busRouteNm: String,
    @SerializedName("direction")
    val direction: String,
    @SerializedName("gpsX")
    val gpsX: String,
    @SerializedName("gpsY")
    val gpsY: String,
    @SerializedName("lastTm")
    val lastTime: String,
    @SerializedName("posX")
    val posX: String,
    @SerializedName("posY")
    val posY: String,
    @SerializedName("routeType")
    val routeType: String,
    @SerializedName("sectSpd")
    val sectSpd: String,
    @SerializedName("section")
    val section: String,
    @SerializedName("seq")
    val seq: String,
    @SerializedName("station")
    val station: String,
    @SerializedName("stationNm")
    val stationNm: String,
    @SerializedName("stationNo")
    val stationNo: String,
    @SerializedName("transYn")
    val transYn: String,
    @SerializedName("fullSectDist")
    val fullSectDist: String,
    @SerializedName("trnstnid")
    val trnstnid: String
)