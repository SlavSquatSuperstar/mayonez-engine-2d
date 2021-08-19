package slavsquatsuperstar.mayonez

object Time {
    @JvmField
    val TIME_STARTED: Long = System.nanoTime()

    @JvmField
    val TIME_STEP: Float = 1.0f / Preferences.FPS

    /**
     * Returns the time in seconds since the program started.
     *
     * @return the duration of this program
     */
    @JvmStatic
    val time: Float
        get() = (System.nanoTime() - TIME_STARTED) / 1.0E9f
}