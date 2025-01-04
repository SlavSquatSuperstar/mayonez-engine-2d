package mayonez.input.keyboard

import mayonez.event.*
import mayonez.graphics.*
import mayonez.input.*
import mayonez.input.events.*
import java.awt.event.*

/**
 * Receives keyboard input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
internal class JKeyManager : KeyAdapter(), KeyInputHandler {

    // Key Callbacks

    override fun keyPressed(e: KeyEvent) {
        eventSystem.broadcast(KeyInputEvent(e.keyCode, true))
    }

    override fun keyReleased(e: KeyEvent) {
        eventSystem.broadcast(KeyInputEvent(e.keyCode, false))
    }

    // Event Handler Overrides

    override fun getEventSystem(): EventSystem<KeyInputEvent> {
        return InputEvents.KEYBOARD_EVENTS
    }

    override fun getKeyCode(key: Key): Int = key.awtCode

}