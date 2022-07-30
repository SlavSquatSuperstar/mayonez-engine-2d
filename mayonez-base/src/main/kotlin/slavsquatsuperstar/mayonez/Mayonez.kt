package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.engine.GLGame
import slavsquatsuperstar.mayonez.engine.GameEngine
import slavsquatsuperstar.mayonez.engine.JGame
import slavsquatsuperstar.mayonez.io.Assets
import slavsquatsuperstar.mayonez.graphics.renderer.Renderer
import slavsquatsuperstar.mayonez.physics.Physics
import kotlin.system.exitProcess

/**
 * The entry point into any game. Loads application resources, configures the engine instance,
 * and sets the scene to be played.
 */
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

    // Game
    // TODO make not null
    @JvmStatic
    var useGL: Boolean? = null
        @JvmName("shouldUseGL")
        get() = field!!
        set(value) {
            field = value
            game = if (field!!) GLGame() else JGame()
        }
    private var game: GameEngine? = null // Engine config, either Java or GL
    private var started = false

    // Initializer
    private var INIT_ENGINE = false // Whether the engine and logger have been created
    var INIT_ASSETS = false // Whether the asset system has been created
        private set
    var INIT_PREFERENCES = false // Whether the preferences file has been applied
        private set
    var INIT_LOGGER = false
        private set
    private var INIT_RESOURCES = false // Whether the core resources have been created

    init {
        init()
        TIME_STEP = 1.0f / Preferences.FPS
    }

    // Init Methods

    /**
     * Instantiate singleton objects in the correct order to avoid initializer errors from circular dependencies.
     */
    @JvmStatic
    internal fun init() { // TODO internal
        // Create Logger object
        if (!INIT_ENGINE) {
            Logger.log("Engine: Initializing...")
            INIT_ENGINE = true
        }
        // Set up Assets system
        if (!INIT_ASSETS) {
            Assets.getCurrentDirectory()
            INIT_ASSETS = true
        }
        // Read preferences file
        if (!INIT_PREFERENCES) {
            Preferences.readPreferences()
            Logger.debug("Engine: Loaded settings from preferences.json")
            INIT_PREFERENCES = true
        }
        // Create log file
        if (!INIT_LOGGER) {
            Logger.createLogFile()
            INIT_LOGGER = true
        }
        // Create Resources
        if (!INIT_RESOURCES) {
            INIT_RESOURCES = true
            Logger.log("Engine: Starting %s %s", Preferences.title, Preferences.version)
            Logger.debug("Engine: Loading assets")
            Assets.scanResources("assets") // Load all game assets
        }
    }

    /* Game Loop */

    @JvmStatic
    fun start() { // TODO make setScene auto start
        if (started) return
        started = true
        game?.start() ?: exitIfNotConfigured()
    }

    @JvmStatic
    fun stop(status: Int) {
        if (!started) return
        started = false

        if (status == 0) Logger.log("Engine: Exiting with exit code %d", status)
        else Logger.error("Engine: Exiting with exit code %d", status)

        game?.stop()
        Assets.clearAssets()
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
    val physics: Physics
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

    // Helper Methods

    private fun exitIfNotConfigured() {
        Logger.error("Game Engine \"Use GL\" option has not been configured yet")
        stop(1)
    }

}