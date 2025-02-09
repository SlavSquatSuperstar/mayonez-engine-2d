package mayonez

import mayonez.config.*
import mayonez.input.*
import java.awt.*

/**
 * Store multiple scenes for later use and helps the user reload and switch between
 * scenes. Stored scenes must have a unique name, and only one scene may be active
 * at once.
 *
 * Usage: Scenes can be preloaded into the SceneManager through the
 * [Launcher.loadScenesToManager] method before the application starts
 * running. Scenes can be added at any time with [SceneManager.addScene]
 * and retrieved with [SceneManager.getScene] using their name or the order in which
 * they were added. To switch scenes, use the [SceneManager.changeSceneAsNew] method
 * to set a new scene or the [SceneManager.changeScene] method to resume an existing
 * scene.
 *
 * See [Launcher] and [Scene] for more information.
 *
 * @author SlavSquatSuperstar
 */
// TODO rework
// TODO allow null scene?
object SceneManager {

    // Scene Fields

    private val scenes: MutableMap<String, Scene> = HashMap() // The scene pool
    private val sceneNames: MutableList<String> = ArrayList() // The scene order

    /** The scene that is currently loaded by the game. */
    @JvmStatic
    lateinit var currentScene: Scene

    private val hasCurrentScene: Boolean
        get() = this::currentScene.isInitialized

    // Game Loop Methods
    @JvmStatic
    @JvmName("updateScene")
    internal fun updateScene(dt: Float) {
        currentScene.update(dt)
    }

    @JvmStatic
    @JvmName("renderScene")
    // TODO remove g2 from param?
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
        Logger.debug("Restarting current scene")
        stopScene()
        startScene()
    }

    /**
     * Stops the current scene and switches over to the given scene. If the new scene
     * was already loaded, it will be restarted.
     *
     * @param scene a scene instance
     */
    @JvmStatic
    fun changeSceneAsNew(scene: Scene?) {
        if (scene == null) return  // Don't set a null scene
        if (hasCurrentScene) {
            Logger.debug("Switching scenes as new")
            stopScene()
        }
        currentScene = scene
        if (scene.name !in scenes) {
            addScene(scene) // Auto-add scene if new
        }
        startScene()
    }

    /**
     * Pauses the current scene and switches over to the given scene without restarting
     * the new scene.
     *
     * @param scene a scene instance
     */
    @JvmStatic
    fun changeScene(scene: Scene?) {
        if (scene == null) return  // Don't set a null scene
        if (hasCurrentScene) {
            Logger.debug("Switching scenes")
            pauseScene()
        }
        currentScene = scene
        if (scene.name !in scenes) {
            addScene(scene) // Auto-add scene if new
        }
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
            Logger.debug("Resumed scene \"${currentScene.name}\"")
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
            Logger.debug("Paused scene \"${currentScene.name}\"")
        }
    }

    // Scene Pool Methods

    /** Clears all stored scenes from the scene pool. */
    @JvmStatic
    fun clearScenes() {
        scenes.clear()
        sceneNames.clear()
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
        if (scene == null) return
        if (scene.name in scenes) {
            Logger.debug("Replaced scene \"${scene.name}\"")
        } else {
            sceneNames.add(scene.name) // Add scene name if new
            Logger.debug("Added scene \"${scene.name}\"")
        }
        scenes[scene.name] = scene
    }

    /**
     * Retrieves the scene stored in the scene pool with the given name.
     *
     * @param name the name of the stored scene
     * @return the scene, or null if it does not exist
     */
    @JvmStatic
    fun getScene(name: String?): Scene? = scenes[name]

    /**
     * Retrieves the scene stored in the scene pool with the given name.
     *
     * @param index the order of the stored scene
     * @return the scene, or null if the index is invalid
     */
    @JvmStatic
    fun getScene(index: Int): Scene? {
        if (index !in sceneNames.indices) return null
        val name = sceneNames[index]
        return scenes[name]
    }

}