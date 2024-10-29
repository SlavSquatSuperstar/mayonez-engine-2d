package mayonez.engine;

import mayonez.graphics.*;
import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * An instance of this game using OpenGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLGameEngine extends GameEngine {

    public GLGameEngine(Window window, KeyManager keyInput, MouseManager mouse) {
        super(window, keyInput, mouse);
    }

    // Game Engine Methods

    @Override
    public float getCurrentTimeSecs() {
        return (float) glfwGetTime();
    }

    @Override
    public String toString() {
        return String.format("GL Game (%s)", getRunningString());
    }

}