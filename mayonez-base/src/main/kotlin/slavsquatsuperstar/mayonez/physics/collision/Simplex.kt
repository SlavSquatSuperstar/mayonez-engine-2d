package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.Vec2

/**
 * A basic data class that stores up to three support points between two overlapping shapes.
 *
 * @param firstPoint the first point in the simplex
 *
 * @author SlavSquatSuperstar
 */
internal class Simplex(firstPoint: Vec2) {

    var size: Int = 0
        private set
    private val points: Array<Vec2> = arrayOf(Vec2(), Vec2(), Vec2())

    init {
        add(firstPoint)
    }

    fun add(point: Vec2) {
        if (size >= 3) return
        points[size++] = point
    }

    operator fun get(index: Int): Vec2 {
        return when (index) {
            0 -> points[0]
            1 -> points[1]
            2 -> points[2]
            else -> Vec2()
        }
    }

    operator fun set(index: Int, point: Vec2) {
        if (index >= size) return
        points[index] = point
    }

    fun reset(firstPoint: Vec2) {
        points.fill(Vec2())
        points[0] = firstPoint
        size = 1
    }

}