package mayonez;

/**
 * A class for managing time based variables.
 *
 * @author SlavSquatSuperstar
 */
// TODO use LocalDateTime to track time?
public final class Time {

    // Constants
    public static final long NANOS_STARTED = System.nanoTime();
    private static final float NANOS_TO_SECS = 1e-9f;
    public static final float DEFAULT_TIME_STEP = 1f / 60f;

    // Static Fields
    private static float timeStep = DEFAULT_TIME_STEP;
//    private static float timeScale = 1f;

    private Time() {
    }

    // Getters and Setters

    /**
     * The total time in seconds since the program started.
     *
     * @return the program duration
     */
    public static float getTotalProgramSeconds() {
        return (System.nanoTime() - NANOS_STARTED) * NANOS_TO_SECS;
    }

    public static float getTimeStep() {
        return timeStep;
    }

    static void setTimeStep(float timeStep) {
        Time.timeStep = timeStep;
    }

//    /**
//     * How sped up or slowed down the in-game time passes. A scale of 1.0 (100%
//     * speed) means the game runs in real time.
//     *
//     * @return the time scale
//     */
//    public static float getTimeScale() {
//        return timeScale;
//    }
//
//    /**
//     * Set the time scale of the program. For performance reasons, the time scale
//     * is limited to between 0.01x to 100x.
//     *
//     * @param timeScale the percentage of real time
//     */
//    public static void setTimeScale(float timeScale) {
//        Time.timeScale = FloatMath.clamp(0.01f, timeScale, 100f);
//    }

}
