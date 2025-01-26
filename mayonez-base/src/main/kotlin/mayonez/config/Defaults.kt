package mayonez.config

import mayonez.util.*

/**
 * The default, hardcoded settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults {

    // Application
    private const val TITLE: String = "Mayonez Engine"

    // Graphical
    private const val SCREEN_WIDTH: Int = 800
    private const val SCREEN_HEIGHT: Int = 600
    private const val FPS: Int = 60
    private const val FRAME_SKIP: Boolean = true

    val preferences: Record = Record()

    init {
        // Application
        preferences["title"] = TITLE

        // Graphical
        preferences["screen_width"] = SCREEN_WIDTH
        preferences["screen_height"] = SCREEN_HEIGHT
        preferences["fps"] = FPS
        preferences["frame_skip"] = FRAME_SKIP

        // Logging
        preferences["log_level"] = LoggerConfig.DEFAULT_LOG_LEVEL
        preferences["save_logs"] = LoggerConfig.DEFAULT_SAVE_LOGS
        preferences["log_directory"] = LoggerConfig.DEFAULT_LOG_DIRECTORY
    }

}