package slavsquatsuperstar.mayonezgl

import org.lwjgl.glfw.GLFW.*

object KeyInputGL {

    private val keysPressed = BooleanArray(350)
    private val keysHeld = BooleanArray(350)

    @JvmStatic
    fun endFrame() {
        keysPressed.fill(element = false)
    }

    @JvmStatic
    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (action == GLFW_PRESS) {
            keysHeld[key] = true
            keysPressed[key] = true
        } else if (action == GLFW_RELEASE) {
            keysHeld[key] = false
            keysPressed[key] = false
        }
    }

    // Getters

    @JvmStatic
    fun isKeyPressed(keyCode: Int): Boolean = keysPressed[keyCode] // only happens once

    @JvmStatic
    fun isKeyHeld(keyCode: Int): Boolean = keysHeld[keyCode] // continuously held

}