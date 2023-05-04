package kr.goldenmine.bus_improvement_backend.models.gwangjin

import com.google.gson.annotations.SerializedName

class GwangjinBusInfoList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("result")
    val result: List<GwangjinBusInfo>
) {

}