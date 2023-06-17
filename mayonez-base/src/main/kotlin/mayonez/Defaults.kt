package mayonez

import mayonez.util.*

/**
 * The default, hardcoded settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults {

    /* Window */
    const val TITLE: String = "Mayonez Engine Application"
    const val VERSION: String = "<Unknown Version>"

    const val SCREEN_WIDTH: Int = 1080
    const val SCREEN_HEIGHT: Int = 720

    const val FPS: Int = 60

    /* Logging */
    // TODO move elsewhere
    const val LOG_LEVEL: Int = 2
    const val SAVE_LOGS: Boolean = true
    const val LOG_DIRECTORY: String = "logs/"

    @JvmStatic
    val PREFERENCES: Record = Record()

    init {
        /* Window */
        PREFERENCES["title"] = TITLE
        PREFERENCES["version"] = VERSION
        PREFERENCES["screen_width"] = SCREEN_WIDTH
        PREFERENCES["screen_height"] = SCREEN_HEIGHT
        PREFERENCES["fps"] = FPS

        /* Logging */
        PREFERENCES["log_level"] = LOG_LEVEL
        PREFERENCES["save_logs"] = SAVE_LOGS
        PREFERENCES["log_directory"] = LOG_DIRECTORY
    }

}