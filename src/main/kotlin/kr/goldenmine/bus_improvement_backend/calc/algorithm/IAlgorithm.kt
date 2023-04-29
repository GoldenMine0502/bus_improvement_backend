package kr.goldenmine.bus_improvement_backend.calc.algorithm

interface IAlgorithm<T> {
    fun pre()
    fun executeAlgorithm(startIndex: Int, endIndex: Int): T
}