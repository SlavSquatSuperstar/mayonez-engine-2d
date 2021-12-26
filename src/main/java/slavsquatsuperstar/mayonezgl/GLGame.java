package slavsquatsuperstar.mayonezgl;

import slavsquatsuperstar.mayonez.GameEngine;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonezgl.renderer.GLRenderer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public class GLGame extends GameEngine { // can't implement runnable otherwise GLFW will crash
    
    public GLGame() {
        if (!Mayonez.INSTANCE.getINIT_PREFERENCES()) Mayonez.init();

        window = new GLWindow("Mayonez + LWJGL", Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT);
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);

        renderer = new GLRenderer();
        physics = new Physics2D();
    }

    // Resource Management

    @Override
    public void start() {
        if (running) return; // Don't start the game if already running

        running = true;
        Logger.log("Engine: Starting %s %s", Preferences.TITLE, Preferences.VERSION);

        Logger.log("Engine: Loading assets");
        Assets.scanResources("assets"); // Load all game assets

        window.start();
        startScene();
        run();
    }

    @Override
    public void stop() {
        if (!running) return;
        running = false;
        window.stop();
    }

    // Game Loop Methods

    public void update(float dt) throws Exception {
        if (scene != null) scene.update(dt);
        physics.physicsUpdate(dt);
    }

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