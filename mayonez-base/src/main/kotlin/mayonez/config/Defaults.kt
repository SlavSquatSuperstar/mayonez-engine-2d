package mayonez.config

import mayonez.util.*

/**
 * The hardcoded, fallback settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults {

    // Window
    private const val TITLE: String = "Untitled"
    private const val SCREEN_WIDTH: Int = 800
    private const val SCREEN_HEIGHT: Int = 600
    private const val FPS: Int = 60
    private const val FRAME_SKIP: Boolean = true
    private const val DOUBLE_CLICK_TIME: Float = 0.50f

    val preferences: Record = Record()

    init {
        // Window
        preferences["title"] = TITLE
        preferences["screen_width"] = SCREEN_WIDTH
        preferences["screen_height"] = SCREEN_HEIGHT
        preferences["fps"] = FPS
        preferences["frame_skip"] = FRAME_SKIP
        preferences["double_click_time"] = DOUBLE_CLICK_TIME

        // Logging
        preferences["log_level"] = LoggerConfig.DEFAULT_LOG_LEVEL.name
        preferences["save_logs"] = LoggerConfig.DEFAULT_SAVE_LOGS
        preferences["log_directory"] = LoggerConfig.DEFAULT_LOG_DIRECTORY
    }

}