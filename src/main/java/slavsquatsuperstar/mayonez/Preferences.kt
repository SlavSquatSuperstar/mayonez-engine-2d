package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.assets.JSONFile

/**
 * A storage class for game parameters and statistics.
 *
 * @author SlavSquatSuperstar
 */
class Preferences private constructor() {
    companion object {
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
        val SAVE_LOGS: Boolean = preferences.getBool("save_logs")

        // File I/O
        const val CHARSET: String = "UTF-8"
    }
}