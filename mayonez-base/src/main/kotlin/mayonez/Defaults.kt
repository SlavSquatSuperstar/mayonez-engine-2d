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

    /* Logging */
    const val LOG_LEVEL: Int = 2
    const val SAVE_LOGS: Boolean = true
    const val LOG_DIRECTORY: String = "logs/"

    /* File I/O */
    const val TEXT_CHARSET: String = "utf-8"

    /* Renderer */
    const val FPS: Int = 60
    const val BUFFER_COUNT: Int = 2

    const val MAX_BATCH_SIZE: Int = 100
    const val MAX_TEXTURE_SLOTS: Int = 8

    /* Physics */
    const val IMPULSE_ITERATIONS: Int = 4

    @JvmStatic
    val PREFERENCES: Record = Record()

    init {
        /* Window */
        PREFERENCES["title"] = TITLE
        PREFERENCES["version"] = VERSION
        PREFERENCES["screen_width"] = SCREEN_WIDTH
        PREFERENCES["screen_height"] = SCREEN_HEIGHT

        /* Logging */
        PREFERENCES["log_level"] = LOG_LEVEL
        PREFERENCES["save_logs"] = SAVE_LOGS
        PREFERENCES["log_directory"] = LOG_DIRECTORY

        /* File I/O */
        PREFERENCES["text_charset"] = TEXT_CHARSET

        /* Rendering */
        PREFERENCES["buffer_count"] = BUFFER_COUNT
        PREFERENCES["fps"] = FPS
        PREFERENCES["max_batch_size"] = MAX_BATCH_SIZE
        PREFERENCES["max_texture_size"] = MAX_TEXTURE_SLOTS

        /* Physics */
        PREFERENCES["physics_iterations"] = IMPULSE_ITERATIONS
    }

}