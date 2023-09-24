package mayonez

import mayonez.init.*
import mayonez.util.*

private const val PREFS_FILENAME = "preferences.json"

/**
 * A collection of user-settable application parameters.
 *
 * @author SlavSquatSuperstar
 */
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
            StringValidator("title", "version", "log_directory"),
            IntValidator(240, 3840, "screen_height", "screen_width"),
            IntValidator(10, 250, "fps"),
            IntValidator(0, 5, "log_level")
        )
    }

    // Application

    @JvmStatic
    val title: String
        get() = getString("title")

    @JvmStatic
    val version: String
        get() = getString("version")

    // Graphical

    @JvmStatic
    val screenWidth: Int
        get() = getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = getInt("screen_height")

    // TODO effects timestep but doesn't limit render rate yet
    @JvmStatic
    val fps: Int
        get() = getInt("fps")

    // Logging
    internal fun getLoggerConfig(): LoggerConfig {
        return LoggerConfig(
            getBoolean("save_logs"),
            getInt("log_level"),
            getString("log_directory")
        )
    }

}