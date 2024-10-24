package mayonez.math

import mayonez.annotations.*
import kotlin.math.*

/**
 * An object representing an angle or other rotational quantity, which can
 * be converted between degrees and radians.
 *
 * @author SlavSquatSuperstar
 */
class Angle private constructor(
    private var radD: Double, // most math functions use radian values as doubles
    private var cosD: Double, // cosine as double
    private var sinD: Double // sine as double
) {

    private constructor(radD: Double) : this(radD, cos(radD), sin(radD))

    companion object {
        // Factory Methods

        @JvmStatic
        fun createDegrees(degrees: Float): Angle = Angle(degrees.toRadiansDouble())

        @JvmStatic
        fun createRadians(radians: Float): Angle = Angle(radians.toDouble())

        // Trig Helpers

        private fun Float.toRadiansDouble(): Double {
            return Math.toRadians(this.toDouble())
        }
    }

    // Angle Properties

    /** The radian value for this angle as a float. */
    val radians: Float
        get() = radD.toFloat()

    /** The degree value for this angle as a float. */
    val degrees: Float
        get() = Math.toDegrees(radD).toFloat()

    /** The cosine value for this angle as a float. */
    val cos: Float
        get() = cosD.toFloat()

    /** The sine value for this angle as a float. */
    val sin: Float
        get() = sinD.toFloat()

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
         * When concatenating two transformations with cos/sin values c1/s1 and c2/s2,
         * the result, through angle addition identities, becomes:
         * - cos(a+b) c1*c2 - s1*s2
         * - sin(a+b) s1*c2 + s2*c1
         */
        return Angle(
            this.radD + angle.radD,
            this.cosD * angle.cosD - this.sinD * angle.sinD,
            this.sinD * angle.cosD + angle.sinD * this.cosD
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
        this.radD = angle.radD
        this.cosD = angle.cosD
        this.sinD = angle.sinD
    }

}