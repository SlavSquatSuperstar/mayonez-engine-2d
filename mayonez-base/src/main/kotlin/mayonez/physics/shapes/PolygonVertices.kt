package mayonez.physics.shapes

import mayonez.math.FloatMath
import mayonez.math.Vec2
import java.util.*
import kotlin.math.abs

/**
 * Contains methods for generating and sorting polygon vertices.
 *
 * @author SlavSquatSuperstar
 */
object PolygonVertices {

    /**
     * Orders a list of vertices in the counterclockwise direction. The output
     * will always have the same size as the input.
     *
     * Source: https://www.baeldung.com/cs/sort-points-clockwisec
     *
     * @param vertices an array of vertices
     * @return the ordered array of vertices
     */
    @JvmStatic
    fun orderedVertices(vertices: Array<Vec2>): Array<Vec2> {
        if (vertices.size < 3) return vertices.copyOf()

        // Find minimum point
        val start = vertices[getMinCoordsPointIdx(vertices)]

        // Find center point
        var center = Vec2()
        for (v in vertices) center += v
        center /= vertices.size.toFloat()

        val sorted = vertices.copyOf()
        Arrays.sort(sorted) { v1, v2 -> v1.compareOrder(v2, center, start) }
        return sorted
    }

    /**
     * Creates a convex hull ordered counterclockwise from an array of
     * vertices. The convex hull is the smallest polygon that contains all the
     * vertices.
     */
    @JvmStatic
    fun orderedConvexHull(vertices: Array<Vec2>): Array<Vec2> {
        val size = vertices.size
        if (vertices.size < 3) return vertices.copyOf()

        val hull = ArrayList<Vec2>(size)

        // Find leftmost point
        val vLeft = getMinCoordsPointIdx(vertices)
        hull.add(vertices[vLeft])

        // Perform Jarvis march
        var vStart = vLeft // find the most clockwise point from here
        while (hull.size <= size) {
            var vCurr = (vStart + 1) % size // current most clockwise point (next point in hull)
            /*
             * To determine the orientation of three points A, B, C,
             * Find the cross product of AB and BC.
             * - If positive, then counterclockwise
             * - If negative, then clockwise
             * - If zero, then collinear
             */
            for (i in vertices.indices) { // find the most clockwise point
                if (i == vStart || i == vCurr) continue
                val ptA = vertices[vStart]
                val ptB = vertices[vCurr]
                val ptC = vertices[i]

                val vecAB = ptB - ptA // current most clockwise
                val vecBC = ptC - ptB // compare this point
                val orientation = vecAB.cross(vecBC) // find the most clockwise point from current point
                if (orientation < 0) vCurr = i
                // TODO this is not right
                else if (FloatMath.equals(orientation)) { // or the farthest point if collinear
                    if (ptA.distanceSq(ptB) < ptA.distanceSq(ptC)) vCurr = i
                }
            }
            vStart = vCurr
            if (vStart == vLeft) break // loop until reaching leftmost point
            hull.add(vertices[vCurr]) // add most clockwise point to hull
        }
        return hull.toTypedArray()
    }

    private fun getMinCoordsPointIdx(vertices: Array<Vec2>): Int {
        var minIdx = 0
        for (i in vertices.indices) {
            val min = vertices[minIdx]
            val pt = vertices[i]
            if (pt.x < min.x) { // look for leftmost
                minIdx = i
            } else if (FloatMath.equals(pt.x, min.x)) { // use bottommost if same x
                if (pt.y < min.y) minIdx = i
            }
        }
        return minIdx
    }

    private fun getMinAnglePointIdx(vertices: Array<Vec2>): Int {
        var minIdx = 0
        for (i in vertices.indices) {
            val minAng = vertices[minIdx].angle()
            val ptAng = vertices[i].angle()
            if (abs(ptAng) < abs(minAng)) { // look for signed angle closest to 0
                minIdx = i
            } else if (FloatMath.equals(abs(ptAng), abs(minAng))) { // use positive angle if same magnitude
                if (ptAng > minAng) minIdx = i
            }
        }
        return minIdx
    }

    private fun Vec2.compareOrder(v: Vec2, center: Vec2, start: Vec2): Int {
        val startDir = start - center
        val dir1 = this - center
        val dir2 = v - center
        if (dir1.posAngle(startDir) < dir2.posAngle(startDir)) {
            return -1
        } else if (FloatMath.equals(dir1.posAngle(startDir), dir2.posAngle(startDir))) {
            if (dir1.lenSq() > dir2.lenSq()) return -1
        }
        return 1
    }

}