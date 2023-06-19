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

    // Move somewhere else, maybe Time class
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

    // Properties
    @JvmStatic
    val screenSize: Vec2
        get() = Vec2(Preferences.screenWidth.toFloat(), Preferences.screenHeight.toFloat())


    // Run Config
    private var initialized: Boolean = false
    private var config: RunConfig = RunConfig.DEFAULT_CONFIG

    @JvmStatic
    var useGL: Boolean = false
        private set

    // Game Fields
    // TODO set default fps
    private lateinit var game: GameEngine
    private var started: Boolean = false
    
    init {
        init()
        TIME_STEP = 1.0f / Preferences.fps
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
        Logger.log("Initializing Mayonez Engine...")
        Preferences.readFromFile()
        Logger.setConfig(Preferences.getLoggerConfig())
        Assets.initialize() // Set up Assets System
        Assets.loadResources()
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
        startGame()
    }

    /**
     * Stop the game with an exit code.
     *
     * @param status an exit code (zero for no error, non-zero for error)
     */
    @JvmStatic
    fun stop(status: Int) {
        if (!started) return
        started = false

        game.stop()
        Assets.clearAssets()

        logExitMessage(status)
        exitProcess(status)
    }

    // Helper Methods

    private fun startGame() {
        if (this::game.isInitialized) game.start()
        else exitIfNotConfigured()
    }

    // TODO make new method
    private fun exitIfNotConfigured() {
        Logger.error("Game Engine \"Use GL\" option has not been configured yet")
        stop(1)
    }

    private fun logExitMessage(status: Int) {
        if (status == 0) Logger.log("Exiting program with code %d", status)
        else Logger.error("Exiting program with code %d", status)
    }

}