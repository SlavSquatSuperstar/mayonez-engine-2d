package mayonez.input

import java.awt.event.*

/**
 * Receives mouse input events from GLFW.
 *
 * @author SlavSquatSuperstar
 */
class JMouseManager : MouseManager() {

    // Mouse Button Callbacks

    override fun mousePressed(e: MouseEvent) {
        if (e.button.isValidIndex()) {
            setButtonDown(e.button, true)
            setPressed(true)
            setClicks(e.clickCount)
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.button.isValidIndex()) {
            setButtonDown(e.button, false)
            setPressed(false)
            setMouseDisp(0, 0)
            setClicks(e.clickCount)
        }
    }

    // Mouse Movement Callbacks

    override fun mouseMoved(e: MouseEvent) {
        setMousePos(e.x, e.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        setPressed(true)
        setMouseDisp(e.x - mousePosPx.x, e.y - mousePosPx.y)
        setMousePos(e.x, e.y)
    }

    // Mouse Scroll Callbacks

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setScrollPos(e.x, e.y)
    }

}