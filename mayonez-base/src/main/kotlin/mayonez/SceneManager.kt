package mayonez

import mayonez.util.Colors
import mayonez.util.Logger

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
            Logger.debug("Scene Manager: Loaded scene \"%s\"", currentScene.name)
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
            Logger.debug("Scene Manager: Unloaded scene \"%s\"", currentScene.name)
        }
    }

}