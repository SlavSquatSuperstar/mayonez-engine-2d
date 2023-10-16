package mayonez

import mayonez.engine.*
import mayonez.input.*
import mayonez.io.*
import mayonez.launcher.*
import mayonez.math.*
import kotlin.system.exitProcess

/**
 * An instance of Mayonez Engine. Upon startup, the application loads
 * resources, configures other engine components, and tells the scene
 * manager to load a scene.
 *
 * Usage: To start an instance of Mayonez Engine, create a [Launcher]
 * and set the "Use GL" property through [Launcher.setRunConfig]. Then,
 * load any number of scenes through [Launcher.loadScenesToManager] or
 * [SceneManager.addScene]. Finally, start the game with a scene by calling
 * [Launcher.startGame].
 *
 * To exit the program, call [Mayonez.stop] with an integer exit code (0
 * for success, anything else for failure).
 *
 * See [Launcher] for more information.
 */
object Mayonez {

    // Run Fields
    private var initialized: Boolean = false
    private lateinit var game: GameEngine
    private var started: Boolean = false

    // Properties

    /** The size of the application window, in pixels. */
    @JvmStatic
    val screenSize: Vec2
        get() = Vec2(
            Preferences.screenWidth.toFloat(),
            Preferences.screenHeight.toFloat()
        )

    /**
     * The content scaling of the application window, usually 1x1 (100%). The
     * value may be different if the current display has changed its screen
     * scaling or resolution.
     *
     * For example, if the window scale is 2x2 (200%), then the window size
     * will appear twice as large as the value stated in the user preferences.
     */
    @JvmStatic
    internal var windowScale: Vec2 = Vec2(1f)
        @JvmName("getWindowScale") get
        @JvmName("setWindowScale") set

    /** Whether to use OpenGL for rendering instead of Java AWT. */
    @JvmStatic
    internal var useGL: Boolean = false // crashes tests when set to true :\
        @JvmName("getUseGL") get
        private set

    // Init Methods

    /**
     * Sets the run configuration for the program. Must be called before
     * [start].
     */
    @JvmStatic
    @JvmName("setConfig")
    internal fun setConfig(config: RunConfig) {
        if (!initialized) {
            this.useGL = config.useGL
            initializeSingletons()
            initializeGame()
            initialized = true
        }
    }

    /**
     * Instantiate singleton objects and fields in the correct order to avoid
     * initializer and null pointer errors.
     */
    private fun initializeSingletons() {
        Logger.log("Starting program...")
        Time.debugCurrentDateTime()

        Preferences.setPreferences()
        Time.timeStepSecs = (1f / Preferences.fps)
        Logger.setConfig(Preferences.getLoggerConfig())
        Logger.log("Started ${Preferences.title} ${Preferences.version}")

        Assets.initialize()
        Assets.loadResources()
    }

    private fun initializeGame() {
        if (!this::game.isInitialized) {
            KeyInput.setUseGL(useGL)
            MouseInput.setUseGL(useGL)
            game = EngineFactory.createGameEngine(useGL)
            Logger.debug("Using \"%s\" engine", if (useGL) "GL" else "AWT")
        }
    }

    // Game Loop Methods

    /**
     * Start the game and load a scene. Must be called after [setConfig].
     *
     * @param scene the starting scene
     */
    @JvmStatic
    @JvmName("start")
    internal fun start(scene: Scene?) {
        if (!initialized) {
            exitWithErrorMessage("Cannot start program without setting run configuration")
        } else if (scene == null) {
            exitWithErrorMessage("Cannot start program with a null scene")
        }
        if (!started) {
            started = true
            SceneManager.setScene(scene)
            startGame()
        }
    }

    /**
     * Stop the game with an exit code and terminate the application. See
     * [ExitCode] for reserved codes.
     *
     * @param status an exit code (zero for success, non-zero for error)
     */
    @JvmStatic
    fun stop(status: Int) {
        if (started) {
            started = false
            game.stop()
            Assets.clearAssets()
            SceneManager.clearScenes()
            exitProgram(status)
        }
    }

    // Helper Methods

    private fun startGame() {
        if (this::game.isInitialized) game.start()
        else exitWithErrorMessage("Cannot start without configuring program \"Use GL\" option")
    }

    private fun exitWithErrorMessage(message: String) {
        Logger.error(message)
        val status = ExitCode.ERROR
        exitProgram(status)
    }

    /** Print an exit message and immediately terminate the program. */
    private fun exitProgram(status: Int): Nothing {
        Logger.logExitMessage(status)
        exitProcess(status)
    }

    private fun Logger.logExitMessage(status: Int) {
        val message = "Exited program with code $status"
        if (status == 0) this.log("$message (Success)")
        else this.error("$message (Error)")
    }

}