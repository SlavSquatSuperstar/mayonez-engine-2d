package mayonez.math

import java.lang.Math.toDegrees
import java.lang.Math.toRadians

/**
 * An object representing an angle or other rotational quantity, which can be converted between degrees and radians.
 *
 * @author SlavSquatSuperstar
 */
class Angle private constructor(private var value: Double) { // most angle functions take radian values as doubles

    // Measure Methods

    val radians: Float
        get() = value.toFloat()
    val degrees: Float
        get() = toDegrees(value).toFloat()
    val sin: Float
        get() = kotlin.math.sin(value).toFloat()
    val cos: Float
        get() = kotlin.math.cos(value).toFloat()

    // Addition Methods

    operator fun plus(angle: Angle): Angle = Angle(this.value + angle.value)
    fun add(angle: Angle): Angle = this + angle

    fun addDegrees(degrees: Float): Angle = Angle(this.value + degrees.toDoubleRadians())
    fun addRadians(radians: Float): Angle = Angle(this.value + radians.toDouble())

    companion object {
        @JvmStatic
        fun createDegrees(degrees: Float): Angle = Angle(degrees.toDoubleRadians())

        @JvmStatic
        fun createRadians(radians: Float): Angle = Angle(radians.toDouble())

        private fun Float.toDoubleRadians(): Double {
            return toRadians(this.toDouble())
        }
    }

}