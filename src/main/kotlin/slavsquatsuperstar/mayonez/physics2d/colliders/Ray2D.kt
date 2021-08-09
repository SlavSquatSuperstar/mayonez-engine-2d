package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.Vec2

/**
 * An object in space with an origin point, a direction, and a length of one (a unit vector).
 * Can also represent a parametric line equation.
 *
 * @author SlavSquatSuperstar
 */
class Ray2D(val origin: Vec2, direction: Vec2) {

    val direction: Vec2 = direction.unit()

    constructor(edge: Edge2D) : this(edge.start, edge.toVector())

    /**
     * Returns a point along this ray at the specified distance.
     *
     * @param distance parameterized distance
     * @return the point
     */
    fun getPoint(distance: Float): Vec2 {
        return origin.add(direction.mul(distance))
    }

    // TODO Get length from distance along axis
    // S = √(1 + (dy/dx)^2)
    override fun toString(): String {
        return String.format("Origin: %s, Direction, %s", origin, direction)
    }

}