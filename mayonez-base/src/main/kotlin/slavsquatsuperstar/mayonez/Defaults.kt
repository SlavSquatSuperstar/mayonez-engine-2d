package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.util.Record

/**
 * The default, hardcoded settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults : Record() {

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

    init {
        /* Window */
        this["title"] = TITLE
        this["version"] = VERSION
        this["screen_width"] = SCREEN_WIDTH
        this["screen_height"] = SCREEN_HEIGHT

        /* Logging */
        this["log_level"] = LOG_LEVEL
        this["save_logs"] = SAVE_LOGS
        this["log_directory"] = LOG_DIRECTORY

        /* File I/O */
        this["text_charset"] = TEXT_CHARSET

        /* Rendering */
        this["buffer_count"] = BUFFER_COUNT
        this["fps"] = FPS
        this["max_batch_size"] = MAX_BATCH_SIZE
        this["max_texture_size"] = MAX_TEXTURE_SLOTS

        /* Physics */
        this["physics_iterations"] = IMPULSE_ITERATIONS
    }

}