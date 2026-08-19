package mayonez

import mayonez.config.*

private const val PREFS_FILENAME = "preferences.json"

/**
 * A collection of user-settable application parameters.
 *
 * @author SlavSquatSuperstar
 */
// TODO make editable, need events
object Preferences : GameConfig(PREFS_FILENAME, Defaults.preferences) {

    private var initialized = false

    // Read Preferences Methods

    internal fun setPreferences() {
        if (!initialized) {
            readFromFile()
            validateUserPreferences(*getRules())
            Logger.debug("Loaded preferences from $PREFS_FILENAME")
            initialized = true
        }
    }

    private fun getRules(): Array<PreferenceValidator<*>> {
        return arrayOf(
            StringValidator("title", "log_level", "log_directory"),
            BooleanValidator("fullscreen", "resizable", "save_logs"),
            IntValidator(240, 3840, "screen_height", "screen_width"),
            IntValidator(10, 250, "fps", "fixed_tps"),
            IntValidator(1, 20, "max_ticks_per_frame"),
            FloatValidator(0f, 5f, "double_click_time"),
        )
    }

    // Window

    /**
     * The title of the window.
     */
    @JvmStatic
    val title: String
        get() = getString("title")

    /**
     * The width of the window, in pixels.
     */
    @JvmStatic
    val screenWidth: Int
        get() = getInt("screen_width")

    /**
     * The height of the window, in pixels.
     */
    @JvmStatic
    val screenHeight: Int
        get() = getInt("screen_height")

    /**
     * If the window should start in exclusive fullscreen mode.
     */
    @JvmStatic
    val fullscreen: Boolean
        @JvmName("isFullscreen")
        get() = getBoolean("fullscreen")

    /**
     * If the window may be resized by the user.
     */
    @JvmStatic
    val resizable: Boolean
        @JvmName("isResizable")
        get() = getBoolean("resizable")

    /**
     * The maximum frames per second, or the frequency of render frames and
     * non-fixed updates.
     */
    @JvmStatic
    val fps: Int
        get() = getInt("fps")

    /**
     * The maximum fixed ticks per second, or the frequency of fixed updates.
     */
    @JvmStatic
    val fixedTps: Int
        get() = getInt("fixed_tps")

    /**
     * The maximum fixed updates per render frame. The fixed ticks per second
     * will not exceed [fixedTps] but may be limited in case the rendering
     * lags behind.
     */
    @JvmStatic
    val maxTicksPerFrame: Int
        get() = getInt("max_ticks_per_frame")

    /**
     * The delay in seconds between the first and second mouse presses of a
     * double click.
     */
    @JvmStatic
    val doubleClickTime: Float
        get() = getFloat("double_click_time")

    // Logging
    internal fun getLoggerConfig(): LoggerConfig {
        return LoggerConfig(
            getBoolean("save_logs"),
            getString("log_level"),
            getString("log_directory")
        )
    }

}