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
            StringValidator("title", "log_directory"),
            BooleanValidator("save_logs", "frame_skip"),
            IntValidator(240, 3840, "screen_height", "screen_width"),
            IntValidator(10, 250, "fps"),
            IntValidator(0, 5, "log_level")
        )
    }

    // Application

    @JvmStatic
    val title: String
        get() = getString("title")

    // Graphical

    @JvmStatic
    val screenWidth: Int
        get() = getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = getInt("screen_height")

    @JvmStatic
    val fps: Int
        get() = getInt("fps")

    /**
     * Update the game as many times as possible before rendering (fast),
     * rather than rendering once per update (slow).
     */
    @JvmStatic
    val frameSkip: Boolean
        get() = getBoolean("frame_skip")

    // Logging
    internal fun getLoggerConfig(): LoggerConfig {
        return LoggerConfig(
            getBoolean("save_logs"),
            getInt("log_level"),
            getString("log_directory")
        )
    }

}