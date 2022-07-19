package slavsquatsuperstar.mayonez.engine;

import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.graphics.renderer.GLRenderer;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.Physics;

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

        renderer = new GLRenderer();
        physics = new Physics();
    }

    // Game Loop Methods

    @Override
    public void update(float dt) throws Exception {
        if (scene != null) scene.update(dt);
        physics.physicsUpdate(dt);
    }

    @Override
    public void render() throws Exception {
        window.render((_g2) -> {
            if (scene != null) renderer.render(_g2); // don't pass a G2D object
        });
    }

    @Override
    public float getCurrentTime() {
        return (float) glfwGetTime();
    }
}