package mayonez.engine;

import mayonez.graphics.*;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * An instance of this game using OpenGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLGameEngine extends GameEngine { // can't implement runnable otherwise GLFW will crash

    GLGameEngine() {
        super(EngineFactory.createWindow(true));
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