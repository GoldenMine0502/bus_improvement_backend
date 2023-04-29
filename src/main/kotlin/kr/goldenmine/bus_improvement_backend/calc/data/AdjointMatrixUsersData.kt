package kr.goldenmine.bus_improvement_backend.calc.data

import kr.goldenmine.bus_improvement_backend.calc.BusCalculator
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.node.BusTrafficNodeInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AdjointMatrixUsersData(
    private val busInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficNodeInfoSerivce: BusTrafficNodeInfoSerivce,
): DatabaseIndexData(busInfoService) {
    private val log: Logger = LoggerFactory.getLogger(AdjointMatrixUsersData::class.java)

    val stations = busStopStationService.list()
    val busTimes = HashMap<String, Int>()

    val adjointMatrixUsers: Array<IntArray>

    init {
        val stationsSize = stations.size

        adjointMatrixUsers = Array(stationsSize) { IntArray(stationsSize) }

        val busTrafficNodeInfoList = busTrafficNodeInfoSerivce.list()

        val lastSequences = mutableListOf<Int>()

        fun sequenceList(routeId: String): MutableList<Int> {
            return busThroughInfoService.getThroughSequencesFromRouteId(routeId)
                .asSequence()
                .map {
                    BusCalculator.stationsIdToIndexMap[it.busStopStationId]
                }
                .filterNotNull()
                .toMutableList()
        }

        try {
            log.info("routeNo: ${busTrafficNodeInfoList[0].routeNo} ${routeNoToBusInfo[busTrafficNodeInfoList[0].routeNo!!]?.routeId}")
            lastSequences.addAll(sequenceList(routeNoToBusInfo[busTrafficNodeInfoList[0].routeNo!!]?.routeId!!))
        } catch(ex: Exception) {
            log.info("first route is null")
        }
        for (index in 0 until busTrafficNodeInfoList.size - 1) {
            val start = busTrafficNodeInfoList[index]
            val finish = busTrafficNodeInfoList[index + 1]


            if (start.routeNo == finish.routeNo && finish.sequence != 0) {
                // 일부 인천-부천 노선이나 광역버스들
                if(lastSequences.size == 0) continue

                val total = start.getTotal()

//                val routeId = routeNoToBusInfo[start.routeNo!!]?.routeId!!

                if(start.sequence!! >= lastSequences.size - 1) continue

                val startIndex = lastSequences[start.sequence]
                val finishIndex = lastSequences[start.sequence + 1]

                adjointMatrixUsers[startIndex][finishIndex] += total
            } else {
                lastSequences.clear()
//                log.info("routeNo: ${finish.routeNo} ${routeNoToBusInfo[finish.routeNo!!]?.routeId} ${start.id}")
                val routeId = routeNoToBusInfo[finish.routeNo!!]?.routeId
                if(routeId != null) {
                    lastSequences.addAll(sequenceList(routeNoToBusInfo[finish.routeNo]?.routeId!!))
                }
            }
        }
    }
}