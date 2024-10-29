package mayonez.application;

import mayonez.graphics.*;
import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * An instance of the application using OpenGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLApplication extends Application {

    public GLApplication(Window window, KeyManager keyInput, MouseManager mouse) {
        super(window, keyInput, mouse);
    }

    // Application Methods

    @Override
    public float getCurrentTimeSecs() {
        return (float) glfwGetTime();
    }

    @Override
    public String toString() {
        return String.format("GL Game (%s)", getRunningString());
    }

}