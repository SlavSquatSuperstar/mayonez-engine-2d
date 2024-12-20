package mayonez.input.mouse

import mayonez.input.*
import java.awt.event.*

/**
 * Receives mouse input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
class JMouseManager : MouseManager() {

    // Mouse Button Callbacks

    override fun mousePressed(e: MouseEvent) {
        if (e.button.isValidIndex()) {
            setButtonDown(e.button, true)
            pressed = true
            clicks = e.clickCount
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        if (e.button.isValidIndex()) {
            setButtonDown(e.button, false)
            pressed = false
            setMouseDisp(0, 0)
            clicks = e.clickCount
        }
    }

    // Mouse Movement Callbacks

    override fun mouseMoved(e: MouseEvent) {
        setMousePos(e.x, e.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        pressed = true
        setMouseDisp(e.x - mousePosPx.x, e.y - mousePosPx.y)
        setMousePos(e.x, e.y)
    }

    // Mouse Scroll Callbacks

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        setScrollPos(e.x, e.y)
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