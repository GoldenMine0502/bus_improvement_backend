package kr.goldenmine.bus_improvement_backend.models.gwangjin.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kr.goldenmine.bus_improvement_backend.calc.BusCalculatorDijkstraNodeTotal
import kr.goldenmine.bus_improvement_backend.util.nameNoExtension
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusInfo
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusInfoList
import kr.goldenmine.bus_improvement_backend.util.nameExtension
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
final class GwangjinBusInfoDatabase : IDatabase {
    private val log: Logger = LoggerFactory.getLogger(GwangjinBusInfoDatabase::class.java)
    val buses = HashMap<String, GwangjinBusInfo>()


    init {
        loadAll()
    }

    override fun loadAll() {
        val gson = Gson()
        val type = object : TypeToken<GwangjinBusInfoList>() {}.type

        File("database/routeinfoitem_data").walk().forEach { file->
            if(file.isFile && file.name.endsWith("json")) {
                val name = file.nameNoExtension

                file.bufferedReader().use { reader ->
                    val busInfoList = gson.fromJson<GwangjinBusInfoList>(reader, type)
                    if(busInfoList.count > 1) { // 카운트는 항상 1이라 조건문 통과하진 않음
                        println(busInfoList)
                    }

                    val busInfo = busInfoList.result[0]
                    buses[name] = busInfo
                }
            }
        }
        log.info("total buses: ${buses.size}")
    }
}

//fun main() {
//    GwangjinBusInfoDatabase()
//}