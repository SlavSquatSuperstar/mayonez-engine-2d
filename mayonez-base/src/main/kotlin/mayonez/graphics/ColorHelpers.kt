package mayonez.graphics

import mayonez.math.*

// Constants
internal const val ALPHA_SHIFT_BITS: Int = 24
internal const val RED_SHIFT_BITS: Int = 16
internal const val GREEN_SHIFT_BITS: Int = 8
internal const val BLUE_SHIFT_BITS: Int = 0

private const val SELECT_8_BITS: Int = 0xFF
private const val NORMALIZE: Float = 1 / 255f

// Bit Shift Helper Methods
// Source java.awt.Color
internal fun Int.getSelectedBits(bits: Int) = (this shr bits) and SELECT_8_BITS
internal fun Int.shiftBitsToCombine(shiftAmount: Int) = (this and SELECT_8_BITS) shl shiftAmount

// Int Helper Methods
internal fun Int.clamp(): Int = IntMath.clamp(this, 0, 255)
internal fun Int.norm(): Float = this * NORMALIZE
internal fun Int.combine(other: Int): Int = (this * other * NORMALIZE).toInt()