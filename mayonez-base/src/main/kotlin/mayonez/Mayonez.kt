package mayonez

import mayonez.assets.*
import mayonez.engine.*
import mayonez.input.*
import mayonez.launcher.*
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
// TODO rename to application manager
// TODO move to launcher/config pkg
object Mayonez {

    // Run Fields
    private var initialized: Boolean = false
    private lateinit var game: GameEngine
    private var started: Boolean = false

    // Run Properties

    /**
     * Whether to use LWJGL for creating the window and rendering instead of
     * Java AWT.
     */
    @JvmStatic
    internal var useGL: Boolean = RunConfig.DEFAULT_USE_GL
        @JvmName("getUseGL") get
        private set

    // Time Properties
    // TODO move to time

    @JvmStatic
    val deltaTime: Float
        @JvmName("getDeltaTime") get() {
            return if (this::game.isInitialized) game.deltaTime else 0f
        }

    @JvmStatic
    val fps: Int
        @JvmName("getFPS") get() {
            return if (this::game.isInitialized) game.fps else 0
        }

    @JvmStatic
    val updateFPS: Int
        get() {
            return if (this::game.isInitialized) game.updateFPS else 0
        }

    @JvmStatic
    val renderFPS: Int
        get() {
            return if (this::game.isInitialized) game.renderFPS else 0
        }

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
        // Start counting
        Time.startTrackingTime()
        Logger.debug("Starting program...")
        val now = Time.getStartupDateTime()
        Logger.debug("The current date time is %s %s", now.toLocalDate(), now.toLocalTime())

        // Set preferences
        Preferences.setPreferences()
        Time.setTimeStepSecs(1f / Preferences.fps)
        Logger.setConfig(Preferences.getLoggerConfig())
        Logger.log("Started ${Preferences.title} ${Preferences.version}")

        // load assets
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
            // Start game
            if (this::game.isInitialized) game.start()
            else exitWithErrorMessage("Cannot start without configuring program \"Use GL\" option")
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

    // Exit Helper Methods

    /**
     * Terminate the program with the given error message with exit code 1.
     */
    private fun exitWithErrorMessage(message: String): Nothing {
        Logger.error(message)
        exitProgram(ExitCode.ERROR)
    }

    /**
     * Print an exit message and immediately terminate the program with the
     * given status.
     */
    private fun exitProgram(status: Int): Nothing {
        val message = "Exited program with code $status"
        if (status == 0) Logger.log("$message (Success)")
        else Logger.error("$message (Error)")
        exitProcess(status)
    }

}