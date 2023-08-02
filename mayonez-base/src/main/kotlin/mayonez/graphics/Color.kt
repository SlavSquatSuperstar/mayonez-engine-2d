package mayonez.graphics

import mayonez.util.*
import org.joml.*
import java.util.*

/**
 * Stores an immutable color used by the program and translates to and from
 * [java.awt.Color] and [org.joml.Vector4f].
 */
class Color(red: Int, green: Int, blue: Int, alpha: Int) {

    /** Construct a color from a red, green, and blue values. */
    constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, 255)

    /**
     * Construct a color from a single combined RGBA value.
     *
     * Source: [java.awt.Color].getRed(), getGreen(), getBlue(), getAlpha()
     */
    constructor(rgba: Int) : this(
        rgba.getSelectedBits(RED_SHIFT_BITS),
        rgba.getSelectedBits(GREEN_SHIFT_BITS),
        rgba.getSelectedBits(BLUE_SHIFT_BITS),
        rgba.getSelectedBits(ALPHA_SHIFT_BITS)
    )

    constructor(color: JColor) : this(color.red, color.green, color.blue, color.alpha)

    constructor(vec4: Vector4f) : this(vec4.x.toInt(), vec4.y.toInt(), vec4.z.toInt(), vec4.w.toInt())

    // Color Properties

    /** Red component of this color, between 0-255. */
    val red: Int = red.clamp()

    /** Green component of this color, between 0-255. */
    val green: Int = green.clamp()

    /** Blue component of this color, between 0-255. */
    val blue: Int = blue.clamp()

    /** Alpha component of this color, between 0-255. */
    val alpha: Int = alpha.clamp()

    // Conversion Methods

    /**
     * Converts this color to an instance of [java.awt.Color] to use in the AWT
     * engine.
     */
    fun toAWT(): JColor = JColor(red, green, blue, alpha)

    /**
     * Converts this color to an instance of [org.joml.Vector4f] to use in the
     * GL engine, normalizing the values to between 0-1.
     */
    fun toGL(): Vector4f = Vector4f(red.norm(), green.norm(), blue.norm(), alpha.norm())

    // Color Value Methods

    /**
     * Get the combined RGBA value as an integer, with each component taking up
     * 8 bits. From most to least significant, the order is alpha, red, green,
     * and blue.
     *
     * Source: [java.awt.Color].Color(int r, int g, int b, int a)
     *
     * @return the RGB value
     */
    fun getRGBAValue(): Int {
        return alpha.shiftBitsToCombine(ALPHA_SHIFT_BITS) or
                red.shiftBitsToCombine(RED_SHIFT_BITS) or
                green.shiftBitsToCombine(GREEN_SHIFT_BITS) or
                blue.shiftBitsToCombine(BLUE_SHIFT_BITS)
    }

    /**
     * Combine this color with another, multiplying the values for all
     * components.
     *
     * @param color the other color
     * @return the combined color
     */
    internal fun combine(color: MColor): MColor {
        return MColor(
            this.red.combine(color.red),
            this.green.combine(color.green),
            this.blue.combine(color.blue),
            this.alpha.combine(color.alpha)
        )
    }

    // Color Code Methods

    fun rgbHexCode(): String {
        return String.format("#%02x%02x%02x", red, green, blue)
    }

    fun rgbaHexCode(): String {
        return String.format("#%02x%02x%02x%02x", red, green, blue, alpha)
    }

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Color) && (this.red == other.red) && (this.green == other.green)
                && (this.blue == other.blue) && (this.alpha == other.alpha)
    }

    override fun hashCode(): Int = Objects.hash(red, green, blue, alpha)

    override fun toString(): String = "Color($red, $green, $blue, $alpha)"

}