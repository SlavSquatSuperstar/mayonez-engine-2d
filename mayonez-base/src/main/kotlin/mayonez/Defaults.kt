package mayonez

import mayonez.util.*

/**
 * The default, hardcoded settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults {

    // Application
    private const val TITLE: String = "Mayonez Engine Application"
    private const val VERSION: String = "<Unknown Version>"

    // Graphical
    private const val SCREEN_WIDTH: Int = 1080
    private const val SCREEN_HEIGHT: Int = 720
    private const val FPS: Int = 60

    val PREFERENCES: Record = Record()

    init {
        // Application
        PREFERENCES["title"] = TITLE
        PREFERENCES["version"] = VERSION

        // Graphical
        PREFERENCES["screen_width"] = SCREEN_WIDTH
        PREFERENCES["screen_height"] = SCREEN_HEIGHT
        PREFERENCES["fps"] = FPS
    }

}