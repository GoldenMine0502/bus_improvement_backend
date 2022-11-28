package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfo
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class BusCalculatorRouteCurve(
    private val busInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce,
) : BusCalculator(busStopStationService, busThroughInfoService, busTrafficService) {

    private val log: Logger = LoggerFactory.getLogger(BusCalculatorRouteCurve::class.java)

    val turnPoints = HashMap<String, Int>()
    val endPoints = HashMap<String, Int>()
    val busList = busInfoService.list()
    val routeIdToBusInfo = HashMap<String, BusInfo>()
    val busThroughMap = HashMap<String, ArrayList<BusThroughInfo>>() // route id to list of bus through info

    override fun calculate() {
        super.calculate()

        busList.forEach {
            routeIdToBusInfo[it.routeId!!] = it
        }

        // 데이터 정리
//        val busStopStationsMap = HashMap<Int, Int>() // station id to station index

        for (through in throughs) {
            if (through.routeId != null && through.busStopStationId != null) {
                val station = stations[stationsIdToIndexMap[through.busStopStationId]!!]

                if (station.id != null) {
                    val list = busThroughMap[through.routeId]
                    if (list != null) {
                        list.add(through)
                    } else {
                        busThroughMap[through.routeId] = ArrayList(listOf(through))
                    }
                }
            }
        }

        // 각 노선별 최단거리와 경로별 이동시 거리 계산
        // 턴하는거, 가장 먼 정류장, 기점 중 택1

//        for (busInfo in busList) {
//            val busStopOriginId = stationsIdToIndexMap[busInfo.originBusStopId]!!
//            val busStopTurnId = stationsIdToIndexMap[busInfo.turnBusStopId]!!
//            val busStopDestId = stationsIdToIndexMap[busInfo.destBusStopId]!!
//            val turnBusStation = stations[busStopTurnId]
//
//            if(busInfo.routeId != null) {
//                turnPoints[busInfo.routeId] = busInfo.turnBusStopId!!
//                endPoints[busInfo.routeId] = busThroughMap[busInfo.routeId]?.last()?.busStopStationId!!
//            }
//        }

        for(busInfo in busList) {
            val busStopOriginIndex = stationsIdToIndexMap[busInfo.originBusStopId]!!
            val busStopTurnIndex = stationsIdToIndexMap[busInfo.turnBusStopId]!!
            val busStopDestinationIndex =
                if(busInfo.originBusStopId != busInfo.destBusStopId)
                    stationsIdToIndexMap[busInfo.destBusStopId]!!
                else
                    stationsIdToIndexMap[busThroughMap[busInfo.routeId]?.last()?.busStopStationId!!]!!

            val startBusStation = stations[busStopOriginIndex]
//            val endBusStation = stations[busStopDestinationIndex]
            val turnBusStation = stations[busStopTurnIndex]

            val maxDistanceBusThrough = busThroughMap[busInfo.routeId]!!.maxByOrNull {
                val currentBusStation = stations[stationsIdToIndexMap[it.busStopStationId!!]!!]

                distanceTM127(
                    Point(startBusStation.posX!!, startBusStation.posY!!),
                    Point(currentBusStation.posX!!, currentBusStation.posY!!)
                )
            }!!
            val maxDistanceBusStation =
                if(maxDistanceBusThrough.busStopStationId != null)
                    stations[stationsIdToIndexMap[maxDistanceBusThrough.busStopStationId]!!]
                else
                    null

            val turnDistance = distanceTM127(
                Point(startBusStation.posX!!, startBusStation.posY!!),
                Point(turnBusStation.posX!!, turnBusStation.posY!!)
            )

//            val endDistance = distanceTM127(
//                Point(startBusStation.posX, startBusStation.posY),
//                Point(endBusStation.posX!!, endBusStation.posY!!)
//            )

            val maxThroughDistance = if(maxDistanceBusStation != null) distanceTM127(
                Point(startBusStation.posX, startBusStation.posY),
                Point(maxDistanceBusStation.posX!!, maxDistanceBusStation.posY!!)
            ) else 0.0

            val turnPoint = if(turnDistance * 2 < maxThroughDistance) {
                maxDistanceBusThrough.busStopStationId
            } else {
                busInfo.turnBusStopId
            }

            val endPoint =
//                if(busInfo.originBusStopId != busInfo.destBusStopId)
//                    busInfo.destBusStopId
//                else
                    busThroughMap[busInfo.routeId]?.last()?.busStopStationId!!


            if(busInfo.routeId != null && turnPoint != null && endPoint != null) {
                turnPoints[busInfo.routeId] = turnPoint
                endPoints[busInfo.routeId] = endPoint
            }
        }


        // 기점-종점간 잇기. 왜냐하면 이렇게 하지 않을 때 연결이 안되는 경우가 있음.
        busThroughMap.forEach { (t, u) ->
            val first = u.first()
            val last = u.last()

            if(first.busStopStationId != last.busStopStationId) {
                u.add(first)
                log.info("connected $t")
            }
        }

    }
}