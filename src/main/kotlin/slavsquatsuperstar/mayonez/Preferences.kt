package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.fileio.AssetType
import slavsquatsuperstar.mayonez.fileio.Assets
import slavsquatsuperstar.mayonez.fileio.JSONFile

/**
 * A storage class for game parameters and statistics.
 *
 * @author SlavSquatSuperstar
 */
object Preferences {

    // TODO default compile tile constants
//    private val preferences = JSONFile(Assets.createAsset("preferences.json", AssetType.CLASSPATH).filename)
    private val preferences = Assets.createAsset("preferences.json", AssetType.CLASSPATH, JSONFile::class.java)!!

    // Window
    @JvmField
    val TITLE: String = preferences.getStr("title")

    @JvmField
    val VERSION: String = preferences.getStr("version")

    @JvmField
    val SCREEN_WIDTH: Int = preferences.getInt("width")

    @JvmField
    val SCREEN_HEIGHT: Int = preferences.getInt("height")

    // Logging
    @JvmField
    val SAVE_LOGS: Boolean = preferences.getBool("save_logs")

    @JvmField
    val LOGS_DIRECTORY: String = preferences.getStr("logs_directory")

    @JvmField
    val LOG_LEVEL: Int = preferences.getInt("log_level")

    // Renderer
    @JvmField
    val FPS: Int = preferences.getInt("fps")

    @JvmField
    val MAX_BATCH_SIZE: Int = preferences.getInt("max_batch_size")

    @JvmField
    val MAX_TEXTURE_SLOTS: Int = preferences.getInt("max_texture_slots")

    // Physics
    @JvmField
    val IMPULSE_ITERATIONS: Int = preferences.getInt("physics_iterations")

    // File I/O
    const val CHARSET: String = "UTF-8"

}