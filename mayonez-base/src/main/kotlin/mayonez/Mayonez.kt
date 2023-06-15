package mayonez

import mayonez.engine.*
import mayonez.init.*
import mayonez.io.*
import mayonez.math.*
import kotlin.system.exitProcess

/**
 * The entry point into any game. Loads application resources, configures
 * the engine instance, and sets the scene to be played.
 *
 * To start an instance of Mayonez Engine, set the "Use GL" property
 * through Mayonez.setUseGL(). Then load any number of scenes through
 * SceneManager.addScene(). Finally, start the game with a scene with
 * Mayonez.start().
 */
object Mayonez {

    // TODO use LocalDateTime to track time

    @JvmField
    val NANOS_STARTED: Long = System.nanoTime()

    @JvmField
    val TIME_STEP: Float

    /**
     * Returns the time in seconds since the program started.
     *
     * @return the duration of this program
     */
    @JvmStatic
    val seconds: Float
        get() = (System.nanoTime() - NANOS_STARTED) / 1.0E9f

//    /**
//     * How sped up or slowed down the in-game time passes. A scale of 1.0 (100%
//     * speed) means the game runs in real time.
//     */
//    @JvmStatic
//    var timeScale: Float = 1f
//        set(value) {
//            field = max(value, 0f) // non-negative only
//        }

    // Run Config
    private var initialized: Boolean = false
    private var config: RunConfig = RunConfig.DEFAULT_CONFIG

    // Game Fields
    @JvmStatic
    var useGL: Boolean = false
        private set

    // TODO need to make non-nullable and set default fps
    private var game: GameEngine? = null
    var started: Boolean = false
        private set

    // Initialization
    private var INIT_ENGINE = false // Whether the engine has been created
    var INIT_ASSETS = false // Whether the asset system has been created
        private set
    var INIT_PREFERENCES = false // Whether the preferences file has been applied
        private set
    private var INIT_LOGGER = false // Whether the logger has been set up
    private var INIT_RESOURCES = false // Whether the core resources have been created

    init {
        init()
        TIME_STEP = 1.0f / Preferences.FPS
    }

    // Init Methods
    @JvmStatic
    fun setConfig(config: RunConfig) {
        if (initialized) return

        this.config = config
        setUseGL(config.useGL)
        init()
        initialized = true
    }

    private fun setUseGL(useGL: Boolean) {
        if (initialized) return

        this.useGL = useGL
        game = GameEngineFactory.createGameEngine(useGL)
        Logger.debug("Using engine \"%s\"", if (this.useGL) "GL" else "AWT")
    }

    /**
     * Instantiate singleton objects in the correct order to avoid initializer
     * errors from circular dependencies.
     */
    @JvmStatic
    internal fun init() {
        // Create Logger object
        if (!INIT_ENGINE) {
            Logger.log("Initializing...")
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
            Logger.debug("Loaded preferences")
            INIT_PREFERENCES = true
        }
        // Create log file
        if (!INIT_LOGGER) {
            Logger.setConfig(
                LoggerConfig(
                    Preferences.saveLogs, Preferences.logLevel, Preferences.logDirectory
                )
            )
            INIT_LOGGER = true
        }
        // Create Resources
        if (!INIT_RESOURCES) {
            INIT_RESOURCES = true
            Logger.log("Starting %s %s", Preferences.title, Preferences.version)
            Logger.debug("Loading resources in \"assets/\"")
            Assets.scanResources("assets") // Load all game assets
        }
    }

    // Game Loop

    /**
     * Start the game and load a scene.
     *
     * @param scene the starting scene
     */
    @JvmStatic
    fun start(scene: Scene?) {
        if (started) return
        started = true
        init()
        SceneManager.setScene(scene)
        game?.start() ?: exitIfNotConfigured()
    }

    /**
     * Stop the game with an exit code.
     *
     * @param status an exit code (0 for no error, positive for error)
     */
    @JvmStatic
    fun stop(status: Int) {
        if (!started) return
        started = false

        game?.stop()
        Assets.clearAssets()

        if (status == 0) Logger.log("Exiting program with code %d", status)
        else Logger.error("Exiting program with code %d", status)
        exitProcess(status)
    }

    // Getters and Setters

    @JvmStatic
    val screenSize: Vec2
        get() = Vec2(Preferences.screenWidth.toFloat(), Preferences.screenHeight.toFloat())

    // Helper Methods

    private fun exitIfNotConfigured() {
        Logger.error("Game Engine \"Use GL\" option has not been configured yet")
        stop(1)
    }

}