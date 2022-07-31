package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.io.Assets
import slavsquatsuperstar.mayonez.io.JSONFile
import slavsquatsuperstar.util.Record

/**
 * A collection of settings and parameters for various parts of the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Preferences : Record() {

    init {
        copyFrom(Defaults)
    }

    internal fun readPreferences() {
        if (!Mayonez.INIT_ASSETS) Mayonez.init()
        if (Mayonez.INIT_PREFERENCES) return

        // Don't set preferences until after file has been read to prevent default values and nulls
        val prefsFile = Assets.createAsset("preferences.json", JSONFile::class.java)!!.readJSON()
        loadFrom(prefsFile) // Read preferences file and update game configuration
    }

    /* Window */
    @JvmStatic
    val title: String
        get() = getString("title")

    @JvmStatic
    val version: String
        get() = getString("version")

    @JvmStatic
    val screenWidth: Int
        get() = getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = getInt("screen_height")

    /* Logging */
    @JvmStatic
    val logLevel: Int
        get() = getInt("log_level")

    @JvmStatic
    val saveLogs: Boolean
        get() = getBoolean("save_logs")

    @JvmStatic
    val logDirectory: String
        get() = getString("log_directory")

    /* File I/O */
    @JvmStatic
    val textCharset: String
        get() = getString("text_charset")

    /* Renderer */
    @JvmStatic
    val FPS: Int
        get() = getInt("fps")

    @JvmStatic
    val bufferCount: Int
        get() = getInt("buffer_count")

    @JvmStatic
    val maxBatchSize: Int
        get() = getInt("max_batch_size")

    @JvmStatic
    val maxTextureSlots: Int
        get() = getInt("max_texture_slots")

    /* Physics */
    @JvmStatic
    val impulseIterations: Int
        get() = getInt("physics_iterations")

}