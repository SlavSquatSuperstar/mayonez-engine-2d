package mayonez.graphics.renderer

import mayonez.annotations.*
import mayonez.graphics.*
import java.awt.*

/**
 * Draws all user interface elements onto the screen using Java's AWT and
 * Swing libraries.
 *
 * This is currently a dummy implementation.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
@ExperimentalFeature
internal class JUIRenderer : UIRenderer {

    override fun clear() {
    }

    override fun render(g2: Graphics2D?) {
    }

    override fun addUIElement(r: Renderable?) {
    }

    override fun removeUIElement(r: Renderable?) {
    }

}