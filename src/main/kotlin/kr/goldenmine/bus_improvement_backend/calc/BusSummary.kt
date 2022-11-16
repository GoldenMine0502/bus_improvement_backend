package kr.goldenmine.bus_improvement_backend.calc

interface BusSummary {
    fun getSummary(): HashMap<String, List<Int>>
}