package mayonez.engine;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;

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
    private boolean hasUpdatedThisFrame;

    // Debug Info Fields
    private float debugTimerSecs;
    private int actualFramesPerSecond;

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

    // Semi-fixed time-step: https://gafferongames.com/post/fix_your_timestep/
    public final void run() {
        lastLoopTimeSecs = getCurrentTimeSecs();
        debugTimerSecs = 0f;
        actualFramesPerSecond = 0;

        while (running && window.notClosedByUser()) {
            calculateFrameTime();
            executeFrame();
            printFrameCount();
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

    private void executeFrame() {
        beginFrame();
        updateTillFrameTimeZero();
        renderIfUpdated();
        endFrame();
    }

    private void updateTillFrameTimeZero() {
        // Update the game as many times as possible even if the screen freezes
        while (frameElapsedTimeSecs > 0) { // Will update any leftover sliver of time
            var deltaTime = FloatMath.min(frameElapsedTimeSecs, timeStepSecs);
            update(deltaTime);
//            update(deltaTime * Mayonez.getTimeScale());
            frameElapsedTimeSecs -= deltaTime;
            hasUpdatedThisFrame = true;
        }
    }

    private void renderIfUpdated() {
        if (hasUpdatedThisFrame) {
            render();
            actualFramesPerSecond += 1;
        }
    }

    private void printFrameCount() {
        if (debugTimerSecs >= 1f) {
            // TODO don't debug this, make into property
            Logger.debug("Frames per Second: %d", actualFramesPerSecond);
            actualFramesPerSecond = 0;
            debugTimerSecs = 0f;
        }
    }

    // Game Engine Overrides

    final void beginFrame() {
        // TODO poll input events
        hasUpdatedThisFrame = false;
        window.beginFrame();
    }

    // TODO multi-thread physics, set time step shorter than refresh rate for smoother results
    final void update(float dt) {
        SceneManager.updateScene(dt);
    }

    final void render() {
        window.render();
    }

    final void endFrame() {
        window.endFrame();
    }

    // Getters and Setters

    public abstract float getCurrentTimeSecs();

    protected String getRunningString() {
        return running ? "running" : "not running";
    }

}
