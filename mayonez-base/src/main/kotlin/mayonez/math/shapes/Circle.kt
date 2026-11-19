package mayonez.math.shapes

import mayonez.math.*
import java.util.*

/**
 * A round shape defined by a center and radius. The distance between every
 * point on the circle's boundary curve and the center equal to the radius.
 *
 * Note: Despite both shapes being round, the `Circle` class is much quicker
 * to check collisions than [Ellipse]. Use [Ellipse] only if the collider will
 * be scaled non-uniformly.
 *
 * @constructor Constructs a circle with the given center and radius.
 * @param center the center
 * @param radius the radius (not diameter)
 *
 * @author SlavSquatSuperstar
 */
class Circle(
    center: Vec2,
    /** The circle's radius, `r`. */
    val radius: Float
) : Ellipse(center, Vec2(radius * 2f), 0f) {

    /**
     * Constructs a circle with the given radius centered at (0, 0).
     *
     * @param radius the radius (not diameter)
     */
    constructor(radius: Float) : this(Vec2(), radius)

    // Circle Properties

    /** The square of the circle's radius, equal to `r^2`. */
    @JvmField
    val radiusSq: Float = radius * radius

    override val isCircle: Boolean
        get() = true

    /** The area of a circle, equal to `πr^2`. */
    override fun area(): Float = MathUtils.PI * radiusSq

    // Collision Properties

    override fun boundingBox(): BoundingBox = BoundingBox(center, Vec2(radius * 2f))

    override fun toPolygon(numEdges: Int): Polygon {
        return Polygon(center, numEdges, radius)
    }

    override fun supportPoint(direction: Vec2): Vec2 = center + getRadius(direction)

    override fun getRadius(direction: Vec2): Vec2 = direction.unit() * radius

    // Physical Properties

    /**
     * The circle's centroidal moment of inertia, equal to `1/2 * mr^2`. Note
     * that this formula for a solid circle (disk).
     *
     * Polar moment of area: `I_z = π/4 *r^4 = 1/2 * Ar^2`.
     */
    override fun angularMass(mass: Float): Float = 0.5f * mass * radiusSq

    // Transformations

    override fun translate(direction: Vec2): Circle {
        return Circle(center + direction, radius)
    }

    /**
     * Rotates this circle around a point. If rotating around the center,
     * returns a copy of this circle.
     *
     * @param angle the counterclockwise angle
     * @param origin The point to rotate around. Pass in null to rotate around
     *     the center of mass.
     * @return a circle with the same center and radius
     */
    override fun rotate(angle: Float, origin: Vec2?): Circle {
        return Circle(center.rotate(angle, origin ?: center), radius)
    }

    /**
     * Scales this circle uniformly using the given vector's x-component as the
     * scale factor. To scale a circle non-uniformly, use the [Ellipse] class.
     *
     * @param factor how much to scale the radius by
     * @param origin The point to rotate around. Pass in null to rotate around
     *     the centroid.
     * @return the scaled circle
     */
    override fun scale(factor: Vec2, origin: Vec2?): Circle {
        return Circle(
            if (origin == null) center else center.scale(factor, origin),
            radius * factor.x
        )
    }

    // Circle vs Point

    override fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position
        else center + (position - center).clampLength(radius)
    }

    /**
     * Whether a point is inside the circle, meaning its distance from the
     * center is less than the radius.
     */
    override fun contains(point: Vec2): Boolean = point.distanceSq(center) <= radiusSq

    // Overrides

    /**
     * Checks whether two circles have the same center and radius.
     *
     * @param other another circle
     * @return if the two circles are equal
     */
    override fun equals(other: Any?): Boolean {
        return (other is Circle) && (this.center == other.center)
                && MathUtils.equals(this.radius, other.radius)
    }

    override fun hashCode(): Int = Objects.hash(center, radius)

    override fun toString(): String = String.format("Circle $center, r=%.4f", radius)

}