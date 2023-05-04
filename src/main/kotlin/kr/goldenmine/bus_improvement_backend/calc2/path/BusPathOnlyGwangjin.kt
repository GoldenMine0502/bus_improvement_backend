package kr.goldenmine.bus_improvement_backend.calc2.path

import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusStopInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusThroughInfoDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BusPathOnlyGwangjin(
    val gwangjinBusInfoDatabase: GwangjinBusInfoDatabase,
    val gwangjinBusThroughInfoDatabase: GwangjinBusThroughInfoDatabase,
    val gwangjinBusStopInfoDatabase: GwangjinBusStopInfoDatabase,
    ) : IBusPath {
    private val log: Logger = LoggerFactory.getLogger(BusPathOnlyGwangjin::class.java)

    override fun createAllBusPath(): HashMap<String, Pair<String, String>> {
        val result = HashMap<String, Pair<String, String>>()
//        val map = algorithm.getShortIdToIndexMap()

//        log.info(gwangjinBusInfoDatabase.buses.size.toString())



        val sorted = gwangjinBusInfoDatabase.buses.keys.toList().sortedBy {
            val o1Bus = gwangjinBusInfoDatabase.buses[it]

            o1Bus?.busInterval?.toInt() ?: 60
        }


        sorted.forEach { t -> // 광진구 필터링
            val throughs = gwangjinBusThroughInfoDatabase.busThroughs[t]

//            log.info("$t: ${throughs != null}")

            if(throughs != null) {
                var startStation = 0
                var finishStation = throughs.size - 1
//                while((startStation < throughs.size - 1) && !gwangjinBusStopInfoDatabase.isInGwangjin(throughs[startStation].arsId)) {
//                    startStation++
//                }
//                while(finishStation >= 1 && !gwangjinBusStopInfoDatabase.isInGwangjin(throughs[finishStation].arsId)) {
//                    if(startStation == finishStation) break
//                    finishStation--
//                }

                val pair = Pair(throughs[startStation].arsId, throughs[finishStation].arsId)
                result[t] = pair
            }
        }

        return result
    }
}