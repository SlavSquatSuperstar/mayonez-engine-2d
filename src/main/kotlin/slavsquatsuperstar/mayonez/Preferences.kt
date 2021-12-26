package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.fileio.AssetType
import slavsquatsuperstar.mayonez.fileio.Assets
import slavsquatsuperstar.mayonez.fileio.JSONFile

/**
 * A storage class for game parameters and statistics.
 *
 * @author SlavSquatSuperstar
 */
// TODO create default compile tile constants
object Preferences {

    private val PREFERENCES: JSONFile

    init {
        // Don't set preferences until after file has been read to prevent default values
        if (!Mayonez.INIT_ASSETS) Mayonez.init()
        PREFERENCES = Assets.createAsset("preferences.json", AssetType.CLASSPATH, JSONFile::class.java)!!
    }

    // Window
    @JvmField
    val TITLE: String = PREFERENCES.getStr("title")

    @JvmField
    val VERSION: String = PREFERENCES.getStr("version")

    @JvmField
    val SCREEN_WIDTH: Int = PREFERENCES.getInt("width")

    @JvmField
    val SCREEN_HEIGHT: Int = PREFERENCES.getInt("height")

    // Logging
    @JvmField
    val SAVE_LOGS: Boolean = PREFERENCES.getBool("save_logs")

    @JvmField
    val LOGS_DIRECTORY: String = PREFERENCES.getStr("logs_directory")

    @JvmField
    val LOG_LEVEL: Int = PREFERENCES.getInt("log_level")

    // Renderer
    @JvmField
    val BUFFER_COUNT = PREFERENCES.getInt("buffers")

    @JvmField
    val FPS: Int = PREFERENCES.getInt("fps")

    @JvmField
    val MAX_BATCH_SIZE: Int = PREFERENCES.getInt("max_batch_size")

    @JvmField
    val MAX_TEXTURE_SLOTS: Int = PREFERENCES.getInt("max_texture_slots")

    // Physics
    @JvmField
    val IMPULSE_ITERATIONS: Int = PREFERENCES.getInt("physics_iterations")

    // File I/O
    const val CHARSET: String = "UTF-8"

}