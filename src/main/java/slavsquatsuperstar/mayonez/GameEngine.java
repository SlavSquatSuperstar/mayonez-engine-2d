package slavsquatsuperstar.mayonez;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.engine.Window;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;
import slavsquatsuperstar.mayonez.renderer.Renderer;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
// TODO probably better off with enums of companion objects
// TODO sealed, core package
public abstract class GameEngine {

    protected boolean running = false;

    // Game Layers
    protected Physics2D physics;
    protected Renderer renderer;
    protected Scene scene;
    protected Window window;

    protected GameEngine() {}

    // Resource Management Methods

    /**
     * Set up system resources and initialize the application
     */
    public abstract void start();

    /**
     * Free system resources and quit the application.
     */
    public abstract void stop();

    // Game Loop Methods

    public void beginFrame() {
        window.beginFrame();
    }

    public final void run() {
        // All time values are in seconds
        float lastTime = 0f; // Last time the game loop iterated
        float currentTime; // Time of current frame
        float deltaTime = 0f; // Unprocessed time since last frame

        // For Debugging
        float timer = 0f;
        int frames = 0;

        try {
            // Render to the screen until the user closes the window or presses the ESCAPE key
            while (running && window.notClosedByUser()) {
                boolean ticked = false; // Has the engine actually updated?

                currentTime = getCurrentTime();
                float passedTime = currentTime - lastTime;
                deltaTime += passedTime;
                timer += passedTime;
                lastTime = currentTime;  // Reset lastTime

                window.beginFrame();

                // Update the game as many times as possible even if the screen freezes
                while (deltaTime >= Mayonez.TIME_STEP) {
                    update(deltaTime);
                    deltaTime -= Mayonez.TIME_STEP;
                    ticked = true;
                }
                // Only render if the game has updated to save resources
                if (ticked) {
                    render();
                    frames++;
                }
                // Print FPS count each second
                if (timer >= 1) {
                    Logger.trace("Frames per Second: %d", frames);
                    frames = 0;
                    timer = 0;
                }

                window.endFrame();
            }
        } catch (Exception e) {
            Logger.warn(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            Mayonez.stop(1);
        }

        Mayonez.stop(0);
    }

    /**
     * Refreshes all objects in the current scene.
     *
     * @param dt seconds since the last frame
     */
    public abstract void update(float dt) throws Exception;

    /**
     * Redraws all objects in the current scene.
     */
    public abstract void render() throws Exception;

    public void endFrame() {
        window.endFrame();
    }

    // Scene Methods

    /**
     * Starts the current scene, if not null
     */
    protected void startScene() {
        if (scene != null && running) {
            scene.start();
            physics.setScene(scene);
            renderer.setScene(scene);
            Logger.trace("Game: Loaded scene \"%s\"", scene.getName());
        }
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
        startScene();
    }

    // Helper Methods

    public abstract float getCurrentTime();

}
