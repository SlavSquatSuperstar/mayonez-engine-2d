package mayonez.engine;

import mayonez.Logger;
import mayonez.Mayonez;
import mayonez.Scene;
import mayonez.SceneManager;
import mayonez.math.FloatMath;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class GameEngine permits JGame, GLGame {

    private boolean running = false;
    protected Window window;

    protected GameEngine() {
    }

    // Resource Management Methods

    /**
     * Set up system resources and initialize the application.
     */
    public final void start() {
        if (!running) {
            running = true;
            window.start();
            SceneManager.startScene();
            run();
        }
    }

    /**
     * Free system resources and quit the application.
     */
    public final void stop() {
        if (running) {
            running = false;
            SceneManager.stopScene();
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
        // Loop
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
                    float deltaTime = FloatMath.min(frameTime, timeStep);
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

    protected Scene getScene() {
        return SceneManager.getCurrentScene();
    }

    public Window getWindow() {
        return window;
    }

    // Helper Methods

    public abstract float getCurrentTime();

}
