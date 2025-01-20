package mayonez

import mayonez.config.*
import mayonez.input.*
import java.awt.Graphics2D

/**
 * Provides an interface to the user for reloading and switching scenes.
 * Only one scene may be active at once.
 *
 * Usage: Scenes can be preloaded into the SceneManager through the
 * [Launcher.loadScenesToManager] method before the application starts
 * running. Scenes can be added at any time with [SceneManager.addScene]
 * and retrieved with [SceneManager.getScene]. To switch scenes,
 * use the [SceneManager.setScene] method to set a new scene or the
 * [SceneManager.loadScene] method to resume an existing scene.
 *
 * See [Launcher] and [Scene] for more information.
 *
 * @author SlavSquatSuperstar
 */
object SceneManager {

    // Scene Fields

    private val scenes: MutableMap<String, Scene> = HashMap() // The scene pool

    /** The scene that is currently loaded by the game. */
    @JvmStatic
    lateinit var currentScene: Scene

    private val startedFirstScene: Boolean
        get() = this::currentScene.isInitialized

    // Game Loop Methods
    @JvmStatic
    @JvmName("updateScene")
    internal fun updateScene(dt: Float) {
        currentScene.update(dt)
    }

    @JvmStatic
    @JvmName("renderScene")
    internal fun renderScene(g2: Graphics2D?) {
        currentScene.render(g2)
    }

    // Scene Control Methods

    @JvmStatic
    fun toggleScenePaused() {
        if (currentScene.isPaused) currentScene.resume()
        else if (currentScene.isRunning) currentScene.pause()
    }

    /** Restarts the current scene and reinitializes all its game objects. */
    @JvmStatic
    fun restartScene() {
        stopScene()
        startScene()
    }

    /**
     * Switches the active scene to the given scene. If the scene is running,
     * it will be restarted.
     *
     * @param scene a scene instance
     */
    @JvmStatic
    fun setScene(scene: Scene?) {
        if (scene == null) return  // don't set a null scene
        if (startedFirstScene) {
            stopScene()
            saveCurrentSceneToPool()
        }
        currentScene = scene
        startScene()
    }

    /**
     * Pauses the current scene, and then resumes or starts an existing scene
     * with the given name.
     *
     * @param name the scene's name
     */
    @JvmStatic
    fun loadScene(name: String?) {
        val scene = getScene(name) ?: return // don't set a null scene
        if (startedFirstScene) {
            pauseScene()
            saveCurrentSceneToPool()
        }
        currentScene = scene
        startScene()
        resumeScene()
    }

    // Load/Unload Scene Methods

    /** Starts the current scene and initializes all its game objects. */
    @JvmStatic
    @JvmName("startScene")
    internal fun startScene() {
        if (currentScene.isStopped) {
            currentScene.start()
            MouseInput.setPointTransformer(currentScene.camera)
            Logger.debug("Started scene \"${currentScene.name}\"")
        }
    }

    /** Stops the current scene and destroys all its game objects. */
    @JvmStatic
    @JvmName("stopScene")
    internal fun stopScene() {
        if (!currentScene.isStopped) {
            currentScene.stop()
            Logger.debug("Stopped scene \"${currentScene.name}\"")
        }
    }

    /**
     * Resumes the current scene if it is paused without reinitializing any of
     * its game objects.
     */
    @JvmStatic
    fun resumeScene() {
        if (currentScene.isPaused) {
            currentScene.resume()
            Logger.debug("Loaded scene \"${currentScene.name}\"")
        }
    }

    /**
     * Suspends the current scene if it is running without destroying any of
     * its game objects.
     */
    @JvmStatic
    fun pauseScene() {
        if (currentScene.isRunning) {
            currentScene.pause()
            Logger.debug("Unloaded scene \"${currentScene.name}\"")
        }
    }

    // Scene Pool Methods

    /** Clears all stored scenes from the scene pool. */
    @JvmStatic
    fun clearScenes() {
        scenes.clear()
        Logger.debug("Cleared scene pool")
    }

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
            Logger.debug("Added scene \"${scene.name}\"")
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

    private fun saveCurrentSceneToPool() {
        if (currentScene.name !in scenes) {
            addScene(currentScene)
        }
    }

}