package kr.goldenmine.bus_improvement_backend.models.gwangjin

import com.google.gson.annotations.SerializedName
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusThroughInfo

class GwangjinBusThroughInfoList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("result")
    val result: List<GwangjinBusThroughInfo>
)