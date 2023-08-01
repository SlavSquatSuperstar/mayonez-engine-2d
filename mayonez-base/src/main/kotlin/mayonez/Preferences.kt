package mayonez

import mayonez.init.*
import mayonez.io.text.*
import mayonez.util.*

// TODO need to validate preferences

/**
 * A collection of user-changeable application parameters.
 *
 * @author SlavSquatSuperstar
 */
object Preferences {

    private const val PREFS_FILENAME = "preferences.json"
    private val preferences: Record = Defaults.PREFERENCES.copy()
    private var initialized = false

    internal fun readFromFile() {
        if (!initialized) {
            val prefsFile = JSONFile(PREFS_FILENAME)
            preferences.addAll(prefsFile.readJSON())
            Logger.debug("Loaded preferences from $PREFS_FILENAME")
            initialized = true
        }
    }

    // Application

    @JvmStatic
    val title: String
        get() = preferences.getString("title")

    @JvmStatic
    val version: String
        get() = preferences.getString("version")

    // Graphical

    @JvmStatic
    val screenWidth: Int
        get() = preferences.getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = preferences.getInt("screen_height")

    @JvmStatic
    val fps: Int
        get() = preferences.getInt("fps")

    // Logging
    internal fun getLoggerConfig(): LoggerConfig {
        return LoggerConfig(
            preferences.getBoolean("save_logs"),
            preferences.getInt("log_level"),
            preferences.getString("log_directory")
        )
    }

}