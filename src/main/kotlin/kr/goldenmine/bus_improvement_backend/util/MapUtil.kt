package kr.goldenmine.bus_improvement_backend.util

import com.jhlabs.map.proj.Projection
import com.jhlabs.map.proj.ProjectionFactory
import java.awt.geom.Point2D
import kotlin.math.*

/**
 * Calculate distance between two points in latitude and longitude taking
 * into account height difference. If you are not interested in height
 * difference pass 0.0. Uses Haversine method as its base.
 *
 * lat1, lon1 Start point
 * lat2, lon2 End point
 * el1 Start altitude in meters
 * el2 End altitude in meters
 * @returns Distance in Meters
 */
fun distance(
    latitude1: Double, latitude2: Double,
    longitude1: Double, longitude2: Double,
    el1: Double = 0.0, el2: Double = 0.0
): Double {
    val R = 6371 // Radius of the earth
    val latDistance = Math.toRadians(latitude2 - latitude1)
    val lonDistance = Math.toRadians(longitude2 - longitude1)
    val a = (sin(latDistance / 2) * sin(latDistance / 2)
            + (cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2))
            * sin(lonDistance / 2) * sin(lonDistance / 2)))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    var distance = R * c * 1000 // convert to meters
    val height = el1 - el2
    distance = distance.pow(2.0) + height.pow(2.0)
    return sqrt(distance)
}

fun distanceTM127(startTM127: Point, finishTM127: Point, el: Point = Point(0.0, 0.0)): Double {
    val start = convertTM127toWGS84(startTM127)
    val finish = convertTM127toWGS84(finishTM127)

    return distance(
        start.y, finish.y,
        start.x, finish.x,
        0.0, 0.0
    )
}

val TM127 = arrayOf(
    "+proj=tmerc",
    "+lat_0=38",
    "+lon_0=127.0028902777777777776",
    "+k=1",
    "+x_0=200000",
    "+y_0=500000",
    "+ellps=bessel",
    "towgs84=-146.43,507.89,681.46"
)

val EPSG4326 = arrayOf(
    "+proj=longlat",
    "+ellps=WGS84",
    "+datum=WGS84",
    "+no_defs"
)


fun convertWGS84toTM127(point: Point): Point {
    val proj = ProjectionFactory.fromPROJ4Specification(TM127)

    val srcProjec = Point2D.Double(point.x, point.y);
    val dstProject = proj.transform(srcProjec, Point2D.Double())

    return Point(dstProject.x, dstProject.y)
}

fun convertTM127toWGS84(point: Point): Point {
    /*
            proj4.defs('TM127', "+proj=tmerc +lat_0=38 +lon_0=127.0028902777777777776 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +towgs84=-146.43,507.89,681.46")
        proj4.defs('TM128', "+proj=tmerc +lat_0=38 +lon_0=128E +k=0.9999 +x_0=400000 +y_0=600000 +ellps=bessel +towgs84=-146.43,507.89,681.46")
        proj4.defs('GRS80', "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs")
        proj4.defs('EPSG:2097', "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43");
        proj4.defs('EPSG:4326', "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs");
     */

    val proj = ProjectionFactory.fromPROJ4Specification(TM127)

    val srcProjec = Point2D.Double(point.x, point.y);
    val dstProject = proj.inverseTransform(srcProjec, Point2D.Double())

    return Point(dstProject.x, dstProject.y)
}