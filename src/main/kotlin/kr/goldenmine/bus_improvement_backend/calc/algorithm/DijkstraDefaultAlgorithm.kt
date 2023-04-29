package kr.goldenmine.bus_improvement_backend.calc.algorithm

import kr.goldenmine.bus_improvement_backend.calc.data.DijkstraData
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127

class DijkstraDefaultAlgorithm(
    override val dijkstraData: DijkstraData
): DijkstraAlgorithm(dijkstraData) {
    override fun getCost(startIndex: Int, finishIndex: Int): Double? {
        val stationStart = dijkstraData.stations[startIndex]
        val stationFinish = dijkstraData.stations[finishIndex]

        if (stationStart.posX != null && stationStart.posY != null &&
            stationFinish.posX != null && stationFinish.posY != null) {
            val distance = distanceTM127(
                Point(stationStart.posX, stationStart.posY),
                Point(stationFinish.posX, stationFinish.posY)
            )

            return distance
        }

        return null
    }
}