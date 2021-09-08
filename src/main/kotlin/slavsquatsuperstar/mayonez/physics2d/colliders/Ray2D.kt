package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import kotlin.math.abs

/**
 * An object in space that with an origin that extends infinitely in one direction.
 *
 * @author SlavSquatSuperstar
 */
class Ray2D(val origin: Vec2, direction: Vec2) {

    val direction: Vec2 = direction.unit()

    constructor(edge: Edge2D) : this(edge.start, edge.toVector())

    /**
     * Returns a point along this ray at the specified distance.
     *
     * @param distance parameterized distance along this ray
     * @return the point
     */
    fun getPoint(distance: Float): Vec2 {
        return origin.add(direction.mul(distance))
    }

    // Distance Methods

    /**
     * Calculates the distance from a point to the plane (infinite line) represented by this ray.
     *
     * @param point a 2D point
     * @return The perpendicular distance from the point to this ray, which will be positive if to the left,
     * negative if to the right, and zero if on this ray.
     */
    fun distance(point: Vec2): Float {
        return point.sub(origin).projectedLength(Vec2(-direction.y, direction.x))
    }

    /**
     * Calculates the absolute value distance from a point to the plane (infinite line) represented by this ray.
     *
     * @param point a 2D point
     * @return the absolute value distance
     */
    fun distanceAbs(point: Vec2): Float {
        return abs(distance(point))
    }

    // Line Intersection

    /**
     * Calculates the point of intersection between the infinite lines represented by this ray and another.
     *
     * @param ray another 2D ray
     * @return the intersection point, or null if both rays are parallel
     */
    fun getIntersection(ray: Ray2D): Vec2? {
        val start1 = this.origin
        val start2 = ray.origin

        // Calculate point of intersection using cross product
        val line1 = this.direction
        val line2 = ray.direction
        val cross = line1.cross(line2)
        if (MathUtils.equals(cross, 0f))
            return null // if parallel

        // Parametric lengths along rays
        val dist1 = start2.sub(start1).cross(line2) / cross
        return start1.add(line1.mul(dist1))
        // rotate left or right depending on which side ray started form
    }

    // TODO Get length from distance along axis
    // S = √(1 + (dy/dx)^2)
    override fun toString(): String {
        return String.format("Origin: %s, Direction, %s", origin, direction)
    }

}