package slavsquatsuperstar.mayonez.graphics

import slavsquatsuperstar.mayonez.Component

/**
 * A visual representation of a GameObject.
 *
 * @author SlavSquatSuperstar
 */
abstract class Sprite : Component() {
    final override fun update(dt: Float) {} // Sprites shouldn't update any game logic
    abstract fun copy(): Sprite?
}