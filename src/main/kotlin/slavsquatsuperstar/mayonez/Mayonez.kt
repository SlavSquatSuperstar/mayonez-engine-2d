package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.Logger.log
import slavsquatsuperstar.mayonez.Logger.printExitMessage
import slavsquatsuperstar.mayonez.Logger.trace
import slavsquatsuperstar.mayonez.Logger.warn
import slavsquatsuperstar.mayonez.engine.JGame
import slavsquatsuperstar.mayonez.fileio.Assets
import slavsquatsuperstar.mayonez.physics2d.Physics2D
import slavsquatsuperstar.mayonez.renderer.Renderer
import slavsquatsuperstar.mayonezgl.engine.GLGame
import kotlin.system.exitProcess

object Mayonez {

    @JvmField
    val TIME_STARTED: Long = System.nanoTime()

    @JvmField
    val TIME_STEP: Float

    /**
     * Returns the time in seconds since the program started.
     *
     * @return the duration of this program
     */
    @JvmStatic
    // TODO use DateTime?
    val time: Float
        get() = (System.nanoTime() - TIME_STARTED) / 1.0E9f

    // Initializer
    var INIT_ASSETS = false
        private set
    var INIT_PREFERENCES = false
        private set
    private var INIT_RESOURCES = false

    // Game
    private var game: GameEngine? = null // Engine config, either Java or GL
    private var started = false

    init {
        init()
        TIME_STEP = 1.0f / Preferences.FPS
    }

    // Init Methods

    @JvmStatic
    fun setUseGL(useGL: Boolean) {
        game = if (useGL) GLGame() else JGame()
    }

    /**
     * Instantiate singleton objects in the correct order to avoid ExceptionInInitializer errors from circular dependencies.
     */
    @JvmStatic
    fun init() {
        // Set up Assets system
        if (!INIT_ASSETS) {
            INIT_ASSETS = true
//            val assets = Assets
        }
        // Read Preferences
        if (!INIT_PREFERENCES) {
            INIT_PREFERENCES = true
//            val prefs = Preferences
        }
        if (!INIT_RESOURCES) {
            INIT_RESOURCES = true
            log("Engine: Starting %s %s", Preferences.TITLE, Preferences.VERSION)
            trace("Engine: Loading assets")
            Assets.scanResources("assets") // Load all game assets
        }
    }

    private fun exitIfNotConfigured() {
        warn("Game Engine \"Use GL\" option has not been configured yet")
        stop(1)
    }

    /* Game Loop */

    @JvmStatic
    fun start() {
        if (started) return
        started = true
        game?.start() ?: exitIfNotConfigured()
    }

    @JvmStatic
    fun stop(status: Int) {
        if (!started) return
        started = false

        if (status == 0) log("Engine: Stopping with exit code %d", status)
        else warn("Engine: Stopping with exit code %d", status)

        game?.stop()
        Assets.clearAssets()
        printExitMessage()
        exitProcess(status)
    }

    // Getters and Setters

    @JvmStatic
    val scene: Scene
        get() = game!!.scene

    @JvmStatic
    fun setScene(scene: Scene) {
        if (!started) init()
        game?.setScene(scene) ?: exitIfNotConfigured()
    }

    @JvmStatic
    val physics: Physics2D
        get() = game!!.physics

    @JvmStatic
    val renderer: Renderer
        get() = game!!.renderer

    @JvmStatic
    val windowWidth: Int
        get() = game!!.window.width

    @JvmStatic
    val windowHeight: Int
        get() = game!!.window.height

}