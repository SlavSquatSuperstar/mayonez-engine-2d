package mayonez

import mayonez.init.*
import mayonez.util.*

/**
 * The default, hardcoded settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults {

    // Application
    private const val TITLE: String = "<No Title>"
    private const val VERSION: String = "<Unknown Version>"

    // Graphical
    private const val SCREEN_WIDTH: Int = 1080
    private const val SCREEN_HEIGHT: Int = 720
    private const val FPS: Int = 60

    private val preferences: Record = Record()

    internal fun copyPreferences(): Record {
        return preferences.copy()
    }

    init {
        // Application
        preferences["title"] = TITLE
        preferences["version"] = VERSION

        // Graphical
        preferences["screen_width"] = SCREEN_WIDTH
        preferences["screen_height"] = SCREEN_HEIGHT
        preferences["fps"] = FPS

        // Logging
        preferences["log_level"] = LoggerConfig.DEFAULT_LOG_LEVEL
        preferences["save_logs"] = LoggerConfig.DEFAULT_SAVE_LOGS
        preferences["log_directory"] = LoggerConfig.DEFAULT_LOG_DIRECTORY
    }

}