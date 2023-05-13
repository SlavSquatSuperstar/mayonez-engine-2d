package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * An instance of this game using OpenGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLGameEngine extends GameEngine { // can't implement runnable otherwise GLFW will crash

    GLGameEngine() {
        super(new GLWindow(String.format("%s %s (GL)", Preferences.getTitle(), Preferences.getVersion()),
                Preferences.getScreenWidth(), Preferences.getScreenHeight()));
    }

    // Game Loop Methods

    @Override
    public float getCurrentTimeSecs() {
        return (float) glfwGetTime();
    }

    @Override
    public String toString() {
        return String.format("GL Game (%s)", getRunningString());
    }
}