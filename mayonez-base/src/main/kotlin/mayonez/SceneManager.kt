package mayonez

import mayonez.graphics.Colors

/**
 * Provides an interface to the user for reloading and switching scenes.
 *
 * @author SlavSquatSuperstar
 */
object SceneManager {

    // Scene Fields

    private val scenes = HashMap<String, Scene>() // The scene pool
    private var sceneRunning: Boolean = false // If the current scene is running

    /**
     * The scene that is currently loaded by the game.
     */
    @JvmStatic
    var currentScene: Scene = object : Scene("Default Scene") {
        override fun init() {
            setBackground(Colors.RED)
        }
    }
        private set

    // Scene Change Methods

    /**
     * Loads a new scene and starts it.
     *
     * @param scene a scene instance
     */
    @JvmStatic
    fun setScene(scene: Scene?) {
        if (scene == null) return  // don't set a null scene
        stopScene() // stop current scene
        currentScene = scene
        startScene() // start new scene
    }

    /**
     * Loads an existing scene with the given name and resumes it.
     *
     * @param name the scene's name
     */
    @JvmStatic
    fun setScene(name: String?) {
        val scene = getScene(name) ?: return // don't set a null scene
        unloadScene() // unload current scene
        if (!scenes.containsKey(currentScene.name)) addScene(currentScene) // save current scene if not in pool
        currentScene = scene
        loadScene() // load new scene
    }

    // Load/Unload Scene Methods

    /**
     * Restarts the current scene and reinitializes all its game objects.
     */
    @JvmStatic
    fun reloadScene() {
        stopScene()
        startScene()
    }

    /**
     * Starts the current scene and initializes all its game objects.
     */
    @JvmStatic
    fun startScene() {
        if (!sceneRunning) {
            currentScene.start()
            sceneRunning = true
            Logger.debug("Scene Manager: Started new scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Stops the current scene and destroys all its game objects.
     */
    @JvmStatic
    fun stopScene() {
        if (sceneRunning) {
            currentScene.stop()
            sceneRunning = false
            Logger.debug("Scene Manager: Stopped scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Resumes the current scene without reinitializing any of its game objects.
     */
    @JvmStatic
    fun loadScene() {
        if (!sceneRunning) {
            currentScene.load()
            sceneRunning = true
            Logger.debug("Scene Manager: Loaded existing scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Pauses the current scene without destroying any of its game objects.
     */
    @JvmStatic
    fun unloadScene() {
        if (sceneRunning) {
            currentScene.unload()
            sceneRunning = false
            Logger.debug("Scene Manager: Unloaded scene \"%s\"", currentScene.name)
        }
    }

    // Scene Pool Methods

    @JvmStatic
    fun clear() = scenes.clear()

    /**
     * Saves a scene to the scene pool and allows it to be retrieved later. If a scene exists with
     * the same name, it will be overwritten.
     *
     * @param scene the scene
     */
    @JvmStatic
    fun addScene(scene: Scene?) {
        if (scene != null) {
            scene.start()
            scenes[scene.name] = scene
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