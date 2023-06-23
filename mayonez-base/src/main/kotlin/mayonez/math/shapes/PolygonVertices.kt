package mayonez.math.shapes

import mayonez.math.*
import java.util.*
import kotlin.math.*

/**
 * Contains methods for generating and sorting polygon vertices.
 *
 * @author SlavSquatSuperstar
 */
// TODO split into classes
object PolygonVertices {

    /**
     * Orders a list of vertices in the counterclockwise direction. The output
     * will always have the same size as the input.
     *
     * Source: [Baeldung](https://www.baeldung.com/cs/sort-points-clockwise)
     *
     * @param vertices an array of vertices
     * @return the ordered array of vertices
     */
    fun orderedVertices(vertices: Array<Vec2>): Array<Vec2> {
        if (vertices.size < 3) return vertices.copyOf()

        val start = vertices[vertices.getMinCoordsPointIdx()]
        val center = vertices.getCenterPoint()

        val sorted = vertices.copyOf()
        Arrays.sort(sorted) { v1, v2 -> v1.compareOrder(v2, center, start) }
        return sorted
    }

    private fun Array<Vec2>.getCenterPoint(): Vec2 {
        var center = Vec2()
        this.forEach { center += it }
        return center / this.size.toFloat()
    }

    private fun Vec2.compareOrder(other: Vec2, center: Vec2, start: Vec2): Int {
        val startDir = start - center
        val dir1 = this - center
        val dir2 = other - center
        if (dir1.posAngle(startDir) < dir2.posAngle(startDir)) {
            // pick smaller angle
            return -1
        } else if (FloatMath.equals(dir1.posAngle(startDir), dir2.posAngle(startDir))) {
            // pick smaller coords if same angle
            if (dir1.lenSq() > dir2.lenSq()) return -1
        }
        return 1
    }

    /**
     * Creates a convex hull ordered counterclockwise from an array of vertices
     * using the Jarvis March algorithm. The convex hull is the smallest
     * polygon that contains all the vertices.
     *
     * Source: [Baeldung](https://www.baeldung.com/cs/sort-points-clockwise)
     */
    fun orderedConvexHull(vertices: Array<Vec2>): Array<Vec2> {
        val size = vertices.size
        if (size < 3) return vertices.copyOf()

        val hull = ArrayList<Vec2>(size)

        val leftMost = vertices.getMinCoordsPointIdx()
        hull.add(vertices[leftMost])

        hull.addConvexHullPoints(leftMost, vertices)
        return hull.toTypedArray()
    }

    private fun ArrayList<Vec2>.addConvexHullPoints(
        vFirst: Int, vertices: Array<Vec2>
    ) {
        val numVertices = vertices.size
        var vStart = vFirst
        while (this.size <= numVertices) {
            var vCurr = (vStart + 1) % numVertices // current most clockwise point (next point in hull)
            for (i in vertices.indices) { // find the most clockwise point
                if (i == vStart || i == vCurr) continue
                // find the most clockwise point from current point
                vCurr = vertices.findNextClockwisePoint(vStart, vCurr, i)
            }
            vStart = vCurr // update start point
            if (vStart == vFirst) break // loop until reaching leftmost point
            else this.add(vertices[vCurr]) // add most clockwise point to hull
        }
    }

    private fun Array<Vec2>.findNextClockwisePoint(
        vStart: Int, vCurr: Int, vNext: Int
    ): Int {
        val ptA = this[vStart]
        val ptB = this[vCurr]
        val ptC = this[vNext]

        return when (getOrientation(ptA, ptB, ptC)) {
            Orientation.COUNTERCLOCKWISE -> vNext
            Orientation.CLOCKWISE -> vCurr
            Orientation.COLLINEAR -> {
                val nextPointIsFurther = ptA.distanceSq(ptC) > ptA.distanceSq(ptB)
                if (nextPointIsFurther) vNext
                else vCurr
            }
        }
    }

    private fun getOrientation(ptA: Vec2, ptB: Vec2, ptC: Vec2): Orientation {
        /*
         * To determine the orientation of three points A, B, C:
         * Find the cross product of AB and BC.
         * - If positive, then counterclockwise
         * - If negative, then clockwise
         * - If zero, then collinear
         */
        val vecAB = ptB - ptA
        val vecBC = ptC - ptB
        val orientation = vecAB.cross(vecBC)
        return when {
            orientation < 0f -> Orientation.COUNTERCLOCKWISE
            orientation > 0f -> Orientation.CLOCKWISE
            else -> Orientation.COLLINEAR
        }
    }

    /**
     * Get the leftmost point, or select the bottommost point if multiple
     * points are found.
     */
    private fun Array<Vec2>.getMinCoordsPointIdx(): Int {
        var minIdx = 0
        for (i in this.indices) {
            val min = this[minIdx]
            val pt = this[i]

            if (pt.x < min.x) { // look for leftmost
                minIdx = i
            } else if (FloatMath.equals(pt.x, min.x)) { // use bottommost if same x
                if (pt.y < min.y) minIdx = i
            }
        }
        return minIdx
    }

    /** Get the point with the minimum absolute angle with the positive x-axis. */
    private fun Array<Vec2>.getMinAnglePointIdx(): Int {
        var minIdx = 0
        for (i in this.indices) {
            val minAng = this[minIdx].angle()
            val ptAng = this[i].angle()

            if (ptAng.isAbsSmaller(minAng)) {
                // look for angle absolute value closest to 0
                minIdx = i
            } else if (ptAng.isAbsEqual(minAng)) {
                // use the positive angle if same magnitude
                if (ptAng > minAng) minIdx = i
            }
        }
        return minIdx
    }

    private fun Float.isAbsSmaller(other: Float): Boolean {
        return abs(this) < abs(other)
    }

    private fun Float.isAbsEqual(other: Float): Boolean {
        return FloatMath.equals(abs(this), abs(other))
    }

    private enum class Orientation {
        COUNTERCLOCKWISE,
        CLOCKWISE,
        COLLINEAR
    }

}