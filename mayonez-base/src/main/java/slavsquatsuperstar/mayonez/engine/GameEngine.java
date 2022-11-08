package slavsquatsuperstar.mayonez.engine;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Scene;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public sealed abstract class GameEngine implements GameLayer permits JGame, GLGame{

    private boolean running = false;
    protected Scene scene;
    protected Window window;

    protected GameEngine() {
    }

    // Resource Management Methods

    /**
     * Set up system resources and initialize the application.
     */
    @Override
    public final void start() {
        if (!running) {
            running = true;
            window.start();
            startScene();
            run();
        }
    }

    /**
     * Free system resources and quit the application.
     */
    @Override
    public final void stop() {
        if (running) {
            running = false;
            scene.stop();
            window.stop();
        }
    }

    // Game Loop Methods

    public void beginFrame() {
        window.beginFrame();
    }

    // Semi-fixed time-step: https://gafferongames.com/post/fix_your_timestep/
    public final void run() {
        // All time values are in seconds
        float timeStep = Mayonez.TIME_STEP;
        float lastTime = getCurrentTime(); // Last time the game loop iterated

        // For Debugging
        float timer = 0f;
        int frames = 0;

        try {
            // Render to the screen until the user closes the window or presses the ESCAPE key
            while (running && window.notClosedByUser()) {
                boolean ticked = false; // Has the engine actually updated?

                float newTime = getCurrentTime();
                float frameTime = newTime - lastTime; // Track the time passed since last frame
                timer += frameTime;
                lastTime = newTime;  // Reset lastTime

                beginFrame();
                // Update the game as many times as possible even if the screen freezes
                while (frameTime > 0) { // Will update any leftover sliver of time
                    float deltaTime = MathUtils.min(frameTime, timeStep);
                    update(deltaTime);
                    frameTime -= deltaTime;
                    ticked = true;
                }
                // Only render if the game has updated to save resources
                if (ticked) {
                    render();
                    frames++;
                }
                // Print FPS count each second
                if (timer >= 1) {
                    Logger.debug("Engine: Frames per Second: %d", frames);
                    frames = 0;
                    timer = 0;
                }

                endFrame();
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
            Mayonez.stop(1);
        }

        Mayonez.stop(0);
    }

    /**
     * Refreshes everything in the current scene, including physics, scripts, and UI.
     *
     * @param dt seconds since the last frame
     */
    // TODO Multithread physics, set time step higher than refresh rate for smoother results
    // TODO Poll input events
    public abstract void update(float dt) throws Exception;

    /**
     * Redraws everything in the current scene, including sprites, backgrounds, and UI.
     */
    public abstract void render() throws Exception;

    public void endFrame() {
        window.endFrame();
    }

    // Getters and Setters

    public Window getWindow() {
        return window;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        startScene();
    }

    // Helper Methods

    public abstract float getCurrentTime();

    /**
     * Starts the current scene, if not null
     */
    private void startScene() {
        if (scene != null && running) {
            scene.start();
            Logger.debug("Game: Loaded scene \"%s\"", scene.getName());
        }
    }

}
