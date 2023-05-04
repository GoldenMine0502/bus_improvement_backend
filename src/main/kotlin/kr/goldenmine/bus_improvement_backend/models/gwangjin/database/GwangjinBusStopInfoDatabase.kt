package kr.goldenmine.bus_improvement_backend.models.gwangjin.database

import com.opencsv.CSVReader
import org.springframework.stereotype.Service
import java.io.File

@Service
class GwangjinBusStopInfoDatabase(private val busThroughInfoDatabase: GwangjinBusThroughInfoDatabase) : IDatabase {
    val gwangjinIds = HashSet<String>()
    override fun loadAll() {
        // TODO 엑셀 읽기
        CSVReader(File("database/gwangjin_bus.csv.csv").bufferedReader()).use { reader ->
            reader.skip(1)

            gwangjinIds.addAll(reader.readAll().map { busThroughInfoDatabase.busStationToStationNumber[it[5]] }.filterNotNull())
        }
    }

    fun isInGwangjin(arsId: String): Boolean {
        return gwangjinIds.contains(arsId)
//        return true
    }
}