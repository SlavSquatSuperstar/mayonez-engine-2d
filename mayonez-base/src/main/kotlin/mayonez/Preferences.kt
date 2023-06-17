package mayonez

import mayonez.io.*
import mayonez.io.text.*
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
        val prefsFile = Assets.getAsset("preferences.json", JSONFile::class.java)!!
//        val prefsFile = JSONFile("preferences.json").readJSON()
        preferences.addAll(prefsFile.readJSON())
    }

    // Application

    @JvmStatic
    val title: String
        get() = preferences.getString("title")

    @JvmStatic
    val version: String
        get() = preferences.getString("version")

    // Graphical

    @JvmStatic
    val screenWidth: Int
        get() = preferences.getInt("screen_width")

    @JvmStatic
    val screenHeight: Int
        get() = preferences.getInt("screen_height")

    @JvmStatic
    val fps: Int
        get() = preferences.getInt("fps")

    // Logging
    @JvmStatic
    val logLevel: Int
        get() = preferences.getInt("log_level")

    @JvmStatic
    val saveLogs: Boolean
        get() = preferences.getBoolean("save_logs")

    @JvmStatic
    val logDirectory: String
        get() = preferences.getString("log_directory")

}