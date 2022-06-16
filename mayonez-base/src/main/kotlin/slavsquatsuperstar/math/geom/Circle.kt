package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.MathUtils.PI
import slavsquatsuperstar.math.Vec2

/**
 * A round shape defined by a center and radius. Every point on the boundary of a circle is the same distance
 * away from the center, which is the length of the radius.
 */
class Circle(private val center: Vec2, val radius: Float) : Shape() {

    /**
     * The square of the circle's radius, equal to r^2.
     */
    private val radiusSq: Float
        get() = radius * radius

    // Circle Properties

    /**
     * The area of a circle, equal to πr^2.
     */
    override fun area(): Float = PI * radiusSq

    /**
     * The perimeter of the circle, equal to its circumference 2πr.
     */
    override fun perimeter(): Float = circumference()

    /**
     * The circumference of a circle, equal to 2πr.
     */
    fun circumference(): Float = 2 * PI * radius

    /**
     * The center of the circle, equal to its center.
     */
    override fun center(): Vec2 = center

    /**
     * The circle's centroidal moment of inertia, equal to 1/2*m*r^2.
     *
     * Second moment of area: I_z = π/4*r^4 = 1/2*Ar^2
     */
    override fun angMass(mass: Float): Float = 0.5f * mass * radiusSq

    // Transformations

    override fun translate(direction: Vec2): Circle = Circle(center + direction, radius)

    /**
     * Returns a copy of this circle as rotating produces no difference.
     *
     * @return a circle with the same center and radius
     */
    override fun rotate(angle: Float): Circle = Circle(center, radius)

    /**
     * Scales this circle uniformly by the given scale factor.
     * To scale a circle non-uniformly, use the Ellipse class.
     *
     * @param factor how much to scale the radius by
     * @return the scaled circle
     */
    override fun scale(factor: Float): Circle = Circle(center, radius * factor)

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