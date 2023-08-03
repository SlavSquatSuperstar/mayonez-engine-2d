package mayonez

import mayonez.init.*
import mayonez.io.text.*
import mayonez.util.*

/**
 * A collection of user-changeable application parameters.
 *
 * @author SlavSquatSuperstar
 */
object Preferences {

    private const val PREFS_FILENAME = "preferences.json"
    private val preferences: Record = Defaults.copyPreferences()
    private var initialized = false

    // Read Preferences Methods

    internal fun setPreferences() {
        if (!initialized) {
            preferences.setFromFile()
            Logger.debug("Loaded preferences from $PREFS_FILENAME")
            initialized = true
        }
    }

    private fun Record.setFromFile() {
        val userPrefs = JSONFile(PREFS_FILENAME).readJSON()
        userPrefs.validate(this)
        this.setFrom(userPrefs)
    }

    private fun Record.validate(defaults: Record) {
        StringValidator("title", "version", "log_directory").validate(this, defaults)
        IntValidator(240, 3840, "screen_height", "screen_width").validate(this, defaults)
        IntValidator(150, 240, "fps").validate(this, defaults)
        IntValidator(0, 5, "log_level").validate(this, defaults)
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

    // TODO effects timestep but doesn't limit render rate yet
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