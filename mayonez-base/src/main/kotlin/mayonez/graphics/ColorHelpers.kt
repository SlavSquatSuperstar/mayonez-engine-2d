package mayonez.graphics

import mayonez.math.*
import org.joml.*

// Type Aliases

/** The [java.awt.Color] class defined by the JDK. */
internal typealias JColor = java.awt.Color
/** The [org.joml.Vector4f] class defined by JOML. */
internal typealias GLColor = Vector4f
/** The [mayonez.graphics.Color] class defined by Mayonez Engine. */
internal typealias MColor = Color

// Normalize Constants
internal const val MAX_COMPONENT_VALUE: Int = 255
private const val NORMALIZE: Float = 1f / MAX_COMPONENT_VALUE

// Bit Shift Constants
internal const val ALPHA_SHIFT_BITS: Int = 24
internal const val RED_SHIFT_BITS: Int = 16
internal const val GREEN_SHIFT_BITS: Int = 8
internal const val BLUE_SHIFT_BITS: Int = 0
private const val SELECT_8_BITS: Int = 0xFF

// Bit Shift Helper Methods
// Source java.awt.Color
internal fun Int.getSelectedBits(bits: Int) = (this shr bits) and SELECT_8_BITS
internal fun Int.shiftBitsToCombine(shiftAmount: Int) = (this and SELECT_8_BITS) shl shiftAmount

// Int Helper Methods
internal fun Int.clamp(): Int = MathUtils.clamp(this, 0, MAX_COMPONENT_VALUE)
internal fun Int.norm(): Float = this * NORMALIZE
internal fun Int.combine(other: Int): Int = (this * other * NORMALIZE).toInt()

// Conversion Helper Methods

/**
 * Convert this color to an instance of [java.awt.Color] to use in the AWT
 * engine.
 */
internal fun MColor.toAWT(): JColor = JColor(red, green, blue, alpha)

/**
 * Convert this color to an instance of [org.joml.Vector4f] to use in the
 * GL engine, normalizing the values to between 0.0-1.0.
 */
internal fun MColor.toGL(): GLColor = GLColor(red.norm(), green.norm(), blue.norm(), alpha.norm())

// Needed for GLQuad
object ColorHelpers {
    @JvmStatic
    @JvmName("toGLColor")
    internal fun toGLColor(color: MColor): GLColor = color.toGL()
}