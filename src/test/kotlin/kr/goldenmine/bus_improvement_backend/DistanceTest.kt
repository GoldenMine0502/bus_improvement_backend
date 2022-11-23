package kr.goldenmine.bus_improvement_backend

import kr.goldenmine.bus_improvement_backend.util.Point
import kr.goldenmine.bus_improvement_backend.util.convertTM127toWGS84
import kr.goldenmine.bus_improvement_backend.util.distance
import kr.goldenmine.bus_improvement_backend.util.distanceTM127
import org.junit.jupiter.api.Test


class DistanceTest {
    @Test
    fun test() {
        val startTM127 = Point(170286.321777, 429478.523482)
        val finishTM127 = Point(169629.464686, 429367.133213)

        val startWgs84 = convertTM127toWGS84(startTM127)
        val finishWgs84 = convertTM127toWGS84(finishTM127)

        println(startWgs84)
        println(finishWgs84)

        val distance = distanceTM127(startTM127, finishTM127)

        // 위도: -90~90 latitude y
        // 경도: -180~180 longitude x

        val distance2 = distance(
            finishWgs84.y, startWgs84.y,
            finishWgs84.x, startWgs84.x,
            0.0, 0.0
        )

        println("${distance}m")
        println("${distance2}m")
    }
}