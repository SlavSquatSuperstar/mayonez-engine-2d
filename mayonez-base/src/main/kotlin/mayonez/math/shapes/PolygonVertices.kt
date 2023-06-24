package mayonez.math.shapes

import mayonez.math.*
import mayonez.math.shapes.Shape.Companion.scale
import java.util.*
import kotlin.math.*

/**
 * Contains methods for sorting and transforming polygon vertices.
 *
 * @author SlavSquatSuperstar
 */
// TODO vertices equals method
object PolygonVertices {

    internal const val MIN_VERTICES: Int = 3

    /**
     * Orders a list of vertices in the counterclockwise direction. The output
     * will always have the same size as the input.
     *
     * Source: [Baeldung](https://www.baeldung.com/cs/sort-points-clockwise)
     *
     * @param vertices an array of vertices, may be concave
     * @return the ordered array of vertices, or a copy if the input has 2 or
     *     fewer points
     */
    fun orderedVertices(vertices: Array<Vec2>): Array<Vec2> {
        if (vertices.size < MIN_VERTICES) return vertices.copyOf()

        val start = vertices[vertices.getMinCoordsPointIdx()]
        val center = vertices.getAveragePoint()

        val sorted = vertices.copyOf()
        Arrays.sort(sorted) { v1, v2 -> v1.compareVertexOrder(v2, start, center) }
        return sorted
    }

    private fun Array<Vec2>.getAveragePoint(): Vec2 {
        var center = Vec2()
        this.forEach { center += it }
        return center / this.size.toFloat()
    }

    private fun Vec2.compareVertexOrder(other: Vec2, start: Vec2, center: Vec2): Int {
        val startDir = start - center
        val dir1 = this - center
        val dir2 = other - center
        // todo use orientation
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
     * Get the leftmost point, or select the bottommost point if multiple
     * points are found.
     */
    internal fun Array<Vec2>.getMinCoordsPointIdx(): Int {
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

    // Transform Methods

    /**
     * Rotates an array of vertices around a center.
     *
     * @param direction the direction of translation
     * @return the translated vertex array
     */
    internal fun Array<Vec2>.translate(direction: Vec2): Array<Vec2> {
        return Array(size) { this[it] + direction }
    }

    /**
     * Rotates an array of vertices around a center.
     *
     * @param angle the counterclockwise angle
     * @param center the center of rotation
     * @return the rotated vertex array
     */
    internal fun Array<Vec2>.rotate(angle: Float, center: Vec2): Array<Vec2> {
        val rot = Mat22(angle) // save rotation matrix
        return Array(size) { (rot * (this[it] - center)) + center }
    }

    /**
     * Scales an array of vertices from a center.
     *
     * @param factor the scale factor
     * @param center the center for scaling
     * @return the scaled vertex array
     */
    internal fun Array<Vec2>.scale(factor: Vec2, center: Vec2): Array<Vec2> {
        return Array(size) { this[it].scale(factor, center) }
    }


}