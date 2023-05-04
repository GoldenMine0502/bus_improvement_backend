package kr.goldenmine.bus_improvement_backend.models.gwangjin.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusStationInfo
import kr.goldenmine.bus_improvement_backend.util.nameNoExtension
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusThroughInfoList
import org.springframework.stereotype.Service
import java.io.File

@Service
final class GwangjinBusThroughInfoDatabase : IDatabase {
    val busThroughs = HashMap<String, List<GwangjinBusThroughInfo>>()
    val busStations = HashMap<String, GwangjinBusStationInfo>() // HashMap<arsId, BusStationInfo>
    val busStationToStationNumber = HashMap<String, String>() // HashMap<arsId, BusStationInfo>

    init {
        loadAll()
    }

    override fun loadAll() {
        val gson = Gson()
        val type = object : TypeToken<GwangjinBusThroughInfoList>() {}.type

        File("database/stationbyroute_data").walk().forEach { file ->
            if(file.isFile && file.name.endsWith("json")) {
                val name = file.nameNoExtension

                file.bufferedReader().use { reader ->
                    val busThroughInfoList = gson.fromJson<GwangjinBusThroughInfoList>(reader, type)

                    for(s in busThroughInfoList.result) {
                        busStations[s.arsId] = GwangjinBusStationInfo(s.posX.toDouble(), s.posY.toDouble())
                    }
                    for(s in busThroughInfoList.result) {
                        busStationToStationNumber[s.station] = s.arsId
                    }

                    busThroughs[name] = busThroughInfoList.result
                }
            }
        }
    }
}