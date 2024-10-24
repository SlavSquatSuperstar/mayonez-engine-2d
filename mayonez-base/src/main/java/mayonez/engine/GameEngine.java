package mayonez.engine;

import mayonez.*;
import mayonez.input.*;

/**
 * An application that displays a window, receives input, and continuously updates and renders a scene.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class GameEngine permits JGameEngine, GLGameEngine {

    // Constants
    private static final float DEBUG_INTERVAL_SECS = 1f;
    private static final boolean LOG_FRAME_COUNTS = false;

    // Engine Fields
    private final Window window;
    private boolean running;

    // Time Fields (Seconds)
    private final boolean frameSkip;
    private final float timeStepSecs; // Target delta time
    private final float halfTimeStepSecs;
    private float lastLoopTimeSecs; // Last update time
    private float unprocessedTime;
    private float deltaTimeSecs; // Time processed this update
    private boolean hasUpdatedThisFrame; // If window should redraw

    // Debug Info Fields
    private float debugTimerSecs;
    private int updateCount;
    private int averageUPS;
    private int averageFPS;

    protected GameEngine(Window window) {
        this.window = window;
        frameSkip = Preferences.getFrameSkip();
        timeStepSecs = Time.getTimeStepSecs();
        halfTimeStepSecs = timeStepSecs * 0.5f;

        running = false;
        // Add input listeners
        window.setKeyInput(KeyInput.getInstance());
        window.setMouseInput(MouseInput.getInstance());
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
        lastLoopTimeSecs = getCurrentTimeSecs();
        unprocessedTime = 0f;
        deltaTimeSecs = 0f;
        debugTimerSecs = 0f;
        averageFPS = 0;
        updateCount = 0;
        int frameCount = 0;

        while (running && window.notClosedByUser()) {
            hasUpdatedThisFrame = false;
            updateGame();

            // Render if updated
            if (hasUpdatedThisFrame) {
                window.render();
                frameCount += 1;
            }

            // Print frame count
            if (debugTimerSecs >= DEBUG_INTERVAL_SECS) {
                averageUPS = updateCount;
                averageFPS = frameCount;
                updateCount = 0;
                frameCount = 0;

                if (LOG_FRAME_COUNTS) {
                    Logger.debug("Updates per second: %d", averageUPS);
                    Logger.debug("Frames per second: %d", averageFPS);
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
    private void updateGame() {
        // Calculate frame time
        var currentLoopTimeSecs = getCurrentTimeSecs();
        var frameElapsedTimeSecs = currentLoopTimeSecs - lastLoopTimeSecs; // Time since last update
        unprocessedTime += frameElapsedTimeSecs;
        debugTimerSecs += frameElapsedTimeSecs;
        lastLoopTimeSecs = currentLoopTimeSecs;  // Reset last time

        while (unprocessedTime > halfTimeStepSecs) { // Carry small slivers of time to next frame
            deltaTimeSecs = Math.min(unprocessedTime, timeStepSecs);

            // TODO multi-thread physics with shorter fixed time step
            window.beginFrame();
            SceneManager.updateScene(deltaTimeSecs);
            window.endFrame();
            updateCount += 1;

            unprocessedTime -= deltaTimeSecs;
            hasUpdatedThisFrame = true;

            if (!frameSkip) break;
        }
    }

    // Engine Getters

    /**
     * Get the current time of the application in seconds. The time relative to an
     * arbitrary point, which is not guaranteed to be when the program started.
     *
     * @return the time in seconds
     */
    protected abstract float getCurrentTimeSecs();

    protected final String getRunningString() {
        return running ? "running" : "not running";
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

}
