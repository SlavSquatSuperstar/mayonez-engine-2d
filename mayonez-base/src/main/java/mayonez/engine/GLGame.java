package mayonez.engine;

import mayonez.Preferences;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * An instance of this game using OpenGL's GLFW and OpenGL libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLGame extends GameEngine { // can't implement runnable otherwise GLFW will crash

    public GLGame() {
        window = new GLWindow(String.format("%s %s (GL)", Preferences.getTitle(), Preferences.getVersion()),
                Preferences.getScreenWidth(), Preferences.getScreenHeight());
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);
    }

    // Game Loop Methods

    @Override
    public void update(float dt) throws Exception {
        if (getScene() != null) getScene().update(dt);
    }

    @Override
    public void render() throws Exception {
        window.render((_args) -> {
            if (getScene() != null) getScene().render(null); // don't pass a G2D object
        });
    }

    @Override
    public float getCurrentTime() {
        return (float) glfwGetTime();
    }

    @Override
    public String toString() {
        return "GL Game";
    }
}