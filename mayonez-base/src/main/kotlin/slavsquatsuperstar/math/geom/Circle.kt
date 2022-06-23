package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.MathUtils.PI
import slavsquatsuperstar.math.Vec2

/**
 * A round shape defined by a center and radius. The distance between every point on the circle's boundary curve
 * and the center equal to the radius.
 *
 * @author SlavSquatSuperstar
 */
class Circle(private val center: Vec2, val radius: Float) : Shape() {

    /**
     * The square of the circle's radius, equal to r^2.
     */
    private val radiusSq: Float
        get() = radius * radius

    // Circle Properties

    /**
     * The diameter, or width of the circle, equal to 2r.
     */
    val diameter: Float
        get() = 2f * radius

    /**
     * The area of a circle, equal to πr^2.
     */
    override fun area(): Float = PI * radiusSq

    /**
     * The circumference of a circle, equal to 2πr.
     */
    fun circumference(): Float = 2f * PI * radius

    /**
     * The perimeter of the circle, equal to its circumference 2πr.
     */
    override fun perimeter(): Float = circumference()

    /**
     * The center of the circle, equal to its center.
     */
    override fun center(): Vec2 = center

    /**
     * The circle's centroidal moment of inertia, equal to 1/2*mr^2.
     *
     * Second moment of area: I_z = π/4*r^4 = 1/2*(πr^2)r^2 = 1/2*Ar^2
     */
    override fun angularMass(mass: Float): Float = 0.5f * mass * radiusSq

    // Transformations

    override fun translate(direction: Vec2): Circle = Circle(center + direction, radius)

    /**
     * Rotates this circle around a point. If rotating around the center, returns a copy of this circle.
     *
     * @param angle the counterclockwise angle
     * @param origin The point to rotate around. Pass in null to rotate around the center of mass.
     * @return a circle with the same center and radius
     */
    override fun rotate(angle: Float, origin: Vec2?): Circle = Circle(center.rotate(angle, origin ?: center), radius)

    /**
     * Scales this circle uniformly using the given vector's x-component as the scale factor.
     *
     * @param factor how much to scale the radius by
     * @param centered whether to scale the circle from its center and not the origin (0, 0)
     * @return the scaled circle
     */
    // To scale a circle non-uniformly, use the Ellipse class.
    override fun scale(factor: Vec2, centered: Boolean): Circle =
        Circle(if (centered) center else center * factor, radius * factor.x)

    // Overrides

    /**
     * Whether a point is inside the circle, meaning its distance form the center is less than the radius.
     */
    override fun contains(point: Vec2): Boolean = point.distanceSq(center) <= radiusSq

    override fun equals(other: Any?): Boolean {
        return (other is Circle) && (this.center == other.center) && MathUtils.equals(this.radius, other.radius)
    }

    /**
     * A description of the circle in the form Circle (x, y), r=radius.
     */
    override fun toString(): String = String.format("Circle $center, r=%.4f", radius)

}