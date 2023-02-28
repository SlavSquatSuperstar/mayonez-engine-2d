package mayonez

import mayonez.io.*
import mayonez.util.*

/**
 * A collection of settings and parameters for various parts of the game
 * engine.
 *
 * @author SlavSquatSuperstar
 */
object Preferences : Record() {

    init {
        // Start with defaults to prevent zero and null values
        copyFrom(Defaults)
    }

    internal fun readPreferences() {
        if (!Mayonez.INIT_ASSETS) Mayonez.init()
        if (Mayonez.INIT_PREFERENCES) return

        // Read preferences file and update game configuration
        loadFrom(Assets.getJSONFile("preferences.json")!!.readJSON())
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
    val maxBatchSprites: Int
        get() = getInt("max_batch_sprites")

    @JvmStatic
    val maxBatchLines: Int
        get() = getInt("max_batch_lines")

    @JvmStatic
    val maxBatchTriangles: Int
        get() = getInt("max_batch_triangles")

    @JvmStatic
    val maxTextureSlots: Int
        get() = getInt("max_texture_slots")

    /* Physics */
    @JvmStatic
    val impulseIterations: Int
        get() = getInt("physics_iterations")

}