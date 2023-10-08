package mayonez

import mayonez.input.*
import mayonez.launcher.*
import java.awt.*

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

    /** The scene that is currently loaded by the game. */
    @JvmStatic
    var currentScene: Scene = object : Scene("Default Scene") {}
        private set

    private val scenes: MutableMap<String, Scene> = HashMap() // The scene pool
    private val sceneState: SceneState // The state of the current scene
        get() = currentScene.state

    // Game Loop Methods
    @JvmStatic
    @JvmName("updateCurrentScene")
    internal fun updateCurrentScene(dt: Float) {
        currentScene.update(dt)
    }

    @JvmStatic
    @JvmName("renderCurrentScene")
    internal fun renderCurrentScene(g2: Graphics2D?) {
        currentScene.render(g2)
    }

    // Scene Control Methods

    // TODO rename methods
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
     * Switches the active scene to the given scene. If the scene is running,
     * it will be restarted.
     *
     * @param scene a scene instance
     */
    @JvmStatic
    fun setScene(scene: Scene?) {
        if (scene == null) return  // don't set a null scene
        stopScene()
        saveCurrentSceneToPool()
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

        pauseScene()
        saveCurrentSceneToPool()
        currentScene = scene
        startScene()
        resumeScene()
    }

    // Load/Unload Scene Methods

    /** Starts the current scene and initializes all its game objects. */
    @JvmStatic
    @JvmName("startScene")
    internal fun startScene() {
        if (sceneState == SceneState.STOPPED) {
            currentScene.start()
            MouseInput.setSceneScale(currentScene.scale)
            MouseInput.setPointTransformer(currentScene.camera)
            Logger.debug("Started scene \"${currentScene.name}\"")
        }
    }

    /** Stops the current scene and destroys all its game objects. */
    @JvmStatic
    @JvmName("stopScene")
    internal fun stopScene() {
        if (sceneState != SceneState.STOPPED) {
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
        if (sceneState == SceneState.PAUSED) {
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
        if (sceneState == SceneState.RUNNING) {
            currentScene.pause()
            Logger.debug("Unloaded scene \"${currentScene.name}\"")
        }
    }

    // Scene Pool Methods

    /** Clears all stored scenes from the scene pool. */
    @JvmStatic
    fun clearScenes() = scenes.clear()

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