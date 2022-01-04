package slavsquatsuperstar.mayonez.graphics.renderer

import java.awt.Graphics2D

/**
 * Maps an object to a draw function.
 *
 * @author SlavSquatSuperstar
 */
fun interface Renderable {
    fun draw(g2: Graphics2D)
    // operator fun invoke(g2: Graphics2D) = draw(g2)
}