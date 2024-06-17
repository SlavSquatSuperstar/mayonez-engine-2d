package mayonez.engine;

import mayonez.*;
import mayonez.input.*;

/**
 * An application that manages the window and input and continuously updates and renders the game.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class GameEngine permits JGameEngine, GLGameEngine {

    // Engine Fields
    private final Window window;
    private boolean running;

    // Time Fields
    private final float timeStepSecs;
    private float lastLoopTimeSecs;
    private float frameElapsedTimeSecs;
    private float deltaTimeSecs;
    private boolean hasUpdatedThisFrame;

    // Debug Info Fields
    private float debugTimerSecs;
    private int framesPerSecond;

    protected GameEngine(Window window) {
        this.window = window;
        timeStepSecs = Time.getTimeStepSecs();
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
        deltaTimeSecs = 0f;
        debugTimerSecs = 0f;
        framesPerSecond = 0;
        int frameCount = 0;

        while (running && window.notClosedByUser()) {
            hasUpdatedThisFrame = false;
            calculateFrameTime();
            updateTillFrameTimeZero(); // TODO re-poll input every update

            // Render if updated
            if (hasUpdatedThisFrame) {
                window.render();
                frameCount += 1;
            }

            // Print frame count every second
            if (debugTimerSecs >= 1f) {
                framesPerSecond = frameCount;
                // TODO don't spam this, make into getter
                Logger.debug("Frames per Second: %d", framesPerSecond);
                frameCount = 0;
                debugTimerSecs = 0f;
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

    private void calculateFrameTime() {
        var currentLoopTimeSecs = getCurrentTimeSecs();
        // Track time since last frame
        frameElapsedTimeSecs = currentLoopTimeSecs - lastLoopTimeSecs;
        debugTimerSecs += frameElapsedTimeSecs;
        lastLoopTimeSecs = currentLoopTimeSecs;  // Reset last time
    }

    // Semi-fixed time-step: https://gafferongames.com/post/fix_your_timestep/
    private void updateTillFrameTimeZero() {
        // Update the game as many times as possible even if the screen freezes
        // TODO accumulate leftover slivers of time into next frame
        while (frameElapsedTimeSecs > 0) { // Will update any leftover sliver of time
            deltaTimeSecs = Math.min(frameElapsedTimeSecs, timeStepSecs);
            window.beginFrame();

            // TODO multi-thread physics with shorter time step for smoother results
            SceneManager.updateScene(deltaTimeSecs);
//            SceneManager.updateScene(deltaTimeSecs * Mayonez.getTimeScale());

            window.endFrame();
            frameElapsedTimeSecs -= deltaTimeSecs;
            hasUpdatedThisFrame = true;
        }
    }


    final void endFrame() {
        window.endFrame();
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

}
