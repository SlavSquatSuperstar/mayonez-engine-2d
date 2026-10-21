package mayonez.math

import mayonez.annotations.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Stores an angle in degrees and radians, as well as its sine and cosine,
 * using float precision.
 *
 * @author SlavSquatSuperstar
 */
class Angle private constructor(
    deg: Float, rad: Float, cos: Float, sin: Float
) {

    companion object {
        // Factory Methods

        @JvmStatic
        fun createDegrees(degrees: Float): Angle {
            val radians = degrees.toRadians()
            return Angle(degrees, radians, cos(radians), sin(radians))
        }

        @JvmStatic
        fun createRadians(radians: Float): Angle {
            val degrees = radians.toDegrees()
            return Angle(degrees, radians, cos(radians), sin(radians))
        }
    }

    // Angle Properties

    /** The radian value for this angle as a float. */
    var radians: Float = rad
        private set

    /** The degree value for this angle as a float. */
    var degrees: Float = deg
        private set

    /** The cosine value for this angle as a float. */
    var cos: Float = cos
        private set

    /** The sine value for this angle as a float. */
    var sin: Float = sin
        private set

    /** The rotation matrix for this angle. */
    val rotation: Mat22
        get() = Mat22(cos, -sin, sin, cos)

    // Addition Methods

    /**
     * Adds this angle to another angle and returns the result.
     *
     * @param angle another angle
     * @return the angle sum
     */
    fun add(angle: Angle): Angle = this + angle

    operator fun plus(angle: Angle): Angle {
        /*
         * Angle addition identities:
         * - cos(a + b) = cos(a) * cos(b) - sin(a) * sin(b)
         * - sin(a + b) = sin(a) * cos(b) + sin(b) * cos(a)
         */
        return Angle(
            this.degrees + angle.degrees,
            this.radians + angle.radians,
            this.cos * angle.cos - this.sin * angle.sin,
            this.sin * angle.cos + angle.sin * this.cos
        )
    }

    // Mutator Methods

    /**
     * Rotates this angle by a given amount in degrees.
     *
     * @param degrees the degrees to rotate
     */
    @Mutating
    fun rotateDegrees(degrees: Float) {
        this.set(this + createDegrees(degrees))
//        val radians = degrees.toRadiansDouble()
//        val c1 = this.cosD
//        val s1 = this.sinD
//        val c2 = cos(radians)
//        val s2 = sin(radians)
//        this.radD += radians
//        this.cosD = c1 * c2 - s1 * s2
//        this.sinD = s1 * c2 + s2 * c1
    }

    /**
     * Rotates this angle by a given amount in radians.
     *
     * @param radians the radians to rotate
     */
    @Mutating
    fun rotateRadians(radians: Float) {
        this.set(this + createRadians(radians))
//        val c1 = this.cosD
//        val s1 = this.sinD
//        val c2 = cos(radians)
//        val s2 = sin(radians)
//        this.radD += radians
//        this.cosD = c1 * c2 - s1 * s2
//        this.sinD = s1 * c2 + s2 * c1
    }

    @Mutating
    fun set(angle: Angle) {
        this.degrees = angle.degrees
        this.radians = angle.radians
        this.cos = angle.cos
        this.sin = angle.sin
    }

    override fun equals(other: Any?): Boolean {
        return (other is Angle) && MathUtils.equals(this.degrees, other.degrees)
    }

    override fun hashCode(): Int {
        return degrees.hashCode()
    }

    override fun toString(): String {
        return "Angle ($degrees degrees, $radians radians)"
    }

}

// Trig Helpers

private fun Float.toRadians(): Float {
    return MathUtils.toRadians(this)
}

private fun Float.toDegrees(): Float {
    return MathUtils.toDegrees(this)
}