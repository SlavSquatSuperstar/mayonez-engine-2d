package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.util.Logger.debug

/**
 * Provides an interface to the user for reloading and switching scenes.
 *
 * @author SlavSquatSuperstar
 */
// TODO need some safeguard against null scenes
object SceneManager {
    @JvmStatic
    lateinit var currentScene: Scene
        private set
    private var sceneRunning: Boolean = false

    /**
     * Restarts the current scene.
     */
    @JvmStatic
    fun reloadScene() {
        stopScene()
        startScene()
    }

    /**
     * Loads a new scene and starts it.
     */
    @JvmStatic
    fun setScene(scene: Scene?) {
        if (scene == null) return  // don't set a null scene
        stopScene() // stop current scene
        currentScene = scene
        startScene() // start new scene
    }

    /**
     * Starts the current scene.
     */
    @JvmStatic
    fun startScene() {
        if (this::currentScene.isInitialized && !sceneRunning) {
            sceneRunning = true
            currentScene.start()
            debug("Scene Manager: Loaded scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Stops the current scene.
     */
    @JvmStatic
    fun stopScene() {
        if (this::currentScene.isInitialized && sceneRunning) {
            sceneRunning = false
            currentScene.stop()
            debug("Scene Manager: Unloaded scene \"%s\"", currentScene.name)
        }
    }
}