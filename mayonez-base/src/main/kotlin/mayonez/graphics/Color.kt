package mayonez.graphics

import java.util.*

/**
 * Stores an immutable color used by the program and translates to and from
 * [java.awt.Color] and [org.joml.Vector4f]. Colors have red, green, blue,
 * and alpha components between 0-255.
 *
 * @constructor Construct a color from red, green, blue, and alpha values
 *     between 0-255.
 */
class Color(red: Int, green: Int, blue: Int, alpha: Int) {

    // Constructors

    /**
     * Construct a color from red, green, and blue values between 0-255, with
     * alpha set to 255.
     */
    constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, MAX_COMPONENT_VALUE)

    /**
     * Construct a color with the same red, green, and blue values as another,
     * with an alpha value between 0-255.
     */
    constructor(color: MColor, alpha: Int) : this(color.red, color.green, color.blue, alpha)

    /**
     * Construct a color from a single combined RGBA value, between 0 - 2^32-1.
     *
     * Source: [java.awt.Color].getRed(), getGreen(), getBlue(), getAlpha()
     */
    constructor(rgba: Int) : this(
        rgba.getSelectedBits(RED_SHIFT_BITS),
        rgba.getSelectedBits(GREEN_SHIFT_BITS),
        rgba.getSelectedBits(BLUE_SHIFT_BITS),
        rgba.getSelectedBits(ALPHA_SHIFT_BITS)
    )

    companion object {
        /**
         * Construct a grayscale color from a single R/G/B value, between 0-255.
         *
         * @param value the grayscale value
         * @return the grayscale color
         */
        @JvmStatic
        fun grayscale(value: Int): Color = Color(value, value, value)

        /**
         * Construct a grayscale color from a single R/G/B value and an alpha
         * value, between 0-255.
         *
         * @param value the grayscale value
         * @param value the alpha value
         * @return the grayscale color
         */
        @JvmStatic
        fun grayscale(value: Int, alpha: Int): Color = Color(value, value, value, alpha)
    }

    // Color Properties

    /** The red component of this color, between 0-255. */
    val red: Int = red.clamp()

    /** The green component of this color, between 0-255. */
    val green: Int = green.clamp()

    /** The blue component of this color, between 0-255. */
    val blue: Int = blue.clamp()

    /** The alpha (transparency) component of this color, between 0-255. */
    val alpha: Int = alpha.clamp()

    /** The normalized red component of this color, between 0.0-1.0. */
    val fRed: Float
        get() = this.red.norm()

    /** The normalized green component of this color, between 0.0-1.0. */
    val fGreen: Float
        get() = this.green.norm()

    /** The normalized blue component of this color, between 0.0-1.0. */
    val fBlue: Float
        get() = this.blue.norm()

    /** The normalized alpha component of this color, between 0.0-1.0. */
    val fAlpha: Float
        get() = this.alpha.norm()

    // Color Value Methods

    /**
     * Get the combined RGBA value as an integer, with each component taking up
     * 8 bits. From most to least significant, the order is alpha, red, green,
     * and blue.
     *
     * Source: [java.awt.Color].Color(int r, int g, int b, int a)
     *
     * @return the RGBA value
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
    fun combine(color: MColor): MColor {
        return MColor(
            this.red.combine(color.red),
            this.green.combine(color.green),
            this.blue.combine(color.blue),
            this.alpha.combine(color.alpha)
        )
    }

    // Color Code Methods

    /**
     * Return the 6-digit HTML hex code representing the RGB values without the
     * number sign #.
     *
     * @return the hex code
     */
    fun rgbHexCode(): String {
        return String.format("%02x%02x%02x", red, green, blue)
    }

    /**
     * Return the 8-digit HTML hex code representing the RGBA values without
     * the number sign.
     *
     * @return the hex code
     */
    fun rgbaHexCode(): String {
        return String.format("%02x%02x%02x%02x", red, green, blue, alpha)
    }

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Color) && (this.red == other.red) && (this.green == other.green)
                && (this.blue == other.blue) && (this.alpha == other.alpha)
    }

    override fun hashCode(): Int = Objects.hash(red, green, blue, alpha)

    override fun toString(): String = "Color($red, $green, $blue, $alpha)"

}