package mayonez.input.keyboard

import mayonez.event.*
import mayonez.graphics.*
import mayonez.input.*
import java.awt.event.*

/**
 * Receives keyboard input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
internal class JKeyManager : KeyManager() {

    // Key Callbacks

    override fun keyPressed(e: KeyEvent) {
        setKeyDown(e.keyCode, true)
        EventSystem.broadcast(KeyboardEvent(e.keyCode, true, e.modifiersEx))
//        println("${KeyEvent.getModifiersExText(e.modifiersEx)}${e.keyLocation}")
    }

    override fun keyReleased(e: KeyEvent) {
        setKeyDown(e.keyCode, false)
        EventSystem.broadcast(KeyboardEvent(e.keyCode, false, e.modifiersEx))
    }

    // Key Getters

    override fun keyDown(key: Key?): Boolean {
        return if (key == null) false
        else keyDown(key.awtCode)
    }

    override fun keyPressed(key: Key?): Boolean {
        return if (key == null) false
        else keyPressed(key.awtCode)
    }

}