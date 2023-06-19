package mayonez

/**
 * A class for managing time based variables.
 *
 * @author SlavSquatSuperstar
 */
// TODO use LocalDateTime to track time?
object Time {

    // Constants
    val NANOS_STARTED: Long = System.nanoTime()
    private const val DEFAULT_TIME_STEP_SECS: Float = 1f / 60f
    private const val NANOS_TO_SECS: Float = 1e-9f

    // Static Properties
    @JvmStatic
    var timeStepSecs = DEFAULT_TIME_STEP_SECS

//    /**
//     * How sped up or slowed down the in-game time passes. A scale of 1.0 (100%
//     * speed) means the game runs in real time.
//     *
//     * @return the time scale
//     */
//    @JvmStatic
//    var timeScale = 1f
//        /**
//         * Set the time scale of the program. For performance reasons, the time
//         * scale is limited to between 0.01x to 100x.
//         *
//         * @param timeScale the percentage of real time
//         */
//        set(timeScale) {
//            field = clamp(0.01f, timeScale, 100f)
//        }

    // Methods

    /**
     * The total time in seconds since the program started.
     *
     * @return the program duration
     */
    @JvmStatic
    fun getTotalProgramSeconds(): Float {
        return (System.nanoTime() - NANOS_STARTED) * NANOS_TO_SECS
    }

}
