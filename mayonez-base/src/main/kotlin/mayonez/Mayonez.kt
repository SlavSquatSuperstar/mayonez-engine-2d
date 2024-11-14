package mayonez

import mayonez.application.*
import mayonez.assets.*
import mayonez.config.*
import mayonez.input.*
import kotlin.system.exitProcess

/**
 * Acts as the entry point into the Mayonez Engine and stores the instance
 * of the game. Upon startup, the application loads resources, configures
 * other engine components, and tells the scene manager to load a scene.
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

    // Application Fields
    private lateinit var application: Application // Application instance
    private var initialized: Boolean = false // Are singletons initialized
    private var started: Boolean = false // Is application running

    // Config Properties

    private var config: RunConfig = RunConfig.DEFAULT_CONFIG

    /**
     * Whether to use LWJGL for creating the window and rendering instead of
     * Java AWT.
     */
    @JvmStatic
    internal val useGL: Boolean
        @JvmName("getUseGL") get() = config.useGL

    // Time Properties
    // TODO move to time

    @JvmStatic
    val deltaTime: Float
        @JvmName("getDeltaTime") get() {
            return if (this::application.isInitialized) application.deltaTime else 0f
        }

    @JvmStatic
    val fps: Int
        @JvmName("getFPS") get() {
            return if (this::application.isInitialized) application.fps else 0
        }

    @JvmStatic
    val updateFPS: Int
        get() {
            return if (this::application.isInitialized) application.updateFPS else 0
        }

    @JvmStatic
    val renderFPS: Int
        get() {
            return if (this::application.isInitialized) application.renderFPS else 0
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
            this.config = config
            initializeSingletons()
            initializeGame(useGL)
            initialized = true
        }
    }

    /**
     * Instantiate singleton objects and fields in the correct order to avoid
     * initializer and null pointer errors.
     */
    private fun initializeSingletons() {
        // Start tracking time
        Time.startTrackingTime()
        Logger.debug("Starting program...")
        val now = Time.getStartupDateTime()
        Logger.debug("The current date time is %s %s", now.toLocalDate(), now.toLocalTime())

        // Set preferences
        Preferences.setPreferences()
        Time.setTimeStepSecs(1f / Preferences.fps)

        // Create logger instance
        Logger.setConfig(Preferences.getLoggerConfig())
        Logger.log("Started ${Preferences.title} ${Preferences.version}")
    }

    /**
     * Initialize the game engine and input instances of the application.
     */
    private fun initializeGame(useGL: Boolean) {
        if (!this::application.isInitialized) {
            // Create input instances
            val keyInput = KeyInput.createInstance(useGL)
            val mouseInput = MouseInput.createInstance(useGL)

            // Create game engine instance
            try {
                application = ApplicationFactory.createApplication(useGL, keyInput, mouseInput)
                Logger.debug("Using \"%s\" engine", if (useGL) "GL" else "AWT")
            } catch (e: WindowInitException) {
                Logger.printStackTrace(e)
                exitWithErrorMessage("Fatal error while initializing engine")
            }

            // Load assets
            // TODO handle from scene manager
            Assets.initialize()
            Assets.loadResources()
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
            if (this::application.isInitialized) application.start()
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
            SceneManager.stopScene()
            SceneManager.clearScenes()
            Assets.clearAssets()
            application.stop()
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
        Logger.shutdown(status)
        exitProcess(status)
    }

}