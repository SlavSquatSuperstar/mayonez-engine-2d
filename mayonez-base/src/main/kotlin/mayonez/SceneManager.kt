package mayonez

import mayonez.graphics.Colors

/**
 * Provides an interface to the user for reloading and switching scenes.
 * Only one scene may be active at once.
 *
 * @author SlavSquatSuperstar
 */
object SceneManager {

    // Scene Fields

    /** The scene that is currently loaded by the game. */
    @JvmStatic
    var currentScene: Scene = object : Scene("Default Scene") {
        override fun init() {
            setBackground(Colors.WHITE)
        }
    }
        private set

    private val scenes = HashMap<String, Scene>() // The scene pool
    private val sceneState: SceneState // The state of the current scene
        get() = currentScene.state

    // Scene Control Methods

    // rename methods
    @JvmStatic
    fun toggleScenePaused() {
        if (sceneState == SceneState.PAUSED) currentScene.resume()
        else if (sceneState == SceneState.RUNNING) currentScene.pause()
    }

    /** Restarts the current scene and reinitializes all its game objects. */
    @JvmStatic
    fun restartScene() {
        stopScene()
        startScene()
    }

    /**
     * Loads a new scene and starts it.
     *
     * @param scene a scene instance
     */
    @JvmStatic
    fun setScene(scene: Scene?) {
        if (scene == null) return  // don't set a null scene
        stopScene()
        if (!scenes.containsKey(currentScene.name)) addScene(currentScene) // save new scene
        currentScene = scene
        startScene()
    }

    /**
     * Loads an existing scene with the given name and resumes it. The scene
     * will also be initialized if needed.
     *
     * @param name the scene's name
     */
    @JvmStatic
    fun loadScene(name: String?) {
        val scene = getScene(name) ?: return // don't set a null scene

        pauseScene()
        if (!scenes.containsKey(currentScene.name)) addScene(currentScene) // save new scene
        currentScene = scene

        startScene() // start if needed
        resumeScene()
    }

    // Load/Unload Scene Methods

    /** Starts the current scene and initializes all its game objects. */
    @JvmStatic
    fun startScene() {
        if (sceneState == SceneState.STOPPED) {
            currentScene.start()
            Logger.debug("Started scene \"%s\"", currentScene.name)
        }
    }

    /** Stops the current scene and destroys all its game objects. */
    @JvmStatic
    fun stopScene() {
        if (sceneState != SceneState.STOPPED) {
            currentScene.stop()
            Logger.debug("Stopped scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Resumes the current scene if it is paused without reinitializing any of
     * its game objects.
     */
    @JvmStatic
    fun resumeScene() {
        if (sceneState == SceneState.PAUSED) {
            currentScene.resume()
            Logger.debug("Loaded scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Suspends the current scene if it is running without destroying any of
     * its game objects.
     */
    @JvmStatic
    fun pauseScene() {
        if (sceneState == SceneState.RUNNING) {
            currentScene.pause()
            Logger.debug("Unloaded scene \"%s\"", currentScene.name)
        }
    }

    // Scene Pool Methods

    /** Clears all stored scenes from the scene pool. */
    @JvmStatic
    fun clear() = scenes.clear()

    /**
     * Saves a scene to the scene pool without initializing it and allows it to
     * be retrieved later. If any scene is stored under the same name, it will
     * be overwritten.
     *
     * @param scene the scene to add
     */
    @JvmStatic
    fun addScene(scene: Scene?) {
        if (scene != null) {
            scenes[scene.name] = scene
            Logger.debug("Added scene \"%s\"", scene.name)
        }
    }

    /**
     * Retrieves the scene stored in the scene pool with the given name.
     *
     * @param name the name of the stored scene
     * @return the scene, or null if it does not exist
     */
    @JvmStatic
    fun getScene(name: String?): Scene? = scenes[name]

}