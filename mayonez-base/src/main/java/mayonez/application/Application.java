package mayonez.application;

import mayonez.*;

/**
 * An application that displays a window, receives input, and continuously updates and renders a scene.
 *
 * @author SlavSquatSuperstar
 */
public class Application {

    // Constants
    private static final float DEBUG_INTERVAL_SECS = 1f;
    private static final boolean LOG_FRAME_COUNTS = false;

    // Engine Fields
    private final Window window;
    private boolean running;

    // Time Fields (Seconds)
    private final boolean frameSkip; // Update only once per draw
    private final float renderDt; // Target render delta time
    private final float fixedDt; // Target physics delta time
    private float lastTime; // Last update time
    private float unprocessedTime; // Timer for render
    private float fixedUnprocessedTime; // Timer for physics
    private float currentDt; // Time processed this frame
    private boolean hasUpdatedThisFrame; // If window should redraw

    // Debug Info Fields
    private float debugTimer;
    private int fixedTickCount;
    private int averageFixedTPS;
    private int averageFPS;

    protected Application(Window window) {
        this.window = window;
        running = false;

        frameSkip = Preferences.getFrameSkip();
        renderDt = 1f / Preferences.getFps(); // Render
        fixedDt = 1f / Preferences.getFixedTps(); // Physics
    }

    // Main Game Loop Methods

    /**
     * Set up system resources and initialize the application.
     */
    public final void start() {
        if (!running) {
            running = true;
            window.start();
            Logger.debug("Started window");
            SceneManager.startScene();
            run();
        }
    }

    private void run() {
        lastTime = window.getCurrentTimeSecs();
        unprocessedTime = 0f;
        fixedUnprocessedTime = 0f;
        currentDt = 0f;
        debugTimer = 0f;
        averageFPS = 0;
        fixedTickCount = 0;
        int frameCount = 0;

        while (running && window.notClosedByUser()) {
            hasUpdatedThisFrame = false;
            updateGame();

            // Render as often as possible
            // TODO do frame skip here
            if (unprocessedTime >= renderDt) {
                currentDt = unprocessedTime;
                window.render();
                frameCount += 1;
                unprocessedTime = 0;
            }

            // Print frame count
            if (debugTimer >= DEBUG_INTERVAL_SECS) {
                averageFixedTPS = fixedTickCount;
                averageFPS = frameCount;
                fixedTickCount = 0;
                frameCount = 0;

                if (LOG_FRAME_COUNTS) {
                    Logger.trace("Ticks per second: %d", averageFixedTPS);
                    Logger.trace("Frames per second: %d", averageFPS);
                }
                debugTimer -= DEBUG_INTERVAL_SECS;
            }
        }
        Mayonez.stop(ExitCode.SUCCESS);
    }

    /**
     * Free system resources and quit the application.
     */
    public final void stop() {
        if (running) {
            running = false;
            window.stop();
            Logger.debug("Closed window");
        }
    }

    // Game Loop Helper Methods

    /*
     * Sources:
     * - https://gafferongames.com/post/fix_your_timestep/
     * - https://gameprogrammingpatterns.com/game-loop.html
     */
    // TODO separate physics and render rate
    // TODO call render/update and fixed update separately
    // TODO limit max physics updates per frame (replace frameskip)
    // TODO interpolate between physics frames
    // TODO class for elapsed timers and last/curr times
    private void updateGame() {
        // Calculate frame time
        var currentTime = window.getCurrentTimeSecs();
        var frameElapsedTime = currentTime - lastTime; // Time since last update
        unprocessedTime += frameElapsedTime;
        fixedUnprocessedTime += frameElapsedTime;
        debugTimer += frameElapsedTime;
        lastTime = currentTime; // Reset last time

        while (fixedUnprocessedTime > fixedDt) { // Always update with fixed delta-t
            window.beginFrame();
            SceneManager.updateScene(fixedDt); // TODO should be fixed update
            window.endFrame();
            fixedTickCount += 1;

            fixedUnprocessedTime -= fixedDt;
            hasUpdatedThisFrame = true;

            if (!frameSkip) break;
        }
    }

    // Time Getters

    /**
     * Get the duration of the current frame in seconds, dt.
     *
     * @return the FPS now
     */
    public float getDeltaTime() {
        return currentDt;
    }

    /**
     * Get the updates per second by the application at the current frame, equal
     * to 1/dt rounded to the nearest integer.
     *
     * @return the FPS now
     */
    public int getFPS() {
        return Math.round(1f / currentDt);
    }

    /**
     * Get the number of game updates by the application in the last second.
     *
     * @return the average update FPS
     */
    public int getFixedTPS() {
        return averageFixedTPS;
    }

    /**
     * Get the number of frames drawn by the application in the last second.
     *
     * @return the average render FPS
     */
    public int getRenderFPS() {
        return averageFPS;
    }

    // Object Overrides

    @Override
    public String toString() {
        return String.format("Application %s", running ? "running" : "not running");
    }

}
