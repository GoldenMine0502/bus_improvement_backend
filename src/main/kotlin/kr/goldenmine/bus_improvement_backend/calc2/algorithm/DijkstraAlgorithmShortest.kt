package kr.goldenmine.bus_improvement_backend.calc2.algorithm

import kr.goldenmine.bus_improvement_backend.models.gwangjin.GwangjinBusThroughInfo
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusStopInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusThroughInfoDatabase
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.GwangjinBusTrafficInfoDatabase
import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.distanceTM127

class DijkstraAlgorithmShortest(
    gwangjinBusStopInfoDatabase: GwangjinBusStopInfoDatabase,
    gwangjinBusInfoDatabase: GwangjinBusInfoDatabase,
    gwangjinBusThroughInfoDatabase: GwangjinBusThroughInfoDatabase,
    gwangjinBusTrafficInfoDatabase: GwangjinBusTrafficInfoDatabase,
): IDijkstraAlgorithm(
    gwangjinBusStopInfoDatabase, gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusTrafficInfoDatabase
) {
    override fun getNode(adjointMatrix: Array<IntArray>, busThroughInfos: List<GwangjinBusThroughInfo>, startThroughIndex: Int, finishThroughIndex: Int, startIndex: Int, endIndex: Int): Node {
        val distance = distanceTM127(
            Point(busThroughInfos[startThroughIndex].posX.toDouble(), busThroughInfos[startThroughIndex].posY.toDouble()),
            Point(busThroughInfos[finishThroughIndex].posX.toDouble(), busThroughInfos[finishThroughIndex].posY.toDouble())
        )

        return Node(endIndex, distance)
    }

    override fun getAlgorithmType(): AlgorithmType {
        return AlgorithmType.SHORTEST
    }
}