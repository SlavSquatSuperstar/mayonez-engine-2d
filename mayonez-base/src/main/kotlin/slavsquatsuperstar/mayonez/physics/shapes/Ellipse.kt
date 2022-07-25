package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.MathUtils.PI
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Colors
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import kotlin.math.cos
import kotlin.math.sqrt

/**
 * A round shape defined by two focal points. For each point on the ellipse's boundary curve, the sum of
 * the distances to the foci is constant.
 */
@ExperimentalFeature
class Ellipse(val center: Vec2, val size: Vec2, val angle: Float) : Shape() {

    constructor(center: Vec2, size: Vec2) : this(center, size, 0f)

    // Ellipse Properties

    val width: Float = size.x

    val height: Float = size.y

    val radius: Vec2 = (size * 0.5f).rotate(angle) // radius vector

    // If the major axis is horizontally oriented
    private val xMajor: Boolean = size.x > size.y

    /**
     * The semi-major axis, a, equal to half the length of the longer dimension.
     */
    private val semiMajor: Float = if (xMajor) size.x * 0.5f else size.y * 0.5f

    /**
     * The semi-minor axis, b, equal to half the length of the shorter dimension.
     */
    private val semiMinor: Float = if (xMajor) size.y * 0.5f else size.x * 0.5f

    // The unit direction of the major axis.
    private val majorAxis: Vec2 = if (xMajor) Vec2(1f, 0f).rotate(angle) else Vec2(0f, 1f).rotate(angle)

    /**
     * The focal length, c, equal to √(a^2 - b^2).
     */
    private val focalLength: Float = sqrt(semiMajor * semiMajor - semiMajor * semiMinor)

    /**
     * The eccentricity, e, equal to c/a and ranging [0, 1). An ellipse with e = 0 is a circle.
     */
    private val eccentricity: Float = focalLength / semiMajor

    /**
     * The foci (focuses) of the ellipse, which are two points along the major axis of distance c from the center.
     */
    private val foci: Array<Vec2> = getEllipseFoci(center, size, angle)

    /**
     * The area of an ellipse, equal to πab.
     */
    override fun area(): Float = PI * semiMajor * semiMinor

    /**
     * The centroid of the ellipse, equal to its center position.
     */
    override fun center(): Vec2 = center

    /**
     * The ellipse's centroidal moment of inertia, equal to 1/4*m(a^2+b^2).
     *
     * Second moment of area: I_x = π/4*ab^3, I_y = π/4*ba^3
     * Polar moment of area: I_z = π/4*(ba^3 + ab^3) = 1/4*πab*(a^2 + b^2) = 1/4*A(a^2+b^2)
     */
    override fun angularMass(mass: Float): Float = 0.25f * mass * MathUtils.hypotSq(semiMajor, semiMinor)

    override fun boundingCircle(): Circle = Circle(center, semiMajor)

    override fun boundingRectangle(): BoundingRectangle {
        TODO("Not yet implemented")
    }

    override fun supportPoint(direction: Vec2): Vec2 {
        TODO("Not yet implemented")
    }

    override fun translate(direction: Vec2): Ellipse = Ellipse(center + direction, size, angle)

    override fun rotate(angle: Float, origin: Vec2?): Ellipse {
        return Ellipse(center.rotate(angle, origin ?: center), size, this.angle + angle)
    }

    override fun scale(factor: Vec2, origin: Vec2?): Ellipse {
        return Ellipse(if (origin == null) center else center.scale(factor, origin), size * factor, angle)
    }

    // Source: https://en.wikipedia.org/wiki/Ellipse#Polar_form_relative_to_center
    override fun contains(point: Vec2): Boolean {
        val diff = point - center
        val distToPoint = diff.len()
        // angle is between major axis and direction to point
        val angle = majorAxis.angle(diff)
        val radLen = semiMinor / sqrt(1 - MathUtils.squared(eccentricity * cos(angle)))
        val rad = diff * (radLen / distToPoint)

//        DebugDraw.drawLine(center, point, Colors.BLACK)
        DebugDraw.drawVector(center, rad, Colors.RED)

        DebugDraw.drawVector(center, radius, Colors.BLUE)


        // Get ellipse radius with angle

        // Sum of distances to foci is less than 2a
//        val diff1 = point - foci[0]
//        val diff2 = point - foci[1]
//
//        DebugDraw.drawVector(foci[0], diff1, Colors.BLACK)
//        DebugDraw.drawVector(foci[1], diff2, Colors.BLACK)
//
//        val dist1 = diff1.len()
//        val dist2 = diff2.len()
//
//        return dist1 + dist2 <= 2 * semiMajor

        // Radius method
//        val dir = diff.unit()
//        val radiusDir = radius * dir


//        DebugDraw.drawVector(center, radiusDir, Colors.BLUE)

//        return diff.lenSq() <= radiusDir.lenSq()
        return false
    }

    override fun equals(other: Any?): Boolean {
        return (other is Ellipse) && (this.center == other.center) && (this.size == other.size)
                && (MathUtils.equals(this.angle, other.angle))
    }

    /**
     * A description of the ellipse in the form "Ellipse (x, y), Size: (b, h), Rotation: theta"
     */
    override fun toString(): String = String.format("Ellipse $center, Size: $size, Rotation: %.2f°", angle)

    companion object {

        fun getEllipseFoci(center: Vec2, size: Vec2, angle: Float): Array<Vec2> {
            // find focal length and major axis direction
            val major: Float
            val minor: Float
            val majorAxisDir: Vec2

            if (size.x > size.y) { // x greater
                major = size.x
                minor = size.y
                majorAxisDir = Vec2(1f, 0f).rotate(angle)
            } else { // y greater
                major = size.y
                minor = size.x
                majorAxisDir = Vec2(0f, 1f).rotate(angle)
            }

            val focalLength = MathUtils.invHypot(major, minor) * 0.25f // major -> semi-major
            val focalAxis = majorAxisDir * focalLength

            return arrayOf(center + focalAxis, center - focalAxis)
        }

    }

}