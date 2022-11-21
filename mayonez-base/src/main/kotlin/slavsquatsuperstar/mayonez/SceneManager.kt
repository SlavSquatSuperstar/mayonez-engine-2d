package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.util.Colors
import slavsquatsuperstar.mayonez.util.Logger.debug

/**
 * Provides an interface to the user for reloading and switching scenes.
 *
 * @author SlavSquatSuperstar
 */
object SceneManager {

    // Scene Fields
    @JvmStatic
    var currentScene: Scene = object : Scene("Default Scene") {
        override fun init() {
            setBackground(Colors.RED)
        }
    }
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
        if (!sceneRunning) {
            currentScene.start()
            sceneRunning = true
            debug("Scene Manager: Loaded scene \"%s\"", currentScene.name)
        }
    }

    /**
     * Stops the current scene.
     */
    @JvmStatic
    fun stopScene() {
        if (sceneRunning) {
            currentScene.stop()
            sceneRunning = false
            debug("Scene Manager: Unloaded scene \"%s\"", currentScene.name)
        }
    }

}