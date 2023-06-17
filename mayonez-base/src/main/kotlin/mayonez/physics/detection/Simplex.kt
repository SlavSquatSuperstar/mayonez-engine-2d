package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*

/**
 * A basic data class that stores a certain number of support points
 * between two overlapping shapes.
 *
 * @param points any known points in the simplex
 * @param maxSize the size of the simplex, by default 3
 * @author SlavSquatSuperstar
 */
internal class Simplex(vararg points: Vec2, private val maxSize: Int = 3) {

    var size: Int = 0
        private set

    private val points: MutableList<Vec2> = ArrayList(maxSize)

    init {
        for (pt in points) {
            add(pt)
            if (size >= maxSize) break
        }
    }

    operator fun get(index: Int): Vec2 = if (index < size) points[index] else Vec2()

    operator fun set(index: Int, point: Vec2) {
        if (index < size) points[index] = point
    }

    /**
     * Appends a point to the end of this simplex.
     *
     * @param point a support point
     */
    internal fun add(point: Vec2) {
        if (size >= maxSize) return
        points.add(point)
        size++
    }

    /**
     * Inserts a point in the middle of this simplex.
     *
     * @param index the index to insert the point before
     * @param point a support point
     */
    internal fun add(index: Int, point: Vec2) {
        if (size >= maxSize || index !in points.indices) return
        points.add(index, point)
        size++
    }

    /**
     * Removes a point from this simplex.
     *
     * @param index the index of the point to remove
     */
    internal fun remove(index: Int) {
        if (index >= size || index < 0) return
        points.removeAt(index)
        size--
    }

    /**
     * Expands this simplex while keeping the original points.
     *
     * @param newSize the size of the new simplex
     * @return the larger simplex
     */
    internal fun expand(newSize: Int): Simplex = Simplex(*points.toTypedArray(), maxSize = newSize)

    /**
     * Creates a polygon from the points in the simplex. Useful for debugging.
     *
     * @return a polygon
     */
    internal fun toPolygon(): Polygon = Polygon(*this.points.toTypedArray())

}