package kr.goldenmine.bus_improvement_backend.calc2.path

interface IBusPath {
    fun createAllBusPath(): HashMap<String, Pair<String, String>>
}