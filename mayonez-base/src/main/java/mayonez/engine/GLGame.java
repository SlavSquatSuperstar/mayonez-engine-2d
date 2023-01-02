package mayonez.engine;

import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * An instance of this game using OpenGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLGame extends GameEngine { // can't implement runnable otherwise GLFW will crash

    public GLGame() {
        super(new GLWindow(String.format("%s %s (GL)", Preferences.getTitle(), Preferences.getVersion()),
                Preferences.getScreenWidth(), Preferences.getScreenHeight()));
    }

    // Game Loop Methods

    @Override
    public float getCurrentTime() {
        return (float) glfwGetTime();
    }

    @Override
    public String toString() {
        return String.format("GL Game (%s)", getRunningString());
    }
}