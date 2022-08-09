package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.Vec2

/**
 * A basic data class that stores a certain number of support points between two overlapping shapes.
 *
 * @param points  any known points in the simplex
 * @param maxSize the size of the simplex, by default 3
 *
 * @author SlavSquatSuperstar
 */
internal class Simplex(vararg points: Vec2, private val maxSize: Int = 3) {

    var size: Int = 0
        private set

    // TODO set to private after testing
    internal val points: MutableList<Vec2> = ArrayList(maxSize)

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

    fun add(point: Vec2) {
        if (size >= maxSize) return
        points.add(point)
        size++
    }

    fun remove(index: Int) {
        if (index >= size || index < 0) return
        points.removeAt(index)
        size--
    }

    fun expand(newSize: Int): Simplex = Simplex(*points.toTypedArray(), maxSize = newSize)

}