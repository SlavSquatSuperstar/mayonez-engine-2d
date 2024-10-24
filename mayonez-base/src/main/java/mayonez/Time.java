package mayonez;

import mayonez.math.*;

import java.time.LocalDateTime;

/**
 * Stores time properties and tracks the time since the application started.
 *
 * @author SlavSquatSuperstar
 */
public final class Time {

    // Constants
    private static final float DEFAULT_TIME_STEP_SECS = 1f / 60f;
    private static final float NANOS_TO_SECS = 1e-9f;

    // Start Time Properties
    private static long startupTimeNanos;
    private static LocalDateTime startupDateTime;

    // Properties
    private static float timeStepSecs = DEFAULT_TIME_STEP_SECS;
    private static float timeScale = 1f;

    private Time() {
    }

    // Start Time Methods

    /**
     * Get the current time since the application started in seconds.
     *
     * @return the time in seconds
     */
    public static float getTotalProgramSeconds() {
        return (System.nanoTime() - startupTimeNanos) * NANOS_TO_SECS;
    }

    static LocalDateTime getStartupDateTime() {
        return startupDateTime;
    }

    static void startTrackingTime() {
        startupTimeNanos = System.nanoTime();
        startupDateTime = LocalDateTime.now();
    }

    // Time Property Methods

    /**
     * The target time step of the application, or the duration between
     * updates, in seconds.
     *
     * @return the time step
     */
    public static float getTimeStepSecs() {
        return timeStepSecs;
    }

    static void setTimeStepSecs(float timeStepSecs) {
        Time.timeStepSecs = timeStepSecs;
    }

    /**
     * How sped up or slowed down the in-game time passes. A scale of 1.0 (100%
     * speed) means the game runs in real time.
     *
     * @return the time scale
     */
    public static float getTimeScale() {
        return timeScale;
    }

    /**
     * Set the time scale of the program. For performance reasons, the time
     * scale is limited to between 0.01x to 100x.
     *
     * @param timeScale the percentage of real time
     */
    public static void setTimeScale(float timeScale) {
        Time.timeScale = MathUtils.clamp(timeScale, 0.01f, 100f);
    }

    // TODO FPS Methods

}
