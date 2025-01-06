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
class JMouseManager : MouseAdapter(), MouseInputHandler {

    // Mouse Button Callbacks

    override fun mousePressed(e: MouseEvent) {
        eventSystem.broadcast(MouseButtonEvent(e.button, true, System.nanoTime() * NANOS_TO_SECS))
        // Not relying on MouseEvent.clickCount since want to be similar to GL input
        // Can query double click interval with AWT, but not with GLFW
        // Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval")
    }

    override fun mouseReleased(e: MouseEvent) {
        eventSystem.broadcast(MouseButtonEvent(e.button, false))
    }

    // Mouse Movement Callbacks

    override fun mouseMoved(e: MouseEvent) {
        eventSystem.broadcast(MouseMoveEvent(e.x.toFloat(), e.y.toFloat()))
    }

    override fun mouseDragged(e: MouseEvent) {
        eventSystem.broadcast(MouseMoveEvent(e.x.toFloat(), e.y.toFloat()))
    }

    // Mouse Scroll Callbacks

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        // AWT only supports one direction of scroll
        eventSystem.broadcast(MouseScrollEvent(0f, e.wheelRotation.toFloat()))
    }

    // Event Handler Overrides

    override fun getEventSystem(): EventSystem<MouseInputEvent> = InputEvents.MOUSE_EVENTS

    override fun getButtonCode(button: Button): Int = button.awtCode

}