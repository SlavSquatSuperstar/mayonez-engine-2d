package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * An object in space that with an origin that extends infinitely in one direction.
 * Represents a ray or line in the 2D plane with the parameterized vector equation r(t) = r0 + vt.
 *
 * @constructor Constructs a ray from the given origin and direction, with the option to normalize the ray's direction.
 *
 * @author SlavSquatSuperstar
 */

class Ray(
    /**
     * The starting point of the ray, r0.
     */
    val origin: Vec2,

    direction: Vec2
) : Transformable {

    /**
     * The direction of the ray, v. The direction also determines the "length" of the ray, or how
     * far along the ray each step travels.
     */
    val direction: Vec2 = direction.unit()

    /**
     * Constructs a ray from an [Edge] object, using the starting point as the origin and keeping the segment's
     * direction and length.
     */
    constructor(edge: Edge) : this(edge.start, edge.toVector())

    /**
     * Returns a point along this ray at the specified distance.
     *
     * @param distance parameterized distance along this ray
     * @return the point on the ray
     */
    fun getPoint(distance: Float): Vec2 = origin + (direction * distance)

    // Distance Methods

    /**
     * Calculates the distance from a point to the plane (infinite line) represented by this ray.
     *
     * @param point a point in space
     * @return The perpendicular distance from the point to this ray, which will be positive if to the left,
     * negative if to the right, and zero if on this ray.
     */
    fun distance(point: Vec2): Float = (point - origin).component(direction.normal())

    // Line Intersection

    /**
     * Calculates the point of intersection between the infinite lines represented by this ray and another.
     *
     * @param ray another ray
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
        val dist1 = (start2 - start1).cross(line2) / cross
        // val dist2 = start1.sub(start2).cross(line1) / -cross
        return start1 + (line1 * dist1)
        // rotate left or right depending on which side ray started form
    }

    // Transform Methods

    /**
     * Moves this ray's origin along the given vector while preserving its direction and length.
     *
     * @param direction the direction to move
     * @return the translated ray
     */
    override fun translate(direction: Vec2): Ray = Ray(origin + direction, this.direction)

    /**
     * Rotates this ray by the given angle around an origin while preserving its direction and length.
     *
     * @param angle  the counterclockwise angle in degrees
     * @param origin the point to rotate around, or the origin if null
     * @return the rotated ray
     */
    override fun rotate(angle: Float, origin: Vec2?): Ray {
        return Ray(this.origin.rotate(angle, origin ?: Vec2()), direction.rotate(angle))
    }

    /**
     * Stretches this ray by the given scale factor. Only changes the length of the ray if normalized is set to false.
     *
     * @param factor how much to scale the ray along each axis
     * @param origin the point to scale from, or the ray's origin if null
     * @return the scaled ray
     */
    override fun scale(factor: Vec2, origin: Vec2?): Ray {
        return Ray(if (origin == null) this.origin else this.origin * factor, this.direction)
    }

    /**
     * Scales this ray's direction vector by the given factor and returns the new length.
     *
     * @param factor how much to scale the direction along each axis
     * @return the scaled length
     */
    fun scaledLength(factor: Vec2): Float = (direction * factor).len()

    /**
     * A description of the ray in the form Origin: (x, y), Direction: (dx, dy)
     */
    override fun toString(): String = "Origin: $origin, Direction, $direction"

}