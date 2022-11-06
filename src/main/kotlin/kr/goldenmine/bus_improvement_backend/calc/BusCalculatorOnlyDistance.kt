package kr.goldenmine.bus_improvement_backend.calc

import kr.goldenmine.bus_improvement_backend.models.station.BusStopStationInfo
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.convertTM127toWGS84
import kr.goldenmine.bus_improvement_backend.util.distance

const val INF = 100000000.0

class BusCalculatorOnlyDistance(
    val stations: List<BusStopStationInfo>
) {
    val array: Array<Array<Double>>
    val previous: Array<Array<Point>>

    init {
        val size = stations.size
        array = Array(size + 1) { Array(size + 1) { INF } }
        previous = Array(size + 1) { y -> Array(size + 1) { x -> Point(x.toDouble(), y.toDouble()) } }

        val map = HashMap<BusStopStationInfo, Point>()

        for (i in stations.indices) {
            val first = stations[i]

            if (first.posX != null && first.posY != null) {
                if (!map.containsKey(first)) map[first] = convertTM127toWGS84(Point(first.posX, first.posY))
                val firstPos = map[first]

                for (j in stations.indices) {
                    val second = stations[i]

                    if (second.posX != null && second.posY != null) {
                        if (!map.containsKey(second)) map[second] = convertTM127toWGS84(Point(second.posX, second.posY))
                        val secondPos = map[second]

                        if(firstPos != null && secondPos != null) {
                            array[i][j] = distance(
                                firstPos.x, secondPos.x,
                                firstPos.y, secondPos.y,
                                0.0, 0.0)
                        }
                    }
                }
            }
        }
    }
}