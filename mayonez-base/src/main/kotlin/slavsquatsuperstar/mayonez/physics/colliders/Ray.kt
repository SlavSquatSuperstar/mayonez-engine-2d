package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Transform
import kotlin.math.abs

/**
 * An object in space that with an origin that extends infinitely in one direction.
 * Represents a ray or line in the 2D plane with the parameterized vector equation r(t) = r0 + vt.
 *
 * @constructor Constructs a ray from the given origin and direction, with the option to normalize the ray's direction.
 *
 * @author SlavSquatSuperstar
 */

// TODO maybe make direction not unit, so allow ray to be transformed
class Ray(
    /**
     * The starting point of the ray, r0.
     */
    val origin: Vec2,

    /**
     * The direction of the ray, v. The direction also determines the "length" of the ray, or how
     * far along the ray each step travels.
     */
    val direction: Vec2,

    /**
     * Whether the direction of the ray should be normalized, meaning each step along the ray travels one unit in the world.
     */
    normalized: Boolean
) {
    init {
        if (normalized) direction.set(direction.unit())
    }

    /**
     * Constructs a normalized ray from the given origin and direction.
     */
    constructor(origin: Vec2, direction: Vec2) : this(origin, direction, true)

    /**
     * Constructs a ray from an [Edge2D] object, using the starting point as the origin and normalizing the segment's direction.
     */
    constructor(edge: Edge2D) : this(edge.start, edge.toVector(), true)

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
        return point.sub(origin).component(Vec2(-direction.y, direction.x))
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
    fun getIntersection(ray: Ray): Vec2? {
        val start1 = this.origin
        val start2 = ray.origin

        // Calculate point of intersection using cross product
        val line1 = this.direction
        val line2 = ray.direction
        val cross = line1.cross(line2)
        if (MathUtils.equals(cross, 0f)) return null // if parallel

        // Parametric lengths along rays
        val dist1 = start2.sub(start1).cross(line2) / cross
        // val dist2 = start1.sub(start2).cross(line1) / -cross
        return start1.add(line1.mul(dist1))
        // rotate left or right depending on which side ray started form
    }

    fun transform(transform: Transform, scale: Boolean = false): Ray {
        val scaleFactor = if (scale) transform.scale else Vec2(1f)
        // Completely transform origin, but don't translate direction
        val newDir = (direction / scaleFactor).rotate(-transform.rotation)
        return Ray(transform.toLocal(origin), newDir, false)
    }

    // TODO Get length from distance along axis
    // S = √(1 + (dy/dx)^2)
    /**
     * A description of the ray in the form Origin: (x, y), Direction: (dx, dy)
     */
    override fun toString(): String {
        return "Origin: $origin, Direction, $direction"
//        return "$origin + ${direction}t"
    }

}