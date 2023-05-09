package mayonez

import mayonez.io.*
import mayonez.util.*

/**
 * A collection of user-changeable application parameters.
 *
 * @author SlavSquatSuperstar
 */
object Preferences {

    private val preferences: Record = Defaults.PREFERENCES.copy()
    
    internal fun readPreferences() {
        if (!Mayonez.INIT_ASSETS) Mayonez.init()
        if (Mayonez.INIT_PREFERENCES) return

        // Read preferences file and update game configuration
        preferences.addAll(Assets.getJSONFile("preferences.json")!!.readJSON())
//        addAll(JSONFile("preferences.json").readJSON())
    }

    /* Window */
    @JvmStatic
    val title: String
        get() = preferences.getString("title")

    @JvmStatic
    val version: String
        get() = preferences.getString("version")

    @JvmStatic
    val screenWidth: Int
        get() = preferences.getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = preferences.getInt("screen_height")

    /* Logging */
    @JvmStatic
    val logLevel: Int
        get() = preferences.getInt("log_level")

    @JvmStatic
    val saveLogs: Boolean
        get() = preferences.getBoolean("save_logs")

    @JvmStatic
    val logDirectory: String
        get() = preferences.getString("log_directory")

    /* File I/O */
    @JvmStatic
    val textCharset: String
        get() = preferences.getString("text_charset")

    /* Renderer */
    @JvmStatic
    val FPS: Int
        get() = preferences.getInt("fps")

    @JvmStatic
    val bufferCount: Int
        get() = preferences.getInt("buffer_count")

    @JvmStatic
    val maxBatchSprites: Int
        get() = preferences.getInt("max_batch_sprites")

    @JvmStatic
    val maxBatchLines: Int
        get() = preferences.getInt("max_batch_lines")

    @JvmStatic
    val maxBatchTriangles: Int
        get() = preferences.getInt("max_batch_triangles")

    @JvmStatic
    val maxTextureSlots: Int
        get() = preferences.getInt("max_texture_slots")

    /* Physics */
    @JvmStatic
    val impulseIterations: Int
        get() = preferences.getInt("physics_iterations")

}