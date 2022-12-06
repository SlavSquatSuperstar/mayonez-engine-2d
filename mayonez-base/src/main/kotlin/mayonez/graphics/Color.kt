package mayonez.graphics

import mayonez.math.IntMath
import mayonez.util.JColor
import org.joml.Vector3f
import org.joml.Vector4f
import java.util.*

/**
 * Stores a color used by the program and translates to and from [java.awt.Color] and [org.joml.Vector4f].
 */
class Color(red: Int, green: Int, blue: Int, alpha: Int) {

    constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, 255)
    constructor(color: JColor) : this(color.red, color.green, color.blue, color.alpha)

    constructor(v4: Vector4f) : this(v4.x.toInt(), v4.y.toInt(), v4.z.toInt(), v4.w.toInt())
    constructor(v3: Vector3f) : this(Vector4f(v3, 0f))

    // Color Properties

    /**
     * Red component of this color, between 0-255.
     */
    var r: Int = red
        set(r) {
            field = r.clamp()
        }

    /**
     * Green component of this color, between 0-255.
     */
    var g: Int = green
        set(g) {
            field = g.clamp()
        }

    /**
     * Blue component of this color, between 0-255.
     */
    var b: Int = blue
        set(b) {
            field = b.clamp()
        }

    /**
     * Alpha component of this color, between 0-255.
     */
    var a: Int = alpha
        set(a) {
            field = a.clamp()
        }

    // Conversion MEthods

    /**
     * Converts this color to an instance of [java.awt.Color] to use in the AWT engine.
     */
    fun toAWT(): JColor = JColor(r, g, b, a)

    /**
     * Converts this color to an instance of [org.joml.Vector4f] to use in the GL engine, normalizing the values between 0-1.
     */
    fun toGL(): Vector4f = Vector4f(r.norm(), g.norm(), b.norm(), a.norm())

    // Object Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Color) && (this.r == other.r) && (this.g == other.g) && (this.b == other.b) && (this.a == other.a)
    }

    override fun hashCode(): Int = Objects.hash(r, g, b, a)

    override fun toString(): String = "Color($r, $g, $b, $a)"

    // Helper Methods

    fun hexCode(alpha: Boolean): String {
        val hex = String.format("#%02x%02x%02x", r, b, g)
        return if (alpha) hex + String.format("%02x", a) else hex
    }

    companion object {
        private const val inv: Float = 1 / 255f
        private fun Int.clamp(): Int = IntMath.clamp(this, 0, 255)
        private fun Int.norm(): Float = this * inv
    }

}