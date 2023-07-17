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
// TODO hide setConfig/start
object Mayonez {

    // Run Fields
    private var initialized: Boolean = false
    private lateinit var game: GameEngine
    private var started: Boolean = false

    // Properties
    @JvmStatic
    val screenSize: Vec2
        get() = Vec2(Preferences.screenWidth.toFloat(), Preferences.screenHeight.toFloat())

    @JvmStatic
    var useGL: Boolean = false // crashes tests when set to true :\
        private set

    // Init Methods
    @JvmStatic
    fun setConfig(config: RunConfig) {
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

        Preferences.readFromFile()
        Time.timeStepSecs = (1f / Preferences.fps)
        Logger.setConfig(Preferences.getLoggerConfig())
        Logger.log("Started %s %s", Preferences.title, Preferences.version)

        Assets.initialize()
        Assets.loadResources()
    }

    private fun initializeGame() {
        if (!this::game.isInitialized) {
            game = EngineFactory.createGameEngine(useGL)
            Logger.debug("Using engine \"%s\"", if (useGL) "GL" else "AWT")
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
     * Stop the game with an exit code and terminate the application.
     *
     * @param status an exit code (zero for success, non-zero for error)
     */
    @JvmStatic
    fun stop(status: Int) {
        if (started) {
            started = false
            game.stop()
            Assets.clearAssets()
            exitProgram(status)
        }
    }

    /**
     * Stop the game with a reserved exit code.
     *
     * @param status an exit code constant
     */
    @JvmStatic
    fun stop(status: ExitCode) = this.stop(status.code)

    // Helper Methods

    private fun startGame() {
        if (this::game.isInitialized) game.start()
        else exitWithErrorMessage("Cannot start without configuring program \"Use GL\" option")
    }

    private fun exitWithErrorMessage(message: String) {
        Logger.error(message)
        val status = ExitCode.ERROR.code
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