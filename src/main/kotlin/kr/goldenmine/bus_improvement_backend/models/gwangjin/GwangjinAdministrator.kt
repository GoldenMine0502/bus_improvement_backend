package kr.goldenmine.bus_improvement_backend.models.gwangjin

import kr.goldenmine.bus_improvement_backend.calc2.algorithm.*
import kr.goldenmine.bus_improvement_backend.models.gwangjin.database.*
import org.springframework.stereotype.Service

@Service
final class GwangjinAdministrator {
    val gwangjinBusInfoDatabase = GwangjinBusInfoDatabase()
    val gwangjinBusThroughInfoDatabase = GwangjinBusThroughInfoDatabase()
    val gwangjinBusStopInfoDatabase = GwangjinBusStopInfoDatabase(gwangjinBusThroughInfoDatabase)
    val gwangjinBusTrafficInfoDatabase = GwangjinBusTrafficInfoDatabase(gwangjinBusThroughInfoDatabase)

    val results = HashMap<AlgorithmType, IDijkstraAlgorithm.AlgorithmResult>()
    val models = HashMap<AlgorithmType, IDijkstraAlgorithm>()

    init {
        calculateAllAlgorithm()
    }

    fun calculateAllAlgorithm() {
        val algorithms = listOf(
            DijkstraAlgorithmShortest(gwangjinBusStopInfoDatabase, gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusTrafficInfoDatabase),
//            DijkstraAlgorithmDijkstra(gwangjinBusStopInfoDatabase, gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusTrafficInfoDatabase),
//            DijkstraAlgorithmGreedy(gwangjinBusStopInfoDatabase, gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusTrafficInfoDatabase),
//            DijkstraAlgorithmGreedy(gwangjinBusStopInfoDatabase, gwangjinBusInfoDatabase, gwangjinBusThroughInfoDatabase, gwangjinBusTrafficInfoDatabase, 5),
        )

        algorithms.forEach {
            val res = it.executeAllAlgorithm()
            results[it.getAlgorithmType()] = res
            models[it.getAlgorithmType()] = it
        }

//        val threads = algorithms.map {
//            Thread {
//                println("executing ${it.getAlgorithmType()}")
//                val res = it.executeAllAlgorithm()
//
//                synchronized(this) {
//                    results[it.getAlgorithmType()] = res
//                    models[it.getAlgorithmType()] = it
//                }
//                println("completed ${it.getAlgorithmType()}")
//            }
//        }

//        threads.forEach { it.start() }
//        threads.forEach { it.join() }
    }
}