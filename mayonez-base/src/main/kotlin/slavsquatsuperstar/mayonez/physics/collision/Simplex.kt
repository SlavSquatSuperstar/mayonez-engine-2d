package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.Vec2

/**
 * A basic data class that stores a limited number of support points between two overlapping shapes.
 *
 * @param points any known points in the simplex
 *
 * @author SlavSquatSuperstar
 */
internal class Simplex(vararg points: Vec2, private val maxSize: Int = 3) {

    var size: Int = 0
        private set
    private val points: Array<Vec2> = Array(maxSize) { Vec2() }

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
        points[size++] = point
    }

    fun remove(index: Int) {
        if (index > size) return

        // shift points down to fill in empty
        for (i in index until size - 1) points[i] = points[i + 1]
        points[--size] = Vec2() // delete last
    }

    fun expand(newSize: Int): Simplex = Simplex(*points, maxSize = newSize)

}