package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.MathUtils.PI
import slavsquatsuperstar.math.MathUtils.toRadians
import slavsquatsuperstar.math.Vec2
import kotlin.math.*

/**
 * A round shape defined by a major and minor radius and containing two focal points. For each point on the ellipse's
 * boundary curve, the sum of the distances to the foci is constant.
 */
open class Ellipse(private val center: Vec2, val size: Vec2, val angle: Float) : Shape() {

    constructor(center: Vec2, size: Vec2) : this(center, size, 0f)

    // Ellipse Properties

    override val numVertices: Int = 0

    private val halfWidth: Float = size.x * 0.5f

    private val halfHeight: Float = size.y * 0.5f

    open val isCircle: Boolean = MathUtils.equals(size.x, size.y)

    /**
     * The area of an ellipse, equal to πab, where a is half the width and b is half the height.
     */
    override fun area(): Float = PI * halfWidth * halfHeight

    /**
     * The centroid of the ellipse, equal to its center position.
     */
    override fun center(): Vec2 = center

    override fun boundingCircle(): Circle = Circle(center, max(halfWidth, halfHeight))

    override fun boundingRectangle(): Rectangle {
        val supX = supportPoint(Vec2(1f, 0f))
        val supY = supportPoint(Vec2(0f, 1f))
        val rectSize = Vec2(supX.x - center.x, supY.y - center.y) * 2f
        return Rectangle(center, rectSize)
    }

    // Source: https://www.youtube.com/watch?v=ajv46BSqcK4&lc=UgwuSOaT8lRug73ljUJ4AaABAg
    override fun supportPoint(direction: Vec2): Vec2 {
        // Find point where tangent is 0
        val angle = toRadians(-direction.angle() + this.angle)
        val t = atan(-halfHeight / halfWidth * tan(angle))
        var point = Vec2(halfWidth * cos(t), halfHeight * sin(t)).rotate(this.angle)
        if (point.dot(direction) < 0) point = -point

//        val point = Vec2(halfWidth, halfHeight) * direction.unit().rotate(-angle)
        return center + point
    }

//    private fun pointInDirection(direction: Vec2): Vec2 {
//        // s, k = width, height
//        // m = sqrt( (cos(a)/k)^2 + (sin(a)/s)^2 )
//        // x = cos(a)/m
//        // y = sin(a)/m
//        // assume rotation is 0
//        val ang = direction.angle() - angle
//        val cos = MathUtils.cos(ang)
//        val sin = MathUtils.sin(ang)
//        val len = 1f / MathUtils.hypot(cos / halfWidth, sin / halfHeight)
//        return center + Vec2(cos, sin).rotate(angle) * len // unit direction times length
//    }

    // Physical Properties

    /**
     * The ellipse's centroidal moment of inertia, equal to 1/4*m(a^2+b^2),  where a is half the width and b is half
     * the height.
     *
     * Second moment of area: I_x = π/4*ab^3, I_y = π/4*ba^3
     * Polar moment of area: I_z = π/4*(ba^3 + ab^3) = 1/4*πab*(a^2 + b^2) = 1/4*A(a^2+b^2)
     */
    override fun angularMass(mass: Float): Float = 0.25f * mass * MathUtils.hypotSq(halfWidth, halfHeight)

    // Transformations

    override fun translate(direction: Vec2): Ellipse = Ellipse(center + direction, size, angle)

    override fun rotate(angle: Float, origin: Vec2?): Ellipse {
        return Ellipse(center.rotate(angle, origin ?: center), size, this.angle + angle)
    }

    override fun scale(factor: Vec2, origin: Vec2?): Ellipse {
        return Ellipse(if (origin == null) center else center.scale(factor, origin), size * factor, angle)
    }

    override fun contains(point: Vec2): Boolean {
        /*
         * Problem: When circle is stretched into ellipse, angle (theta) changes while parameter (t) does not
         * Goal: Find t of point on ellipse for given angle
         * Ellipse: x = a*cos(t), y = b*sin(t)
         *
         * tan(theta) = b/a * tan(t)
         * tan(t) = a/b * tan(theta)
         * t = atan(a/b * tan(theta))
         */
        // Target Point: A, Point on Ellipse: B, Center: C
        val vecCA = point - center // CA
        val pointAngleRad = toRadians(vecCA.angle() - angle) // angle of CA with ellipse's x-axis

        val radius = Vec2(halfWidth, halfHeight)
        val tEllipse = atan(radius.x / radius.y * tan(pointAngleRad)) // t parameter matching angle
        val vecCB = Vec2(cos(tEllipse), sin(tEllipse)) * radius
//        val pointOnEllipse = pointInDirection(centerToPoint)
        return vecCA.lenSq() <= vecCB.lenSq()
    }

    override fun equals(other: Any?): Boolean {
        return (other is Ellipse) && (this.center == other.center) && (this.size == other.size)
                && (MathUtils.equals(this.angle, other.angle))
    }

    /**
     * A description of the ellipse in the form "Ellipse (x, y), Size: (b, h), Rotation: theta"
     */
    override fun toString(): String = String.format("Ellipse $center, Size: $size, Rotation: %.2f°", angle)

}