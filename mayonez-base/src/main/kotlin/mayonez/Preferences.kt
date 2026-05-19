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
            BooleanValidator("fullscreen", "resizable", "save_logs", "frame_skip"),
            IntValidator(240, 3840, "screen_height", "screen_width"),
            IntValidator(10, 250, "fps"),
            FloatValidator(0f, 5f, "double_click_time"),
        )
    }

    // Window

    @JvmStatic
    val title: String
        get() = getString("title")

    @JvmStatic
    val screenWidth: Int
        get() = getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = getInt("screen_height")

    @JvmStatic
    val fullscreen: Boolean
        @JvmName("isFullscreen")
        get() = getBoolean("fullscreen")

    @JvmStatic
    val resizable: Boolean
        @JvmName("isResizable")
        get() = getBoolean("resizable")

    @JvmStatic
    val fps: Int
        get() = getInt("fps")

    @JvmStatic
    val fixedTps: Int
        get() = getInt("fixed_tps")

    /**
     * Whether to update the game as many times as possible before rendering (faster),
     * rather than rendering once per update (slower).
     */
    // TODO idk what this does
    // TODO enable only frame if fixed tick
    @JvmStatic
    val frameSkip: Boolean
        get() = getBoolean("frame_skip")

    /**
     * The delay in seconds between the first and second mouse presses of a double click.
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