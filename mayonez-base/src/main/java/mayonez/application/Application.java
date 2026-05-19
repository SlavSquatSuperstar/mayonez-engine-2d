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
    private final float timeStepSecs; // Target render delta time
    private final float fixedTimeStepSecs; // Target physics delta time
    private float lastLoopTimeSecs; // Last update time
    private float unprocessedTime; // Timer for render
    private float fixedUnprocessedTime; // Timer for physics
    private float deltaTimeSecs; // Time processed this frame
    private boolean hasUpdatedThisFrame; // If window should redraw

    // Debug Info Fields
    private float debugTimerSecs;
    private int updateCount;
    private int averageUPS;
    private int averageFPS;

    protected Application(Window window) {
        this.window = window;
        running = false;

        frameSkip = Preferences.getFrameSkip();
        timeStepSecs = 1 / 60f; // Render
        fixedTimeStepSecs = 1 / 60f; // Physics
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
        lastLoopTimeSecs = window.getCurrentTimeSecs();
        unprocessedTime = 0f;
        fixedUnprocessedTime = 0f;
        deltaTimeSecs = 0f;
        debugTimerSecs = 0f;
        averageFPS = 0;
        updateCount = 0;
        int frameCount = 0;

        while (running && window.notClosedByUser()) {
            hasUpdatedThisFrame = false;
            updateGame();

            // Render as often as possible
            // TODO do frame skip here
            if (unprocessedTime >= timeStepSecs) {
                window.render();
                frameCount += 1;
                unprocessedTime = 0;
            }

            // Print frame count
            if (debugTimerSecs >= DEBUG_INTERVAL_SECS) {
                averageUPS = updateCount;
                averageFPS = frameCount;
                updateCount = 0;
                frameCount = 0;

                if (LOG_FRAME_COUNTS) {
                    Logger.trace("Updates per second: %d", averageUPS);
                    Logger.trace("Frames per second: %d", averageFPS);
                }
                debugTimerSecs -= DEBUG_INTERVAL_SECS;
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
    // TODO call updated and fixed update separately
    // TODO limit max physics updates per frame (replace frameskip)
    // TODO interpolate between physics frames
    private void updateGame() {
        // Calculate frame time
        var currentLoopTimeSecs = window.getCurrentTimeSecs();
        var frameElapsedTimeSecs = currentLoopTimeSecs - lastLoopTimeSecs; // Time since last update
        unprocessedTime += frameElapsedTimeSecs;
        fixedUnprocessedTime += frameElapsedTimeSecs;
        debugTimerSecs += frameElapsedTimeSecs;
        lastLoopTimeSecs = currentLoopTimeSecs; // Reset last time

        while (fixedUnprocessedTime > fixedTimeStepSecs) { // Always update with fixed delta-t
            deltaTimeSecs = fixedTimeStepSecs;

            window.beginFrame();
            SceneManager.updateScene(deltaTimeSecs); // TODO should be fixed update
            window.endFrame();
            updateCount += 1;

            fixedUnprocessedTime -= deltaTimeSecs;
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
        return deltaTimeSecs;
    }

    /**
     * Get the updates per second by the application at the current frame, equal
     * to 1/dt rounded to the nearest integer.
     *
     * @return the FPS now
     */
    public int getFPS() {
        return Math.round(1f / deltaTimeSecs);
    }

    /**
     * Get the number of game updates by the application in the last second.
     *
     * @return the average update FPS
     */
    public int getUpdateFPS() {
        return averageUPS;
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
