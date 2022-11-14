package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.bus.BusInfo
import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.bus.BusInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.springframework.stereotype.Service
import java.lang.Double.max
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Service
class BusCalculatorRouteCurve(
    private val busInfoService: BusInfoSerivce,
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoService: BusThroughInfoSerivce,
    private val busTrafficService: BusTrafficSerivce,
): BusCalculator(busStopStationService, busThroughInfoService, busTrafficService) {

    protected val endPoints = HashMap<String, Int>()
    protected val busList = busInfoService.list()
    protected val routeIdToBusInfo = HashMap<String, BusInfo>()

    override fun calculate() {
        super.calculate()

        busList.forEach {
            routeIdToBusInfo[it.routeId!!] = it
        }

        // 데이터 정리
        val busStopStationsMap = HashMap<Int, Int>() // station id to station index
        val busThroughMap = HashMap<String, ArrayList<BusThroughInfo>>() // route id to list of bus through info

        for (through in throughs) {
            if (through.routeId != null && through.busStopStationId != null) {
                val station = stations[through.busStopStationId]

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
        for(busInfo in busList) {
            val startBusStation = stations[busStopStationsMap[busInfo.originBusStopId!!]!!]
            val endBusStation = stations[busStopStationsMap[busInfo.destBusStopId!!]!!]
            val turnBusStation = stations[busStopStationsMap[busInfo.turnBusStopId!!]!!]

            val maxDistanceBusThrough = busThroughMap[busInfo.routeId]!!.maxByOrNull {
                val currentBusStation = stations[busStopStationsMap[it.busStopStationId!!]!!]

                distanceTM127(
                    Point(startBusStation.posX!!, startBusStation.posY!!),
                    Point(currentBusStation.posX!!, currentBusStation.posY!!)
                )
            }!!
            val maxDistanceBusStation = if(maxDistanceBusThrough.busStopStationId != null) stations[busStopStationsMap[maxDistanceBusThrough.busStopStationId]!!] else null

            val turnDistance = distanceTM127(
                Point(startBusStation.posX!!, startBusStation.posY!!),
                Point(turnBusStation.posX!!, turnBusStation.posY!!)
            )

            val endDistance = distanceTM127(
                Point(startBusStation.posX, startBusStation.posY),
                Point(endBusStation.posX!!, endBusStation.posY!!)
            )

            val maxThroughDistance = if(maxDistanceBusStation != null) distanceTM127(
                Point(startBusStation.posX, startBusStation.posY),
                Point(maxDistanceBusStation.posX!!, maxDistanceBusStation.posY!!)
            ) else 0.0

            val endPoint = if(max(turnDistance, endDistance) * 2 < maxThroughDistance) {
                maxDistanceBusThrough.busStopStationId
            } else if(turnDistance <= endDistance) {
                busInfo.destBusStopId
            } else {
                busInfo.turnBusStopId
            }

            if(busInfo.routeId != null && endPoint != null) {
                endPoints[busInfo.routeId] = endPoint
            }
        }
    }
}