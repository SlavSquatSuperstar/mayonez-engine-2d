package mayonez.math.shapes

import mayonez.math.*
import java.util.*
import kotlin.math.*

/**
 * A round shape defined by a major and minor radius and containing two
 * focal points. For each point on the ellipse's boundary curve, the sum of
 * the distances to the foci is constant.
 */
open class Ellipse(protected val center: Vec2, val size: Vec2, val angle: Float) : Shape() {

    constructor(center: Vec2, size: Vec2) : this(center, size, 0f)

    // Shape Properties

    override val numVertices: Int = 0

    private val halfWidth: Float = size.x * 0.5f

    private val halfHeight: Float = size.y * 0.5f

    // Ellipse Properties

    open val isCircle: Boolean
        get() = FloatMath.equals(size.x, size.y)

    val isAxisAligned: Boolean
        get() = FloatMath.equals(angle % 360f, 0f)

    /**
     * The area of an ellipse, equal to πab, where a is half the width
     * (semi-major axis) and b is half the height (semi-minor axis).
     */
    override fun area(): Float = FloatMath.PI * halfWidth * halfHeight

    /** The centroid of the ellipse, equal to its center position. */
    override fun center(): Vec2 = center

    // Min Bounds

    override fun boundingCircle(): Circle = Circle(center, if (isCircle) halfWidth else max(halfWidth, halfHeight))

    // Source: https://iquilezles.org/articles/ellipses/
    override fun boundingRectangle(): BoundingBox {
        // r(t) = c + a*cos(t) + b*sin(t)
        val rot = Mat22(angle)
        val vecA = rot * Vec2(halfWidth, 0f) // ellipse's horizontal axis
        val vecB = rot * Vec2(0f, halfHeight) // ellipse's vertical axis

        val boxHalfWidth = FloatMath.hypot(vecA.x, vecB.x)
        val boxHalfHeight = FloatMath.hypot(vecA.y, vecB.y)
        return BoundingBox(center, Vec2(boxHalfWidth, boxHalfHeight) * 2f)
    }

    /** Returns a polygon approximation of this ellipse with 2πa vertices. */
    open fun toPolygon(): Polygon {
        val numEdges: Int = (2f * FloatMath.PI * halfWidth).roundToInt() // use πa for # edges
        val unitCircle = Polygon(center, numEdges, 1f)
        return unitCircle.scale(Vec2(halfWidth, halfHeight), center).rotate(this.angle, center)
    }

    // Physical Properties

    /**
     * The ellipse's centroidal moment of inertia, equal to 1/4*m(a^2+b^2),
     * where a is half the width and b is half the height.
     *
     * Second moment of area: I_x = π/4*ab^3, I_y = π/4*ba^3 Polar moment of
     * area: I_z = π/4*(ba^3 + ab^3) = 1/4*πab*(a^2 + b^2) = 1/4*A(a^2+b^2)
     */
    override fun angularMass(mass: Float): Float = 0.25f * mass * FloatMath.hypotSq(halfWidth, halfHeight)

    // Transformations

    override fun translate(direction: Vec2): Ellipse {
        return Ellipse(center + direction, size, angle)
    }

    override fun rotate(angle: Float, origin: Vec2?): Ellipse {
        return Ellipse(
            center.rotate(angle, origin ?: center),
            size, this.angle + angle
        )
    }

    override fun scale(factor: Vec2, origin: Vec2?): Ellipse {
        return Ellipse(
            if (origin == null) center else center.scale(factor, origin),
            size * factor, angle
        )
    }

    // Ellipse vs Point

    /**
     * The farthest point on the ellipse in any given direction. Due to the
     * non-uniform curvature of an ellipse, the support point is not equivalent
     * to the ellipse's radius vector in that direction (see [getRadius]).
     *
     * Source: org.dyn4j.geometry.Ellipse.getFarthestPointOnAlignedEllipse
     */
    override fun supportPoint(direction: Vec2): Vec2 {
        var localDir = direction.rotate(-angle)
        val halfSize = Vec2(halfWidth, halfHeight)
        localDir = (localDir * halfSize).unit() * halfSize
        localDir = localDir.rotate(angle)
        return center + localDir
    }

    /**
     * Calculates the ellipse's radius vector, or the point on the ellipse in
     * the given direction. Not equivalent to the support point function (see
     * [supportPoint]).
     *
     * Source:
     * https://en.wikipedia.org/wiki/Ellipse#Polar_form_relative_to_center
     */
    open fun getRadius(direction: Vec2): Vec2 {
        val theta = direction.angle() - this.angle // theta, angle of point from ellipse's x-axis
        val cos = FloatMath.cos(theta)
        val sin = FloatMath.sin(theta)

        val a = halfWidth // half width, a
        val b = halfHeight // half height, b
        val eSq = 1 - FloatMath.squared(b / a) // eccentricity squared, e^2 = 1 - b^2/a^2

        // radius, r(theta) = b / √[1 - e^2*cos(theta)^2]
        val radius = b / FloatMath.sqrt(1 - (eSq * cos * cos))
        return Vec2(cos, sin).rotate(this.angle) * radius // unit direction times length
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun contains(point: Vec2): Boolean {
        // Perform a circle radius test against the support point
        val ptToCenter = point - center
        val ptToEllipse = getRadius(ptToCenter)
        return ptToCenter.lenSq() <= ptToEllipse.lenSq()
    }

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Ellipse) && (this.center == other.center)
                && (this.size == other.size)
                && (FloatMath.equals(this.angle, other.angle))
    }

    override fun hashCode(): Int = Objects.hash(center, size, angle)

    /**
     * A description of the ellipse in the form "Ellipse (x, y), Size: (b, h),
     * Rotation: theta"
     */
    override fun toString(): String = String.format("Ellipse $center, Size: $size, Rotation: %.2f°", angle)

}