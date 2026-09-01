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
    private boolean running; // Game loop is running
    private boolean quit; // Scene requested quit

    // Time Fields (Seconds)
    private final int maxTicksPerFrame; // Limit physics iterations per render
    private final float renderDt; // Target render delta time
    private final float fixedDt; // Target physics delta time
    private float currentDt; // Time processed this frame

    // Debug Info Fields
    private int averageFixedTPS;
    private int averageFPS;

    protected Application(Window window) {
        this.window = window;
        running = false;
        quit = false;

        maxTicksPerFrame = Preferences.getMaxTicksPerFrame();
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

    /*
     * Sources:
     * - https://gafferongames.com/post/fix_your_timestep/
     * - https://gameprogrammingpatterns.com/game-loop.html
     */
    // TODO separate physics and render rate
    // TODO call render/update and fixed update separately
    // TODO interpolate between physics frames
    private void run() {
        // Frame time variables
        var lastTime = window.getCurrentTimeSecs(); // Last update time
        var unprocessedTime = 0f; // Timer for render
        var fixedUnprocessedTime = 0f; // Timer for physics
        var ticksThisFrame = 0;
        currentDt = 0f;

        // Debug variables
        var debugTimer = 0f;
        var fixedTickCount = 0;
        var frameCount = 0;
        averageFPS = 0;

        while (running) {
            // Update game at constant rate
            var currentTime = window.getCurrentTimeSecs();
            var frameElapsedTime = currentTime - lastTime; // Time since last update
            unprocessedTime += frameElapsedTime;
            fixedUnprocessedTime += frameElapsedTime;
            debugTimer += frameElapsedTime;
            lastTime = currentTime; // Reset last time

            // Request to stop if window closed
            if (window.isClosedByUser()) Mayonez.stop(ExitCode.SUCCESS);

            // Always update with fixed delta-t
            while (fixedUnprocessedTime >= fixedDt && ticksThisFrame < maxTicksPerFrame) {
                SceneManager.fixedUpdateScene(fixedDt);
                fixedUnprocessedTime -= fixedDt;
                fixedTickCount += 1;
                ticksThisFrame += 1;
            }

            // Render as often as possible
            // TODO do frame skip here
            if (unprocessedTime >= renderDt) {
                currentDt = unprocessedTime;
                window.pollEvents();
                SceneManager.updateScene(currentDt);
                window.render();
                frameCount += 1;
                ticksThisFrame = 0;
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

            // Quit if scene stopped or window closed
            if (quit) running = false;
        }
        Mayonez.onStop();
    }

    /**
     * Signal the engine to stop after the current frame.
     */
    public final void requestStop() {
        if (!quit) {
            quit = true;
            Logger.debug("Application requested to stop");
        }
    }

    /**
     * Free system resources and quit the application.
     */
    public final void stop() {
        if (quit && !running) {
            window.stop();
            Logger.debug("Closed window");
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

    // Helper Class

//    private static class LoopTimer {
//        float value = 0f; // Current value
//        float limit = 0f; // Max value
//        int count = 0; // How many times reached max
//    }

}
