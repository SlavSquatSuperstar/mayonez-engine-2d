package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.assets.JSONFile

/**
 * A storage class for game parameters and statistics.
 *
 * @author SlavSquatSuperstar
 */
object Preferences {

    // TODO default compile tile constants
    private val preferences = JSONFile("preferences.json", true)

    // Window
    @JvmField
    val TITLE: String = preferences.getStr("title")

    @JvmField
    val VERSION: String = preferences.getStr("version")

    @JvmField
    val SCREEN_WIDTH: Int = preferences.getInt("width")

    @JvmField
    val SCREEN_HEIGHT: Int = preferences.getInt("height")

    // Engine
    @JvmField
    val FPS: Int = preferences.getInt("fps")

    @JvmField
    val IMPULSE_ITERATIONS: Int = preferences.getInt("physics_iterations")

    // Logging

    @JvmField
    val SAVE_LOGS: Boolean = preferences.getBool("save_logs")

    @JvmField
    val LOGS_DIRECTORY: String = preferences.getStr("logs_directory")

    @JvmField
    val LOG_LEVEL: Int = preferences.getInt("log_level")

    // File I/O
    const val CHARSET: String = "UTF-8"

}