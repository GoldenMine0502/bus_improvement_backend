package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationSerivce
import kr.goldenmine.bus_improvement_backend.models.through.BusThroughInfoSerivce
import kr.goldenmine.bus_improvement_backend.models.traffic.BusTrafficSerivce
import kr.goldenmine.bus_improvement_backend.util.distance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class BusCalculator(
    private val busStopStationService: BusStopStationSerivce,
    private val busThroughInfoSerivce: BusThroughInfoSerivce,
    private val busTrafficSerivce: BusTrafficSerivce
) {
    private val log: Logger = LoggerFactory.getLogger(BusCalculator::class.java)

    protected val stations = busStopStationService.list()
    protected val throughs = busThroughInfoSerivce.list()
    protected val traffics = busTrafficSerivce.list()

    protected val stationsMap = HashMap<Int, Int>()
    protected val trafficMap = HashMap<Int, Int>()
    protected val stationsShortIdMap = HashMap<Int, Int>()

    fun calculate() {
        // 버스정류장 인덱스를 얻기 위한 맵 id to index
        for(stationIndex in stations.indices) {
            val station = stations[stationIndex]

            if(station.id != null)
                stationsMap[station.id] = stationIndex
        }

        for(station in stations) {
            if(station.shortId != null && station.id != null)
                stationsShortIdMap[station.shortId] = station.id
        }

        // 트래픽 정보를 얻기 위한 map
        for(traffic in traffics) {
            if(traffic.routeNo != null) {
                val id = stationsShortIdMap[traffic.shortId]!!
                val previous = trafficMap[id] ?: 0
                trafficMap[id] = previous + traffic.totalOn() + traffic.totalOff()
            }
        }

//        trafficMap.forEach { (t, u) ->
//            log.info("key: $t, value: $u")
//        }

        // 노드 생성
        val nodes = ArrayList<ArrayList<Node>>()
        repeat(stations.size + 1) {
            nodes.add(ArrayList())
        }

        for(i in 0 until throughs.size - 1) {
            val startThrough = throughs[i]
            val finishThrough = throughs[i + 1]
            val start = stationsMap[throughs[i].busStopStationId]
            val finish = stationsMap[throughs[i + 1].busStopStationId]


            if(start != null && finish != null) {
                val stationStart = stations[start]
                val stationFinish = stations[finish]

                if(stationStart.posX != null && stationStart.posY != null
                    && stationFinish.posX != null && stationFinish.posY != null) {
                    val distance = distance(
                        stationStart.posX, stationFinish.posX,
                        stationStart.posY, stationFinish.posY,
                        0.0, 0.0
                    )

                    nodes[start].add(Node(finish, distance))
                }
            }
        }

        // 다익스트라 알고리즘 실행
        val dist = DoubleArray(stations.size )
        val previous = IntArray(stations.size)

        for (i in dist.indices) {
            dist[i] = Double.MAX_VALUE
            previous[i] = i
        }

        val start = 0

        val queue = PriorityQueue<Node>(Comparator.comparingDouble { o -> o.cost })
        queue.offer(Node(start, 0.0))
        dist[start] = 0.0

        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (dist[current.index] < current.cost)
                continue

            val nexts = nodes[current.index]

            for (i in nexts.indices) {
                val next: Node = nexts[i]
                val nextCost = current.cost + next.cost
                if (dist[next.index] > nextCost) {
                    dist[next.index] = nextCost
                    previous[next.index] = current.index
                    queue.add(Node(next.index, dist[next.index]))
                }
            }
        }
    }

    // 다음 노드의 인덱스와, 그 노드로 가는데 필요한 비용을 담고 있다.
    inner class Node(
        val index: Int, val cost: Double
    ) {
        override fun toString(): String {
            return "Node{" +
                    "idx=" + index +
                    ", cost=" + cost +
                    '}'
        }
    }
}