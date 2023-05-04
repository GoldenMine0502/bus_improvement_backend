package kr.goldenmine.bus_improvement_backend.models.gwangjin.database

import com.opencsv.CSVReader
import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusTrafficInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
final class GwangjinBusTrafficInfoDatabase(
    val gwangjinBusThroughInfoDatabase: GwangjinBusThroughInfoDatabase,
) : IDatabase {
    private val log: Logger = LoggerFactory.getLogger(GwangjinBusTrafficInfoDatabase::class.java)

    val traffics = HashMap<String, List<GwangjinBusTrafficInfo>>()

    init {
        loadAll()
    }

    override fun loadAll() {
        File("database/bus_card_usage").walk().forEach { file->
            if(file.isFile && file.name.endsWith("json")) {

                CSVReader(file.bufferedReader()).use { reader ->
                    reader.skip(1)

                    val list = ArrayList<GwangjinBusTrafficInfo>()

                    fun addToMap() {
                        if(list.isNotEmpty()) {
                            val routeNo = list[0].routeNo
                            if (routeNo != null) {
                                val throughs = gwangjinBusThroughInfoDatabase.busThroughs[routeNo]

                                // shortId 부여. 구현의 편리함을 위해 조치
                                if(throughs != null) {
                                    for (i in throughs.indices) {
                                        list[i].shortId = throughs[i].arsId
                                    }
                                }

                                traffics[routeNo] = ArrayList(list)

                            } else {
                                log.warn("routeNo is null: ${list[0]} ${list.size} ${file.name}")
                            }
                        }
                    }

                    reader.readAll().forEach { line ->
                        val busTrafficInfo = makeBusTrafficInfo(line)
                        if(list.isEmpty()) {
                            list.add(busTrafficInfo)
                        } else {
                            if(list[0].routeNo != busTrafficInfo.routeNo) {
                                addToMap()
                                list.clear()
                            }
                            list.add(busTrafficInfo)
                        }
                    }
                    addToMap()
                }
            }
        }
    }

    // 노선,                      기종점,정류장순번,    정류장명,04시,05시,06시,07시,08시,09시,10시,11시,12시,13시,14시,15시,16시,17시,18시,19시,20시,21시,22시,23시,00시,01시,02시,03시,
    //  0,                          1, 2,              3,  4,5,6,7, 8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27
    // 58,�۵���2������ - �۵���2������,21,�۵�ǳ�����̿�2����,  0,3,3,8,11,4, 4,14, 7, 7, 9,11,15,18,20,14,15,15,22,10, 0, 0, 0, 0,
    private fun makeBusTrafficInfo(line: Array<String>): GwangjinBusTrafficInfo {
//        val split = fileName.split("-")

        return GwangjinBusTrafficInfo(
//            id = id,

//            year = split[0].toInt(),
//            month = split[1].toInt(),
//            date = split[2].toInt(),

            routeNo = line[0],
            sequence = line[2].toInt(),

            time04 = line[4].toInt(),
            time05 = line[5].toInt(),
            time06 = line[6].toInt(),
            time07 = line[7].toInt(),
            time08 = line[8].toInt(),
            time09 = line[9].toInt(),

            time10 = line[10].toInt(),
            time11 = line[11].toInt(),
            time12 = line[12].toInt(),
            time13 = line[13].toInt(),
            time14 = line[14].toInt(),
            time15 = line[15].toInt(),

            time16 = line[16].toInt(),
            time17 = line[17].toInt(),
            time18 = line[18].toInt(),
            time19 = line[19].toInt(),
            time20 = line[20].toInt(),
            time21 = line[21].toInt(),

            time22 = line[22].toInt(),
            time23 = line[23].toInt(),
            time00 = line[24].toInt(),
            time01 = line[25].toInt(),
            time02 = line[26].toInt(),
            time03 = line[27].toInt(),
        )
    }
}

//fun main() {
//    val database = GwangjinBusTrafficInfoDatabase()
//    database.traffics["01"]?.forEach {
//        println(it)
//    }
//}