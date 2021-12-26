package slavsquatsuperstar.mayonezgl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.*;
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

    public void run() {
        // All time values are in seconds
        float lastTime = 0f; // Last time the game loop iterated
        float currentTime; // Time of current frame
        float deltaTime = 0f; // Unprocessed time since last frame

        float timer = 0f;
        int frames = 0;

        try {
            // Render to the screen until the user closes the window or pressed the ESCAPE key
            while (running && window.notClosedByUser()) {
                boolean ticked = false; // Has engine actually updated?
                currentTime = (float) glfwGetTime();
                float passedTime = currentTime - lastTime;
                deltaTime += passedTime;
                timer += passedTime;
                lastTime = currentTime;

                window.beginFrame(); // Poll input

                while (deltaTime >= Time.TIME_STEP) {
                    deltaTime -= Time.TIME_STEP;
                    update(Time.TIME_STEP);
                    ticked = true;
                }
                if (ticked) {
                    render();
                    frames++;
                }
                if (timer >= 1) {
                    Logger.log("Frames per Second: %d", frames);
                    timer = 0;
                    frames = 0;
                }
                window.endFrame();
            }
        } catch (Exception e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            Mayonez.stop(1);
        }

        Mayonez.stop(0);
    }

    public void update(float dt) {
        if (scene != null) scene.update(dt);
        physics.physicsUpdate(dt);
    }

    public void render() {
        window.render((_g2) -> {
            if (scene != null) renderer.render(_g2); // don't pass a G2D object
        });
    }

}