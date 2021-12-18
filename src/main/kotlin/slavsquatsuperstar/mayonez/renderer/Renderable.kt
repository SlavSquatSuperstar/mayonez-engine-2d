package slavsquatsuperstar.mayonez.renderer

import java.awt.Graphics2D

/**
 * Maps an object to a draw function.
 */
internal fun interface Renderable {
    fun draw(g2: Graphics2D)
    operator fun invoke(g2: Graphics2D) = draw(g2)
}