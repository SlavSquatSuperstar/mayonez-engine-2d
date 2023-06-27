package mayonez.graphics

import mayonez.math.*
import mayonez.util.*
import org.joml.*
import java.util.*

/**
 * Stores a color used by the program and translates to and from
 * [java.awt.Color] and [org.joml.Vector4f].
 */
// todo to/from rgb value
class Color(red: Int, green: Int, blue: Int, alpha: Int) {

    constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, 255)
    constructor(color: JColor) : this(color.red, color.green, color.blue, color.alpha)

    constructor(v4: Vector4f) : this(v4.x.toInt(), v4.y.toInt(), v4.z.toInt(), v4.w.toInt())
    constructor(v3: Vector3f) : this(Vector4f(v3, 0f))

    // Color Properties

    /** Red component of this color, between 0-255. */
    var red: Int = red
        set(red) {
            field = red.clamp()
        }

    /** Green component of this color, between 0-255. */
    var green: Int = green
        set(green) {
            field = green.clamp()
        }

    /** Blue component of this color, between 0-255. */
    var blue: Int = blue
        set(blue) {
            field = blue.clamp()
        }

    /** Alpha component of this color, between 0-255. */
    var alpha: Int = alpha
        set(alpha) {
            field = alpha.clamp()
        }

    // Conversion Methods

    /**
     * Converts this color to an instance of [java.awt.Color] to use in the AWT
     * engine.
     */
    fun toAWT(): JColor = JColor(red, green, blue, alpha)

    /**
     * Converts this color to an instance of [org.joml.Vector4f] to use in the
     * GL engine, normalizing the values between 0-1.
     */
    fun toGL(): Vector4f = Vector4f(red.norm(), green.norm(), blue.norm(), alpha.norm())

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Color) && (this.red == other.red) && (this.green == other.green)
                && (this.blue == other.blue) && (this.alpha == other.alpha)
    }

    override fun hashCode(): Int = Objects.hash(red, green, blue, alpha)

    override fun toString(): String = "Color($red, $green, $blue, $alpha)"

    // Helper Methods

    fun rgbHexCode(): String {
        return String.format("#%02x%02x%02x", red, blue, green)
    }

    fun rgbaHexCode(): String {
        return String.format("#%02x%02x%02x%02x", red, blue, green, alpha)
    }

    companion object {
        private const val inv: Float = 1 / 255f
        private fun Int.clamp(): Int = IntMath.clamp(this, 0, 255)
        private fun Int.norm(): Float = this * inv
    }

}