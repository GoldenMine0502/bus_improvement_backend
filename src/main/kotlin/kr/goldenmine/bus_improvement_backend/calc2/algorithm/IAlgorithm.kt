package kr.goldenmine.bus_improvement_backend.calc2.algorithm

interface IAlgorithm {
    fun createAdjointMatrix(): Array<IntArray>
    fun executeAlgorithm(adjointMatrix: Array<IntArray>, startIndex: Int, endIndex: Int): List<Int>
    fun getAllRoutes(): HashMap<String, List<Int>> // HashMap<노선이름, List<정류장인덱스>>
    fun getIndexToShortIdMap(): HashMap<Int, String> // HashMap<정류장인덱스, 정류장arsId>
    fun getShortIdToIndexMap(): HashMap<String,Int> // HashMap<정류장인덱스, 정류장arsId>

    fun getAlgorithmType(): AlgorithmType
}