package mayonez.input.mouse

import mayonez.event.*
import mayonez.input.*
import mayonez.input.events.*
import java.awt.event.*

private const val NANOS_TO_SECS = 1.0e-9

/**
 * Receives mouse input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
class JMouseManager : MouseManager(), MouseInputHandler {

    // Mouse Button Callbacks

    override fun mousePressed(e: MouseEvent) {
        onMouseInputEvent(MouseButtonEvent(e.button, true, System.nanoTime() * NANOS_TO_SECS))
        // Not relying on MouseEvent.clickCount since want to be similar to GL input
    }

    override fun mouseReleased(e: MouseEvent) {
        onMouseInputEvent(MouseButtonEvent(e.button, false))
    }

    // Mouse Movement Callbacks

    override fun mouseMoved(e: MouseEvent) {
        onMouseInputEvent(MouseMoveEvent(e.x.toFloat(), e.y.toFloat()))
    }

    override fun mouseDragged(e: MouseEvent) {
        onMouseInputEvent(MouseMoveEvent(e.x.toFloat(), e.y.toFloat()))
    }

    // Mouse Scroll Callbacks

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        // AWT only supports one direction of scroll
        onMouseInputEvent(MouseScrollEvent(0f, e.wheelRotation.toFloat()))
    }

    // Mouse Button Getters

    override fun buttonDown(button: Button?): Boolean {
        return if (button == null) false
        else buttonDown(button.awtCode)
    }

    override fun buttonPressed(button: Button?): Boolean {
        return if (button == null) false
        else buttonPressed(button.awtCode)
    }

    // Event Handler Overrides

    override fun getEventSystem(): EventSystem<MouseInputEvent> {
        return InputEvents.MOUSE_EVENTS
    }

    override fun getButtonCode(button: Button): Int = button.awtCode

}