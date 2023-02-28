package mayonez.engine;

import mayonez.*;
import mayonez.input.*;
import mayonez.math.*;

/**
 * The application that contains the engine's core loop.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class GameEngine permits JGame, GLGame {

    private static final float TIME_STEP_SECS = Mayonez.TIME_STEP;

    // Time Fields
    private float lastTimeSecs; // Last time the game loop iterated
    private float frameTimeSecs; // Time elapsed since last time
    private boolean hasUpdated;

    // Debugging Fields
    float timerSecs = 0f;
    int frames = 0;

    // Engine Fields
    private final Window window;
    private boolean running;

    protected GameEngine(Window window) {
        this.window = window; // Set up window
        running = false;
        // Add input listeners
        window.setKeyInput(KeyInput.INSTANCE);
        window.setMouseInput(MouseInput.INSTANCE);
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

    // Main Game Loop

    // Semi-fixed time-step: https://gafferongames.com/post/fix_your_timestep/
    public final void run() {
        lastTimeSecs = getCurrentTimeSecs();
        timerSecs = 0f;
        frames = 0;

        // Render to the screen until the user closes the window or presses the ESCAPE key
        while (running && window.notClosedByUser()) {
            calculateFrameTime();

            beginFrame();
            updateTillFrameTimeZero();
            renderIfUpdated();
            endFrame();

            printFrameCount();
        }

        Mayonez.stop(0);
    }

    private void calculateFrameTime() {
        // Time of current frame
        var newTimeSecs = getCurrentTimeSecs();
        frameTimeSecs = newTimeSecs - lastTimeSecs;
        timerSecs += frameTimeSecs;
        lastTimeSecs = newTimeSecs;  // Reset last time
    }

    private void updateTillFrameTimeZero() {
        // Update the game as many times as possible even if the screen freezes
        while (frameTimeSecs > 0) { // Will update any leftover sliver of time
            var deltaTime = FloatMath.min(frameTimeSecs, TIME_STEP_SECS);
            update(deltaTime);
//            update(deltaTime * Mayonez.getTimeScale());
            frameTimeSecs -= deltaTime;
            hasUpdated = true;
        }
    }

    private void printFrameCount() {
        if (timerSecs >= 1f) {
            Logger.debug("Frames per Second: %d", frames);
            frames = 0;
            timerSecs = 0f;
        }
    }

    private void renderIfUpdated() {
        if (hasUpdated) {
            render();
            frames++;
        }
    }

    // Game Loop Methods

    final void beginFrame() {
        // TODO poll input events
        hasUpdated = false;
        window.beginFrame();
    }

    // TODO Multi-thread physics, set time step higher than refresh rate for smoother results

    final void update(float dt) {
        getScene().update(dt);
    }

    final void render() {
        window.render(getScene());
    }

    final void endFrame() {
        window.endFrame();
    }

    // Getters and Setters

    public abstract float getCurrentTimeSecs();

    private Scene getScene() {
        return SceneManager.getCurrentScene();
    }

    protected String getRunningString() {
        return running ? "running" : "not running";
    }

    public Window getWindow() {
        return window;
    }

}
