package mayonez.input.mouse

import mayonez.input.*
import java.awt.event.*

private const val NANOS_TO_SECS = 1.0e-9

/**
 * Receives mouse input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
class JMouseManager : MouseManager() {

    // Mouse Button Callbacks

    override fun mousePressed(e: MouseEvent) {
        setButtonDown(e.button, true)
        setLastClickTimeSecs(System.nanoTime() * NANOS_TO_SECS)
        updateButtons()
        // Not relying on MouseEvent.clickCount since want to be similar to GL input
    }

    override fun mouseReleased(e: MouseEvent) {
        setButtonDown(e.button, false)
        setMouseDisp(0, 0)
        updateButtons()
    }

    // Mouse Movement Callbacks

    override fun mouseMoved(e: MouseEvent) {
        setMousePos(e.x, e.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        setMouseDisp(e.x - mousePosPx.x, e.y - mousePosPx.y)
        setMousePos(e.x, e.y)
    }

    // Mouse Scroll Callbacks

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setScrollPos(0, e.wheelRotation) // AWT only supports one direction of scroll
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

}