package mayonez.engine;

import mayonez.*;
import mayonez.input.*;

/**
 * An application that manages the window and input and continuously updates and renders the game.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class GameEngine permits JGameEngine, GLGameEngine {

    // Static Fields
    private static final float DEBUG_INTERVAL_SECS = 1f;

    // Engine Fields
    private final Window window;
    private boolean running;

    // Time Fields (Seconds)
    private final float timeStepSecs; // Target delta time
    private final float halfTimeStepSecs;
    private float lastLoopTimeSecs; // Last update time
    private float frameElapsedTimeSecs; // Time since last update
    private float unprocessedTime;
    private float deltaTimeSecs; // Time processed this update
    private boolean hasUpdatedThisFrame; // If window should redraw

    // Debug Info Fields
    private float debugTimerSecs;
    private int updateCount;
    private int averageUPS;
    private int frameCount;
    private int averageFPS;

    protected GameEngine(Window window) {
        this.window = window;
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
        frameElapsedTimeSecs = 0f;
        unprocessedTime = 0f;
        deltaTimeSecs = 0f;
        debugTimerSecs = 0f;
        averageFPS = 0;
        updateCount = 0;
        frameCount = 0;

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

                // TODO don't spam this, make into getter
                Logger.log("Updates per second: %d", averageUPS);
                Logger.log("Frames per second: %d", averageFPS);

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
            SceneManager.stopScene();
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
        frameElapsedTimeSecs = currentLoopTimeSecs - lastLoopTimeSecs;
        debugTimerSecs += frameElapsedTimeSecs;
        lastLoopTimeSecs = currentLoopTimeSecs;  // Reset last time

        // Update the game as many times as possible even if the screen freezes
        while (frameElapsedTimeSecs > 0f) { // Will update any leftover sliver of time
            deltaTimeSecs = Math.min(frameElapsedTimeSecs, timeStepSecs);
            Logger.log("dt = %.4f", deltaTimeSecs);

            // TODO multi-thread physics with shorter fixed time step
            window.beginFrame();
            SceneManager.updateScene(deltaTimeSecs);
            window.endFrame();
            updateCount += 1;

            frameElapsedTimeSecs -= deltaTimeSecs;
            hasUpdatedThisFrame = true;
        }
    }

    // Getters and Setters

    /**
     * Get the current time of the application. May or may not be relative to
     * when the program started.
     *
     * @return the time in seconds
     */
    protected abstract float getCurrentTimeSecs();

    protected final String getRunningString() {
        return running ? "running" : "not running";
    }

    // Time Getters
    // TODO move to time

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
