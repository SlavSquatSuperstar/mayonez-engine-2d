package slavsquatsuperstar.mayonez.renderer

import slavsquatsuperstar.mayonez.GameObject
import slavsquatsuperstar.mayonez.Scene
import java.awt.Graphics2D

/**
 * Draws objects in a scene to the screen.
 *
 * @author SlavSquatSuperstar
 */
interface Renderer {

    fun render(g2: Graphics2D?)

    /**
     * Submit a [GameObject] for rendering.
     *
     * @param o the game object
     */
    fun addObject(o: GameObject)

    /**
     * Remove a [GameObject] from the renderer.
     *
     * @param o the game object
     */
    fun removeObject(o: GameObject)

    fun setScene(newScene: Scene)

}