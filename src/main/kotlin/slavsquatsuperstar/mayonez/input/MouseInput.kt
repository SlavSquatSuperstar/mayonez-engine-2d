package slavsquatsuperstar.mayonez.input

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Game
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*

/**
 * The receiver for all mouse-related input events.
 *
 * @author SlavSquatSuperstar
 */
object MouseInput : MouseAdapter() {

    // TODO world vs screen
    // Mouse Pointer Fields (in pixels)
    var xScreen = 0
        private set
    var yScreen = 0
        private set
    var dxScreen = 0
        private set
    var dyScreen = 0
        private set

    // Mouse State Fields / Getters
    @JvmStatic
    var pressed = false
        private set

    @JvmStatic
    var dragged = false
        private set

    @JvmStatic
    var button = 0
        private set

    @JvmStatic
    var clicks = 0
        private set

    // MouseListener Method Overrides

    override fun mousePressed(e: MouseEvent) {
        pressed = true
        button = e.button
        clicks = e.clickCount
    }

    override fun mouseReleased(e: MouseEvent) {
        dragged = false
        pressed = dragged
        dyScreen = 0
        dxScreen = dyScreen
        clicks = 0
    }

    override fun mouseMoved(e: MouseEvent) {
        xScreen = e.x
        yScreen = e.y
    }

    override fun mouseDragged(e: MouseEvent) {
        dragged = true
        dxScreen = e.x - xScreen
        dyScreen = e.y - yScreen
        xScreen = e.x
        yScreen = e.y
    }

    // Mouse Button Getters

    @JvmStatic
    fun buttonDown(buttonName: String?): Boolean {
        for (b in MouseMapping.values()) if (b.toString()
                .equals(buttonName, ignoreCase = true)
        ) return pressed && button == b.button
        return false
    }

    // Mouse Pointer Getters

    @JvmStatic
    fun getX(): Float = xScreen.toFloat() / Game.currentScene().cellSize

    @JvmStatic
    fun getY(): Float = yScreen.toFloat() / Game.currentScene().cellSize

    @JvmStatic
    fun getPosition(): Vec2 = Vec2(getX(), getY())

    @JvmStatic
    fun getDx(): Float = dxScreen.toFloat() / Game.currentScene().cellSize

    @JvmStatic
    fun getDy(): Float = dyScreen.toFloat() / Game.currentScene().cellSize

    @JvmStatic
    fun getDisplacement(): Vec2 = Vec2(getDx(), getDy())

    // Enum Declaration
    internal enum class MouseMapping(var button: Int) {
        LEFT_MOUSE(MouseEvent.BUTTON1), RIGHT_MOUSE(MouseEvent.BUTTON3), MIDDLE_MOUSE(MouseEvent.BUTTON2);

        override fun toString(): String {
            // ex: MouseMapping.LEFT_MOUSE returns "left mouse"
            return name.replace('_', ' ').lowercase(Locale.getDefault())
        }
    }
}