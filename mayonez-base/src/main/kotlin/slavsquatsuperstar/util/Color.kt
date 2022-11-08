package slavsquatsuperstar.util

import org.joml.Vector4f
import slavsquatsuperstar.math.MathUtils
import java.util.*

typealias JColor = java.awt.Color

/**
 * Stores a color used by the program and converts between [java.awt.Color] and [org.joml.Vector4f]
 */
class Color(red: Int, green: Int, blue: Int, alpha: Int) {

    constructor(color: JColor) : this(color.red, color.green, color.blue, color.alpha)

    constructor(color: Vector4f) : this(color.x.toInt(), color.y.toInt(), color.z.toInt(), color.w.toInt())

    var red: Int = red
        set(red) {
            field = red.clamp()
        }
    var green: Int = green
        set(green) {
            field = green.clamp()
        }
    var blue: Int = blue
        set(blue) {
            field = blue.clamp()
        }
    var alpha: Int = alpha
        set(alpha) {
            field = alpha.clamp()
        }

    /**
     * Converts this color to an instance of [java.awt.Color] to use in the AWT engine.
     */
    fun toAWT(): JColor = JColor(red, green, blue, alpha)

    /**
     * Converts this color to an instance of [org.joml.Vector4f] to use in the GL engine, normalizing the values between 0-1.
     */
    fun toGL(): Vector4f = Vector4f(red.norm(), green.norm(), blue.norm(), alpha.norm())

    override fun equals(other: Any?): Boolean {
        return (other is Color) && (this.red == other.red) && (this.green == other.green) &&
                (this.blue == other.blue) && (this.alpha == other.alpha)
    }

    override fun hashCode(): Int = Objects.hash(red, green, blue, alpha)

    // Helper Methods

    companion object {
        private const val inv: Float = 1 / 255f
    }

    private fun Int.clamp(): Int = MathUtils.clamp(this, 0, 255)
    private fun Int.norm(): Float = this * inv

}