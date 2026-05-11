package mayonez

import mayonez.config.*
import mayonez.event.*
import mayonez.input.*
import java.awt.*
import java.util.*

/**
 * Store multiple scenes for later use and helps the user reload and switch between
 * scenes. Stored scenes must have a unique name, and only one scene may be active
 * at once. Scenes are also tracked by the order in which they were added, or index.
 *
 * Usage: Scenes can be preloaded into the SceneManager through the
 * [Launcher.addScenesToManager] method before the application starts
 * running. Scenes can be added at any time with [SceneManager.addScene]
 * and retrieved with [SceneManager.getScene] using their name or the order in which
 * they were added. To switch scenes, use the [SceneManager.changeScene] method
 * to set a new scene and control the old and new scene loading behavior.
 *
 * See [Launcher] and [Scene] for more information.
 *
 * @author SlavSquatSuperstar
 */
// TODO rework
// TODO allow null scene?
// TODO remove scene?
object SceneManager {

    // Scene Fields

    private val scenes: MutableMap<String, Scene> = HashMap() // The scene pool
    private val sceneNames: MutableList<String> = ArrayList() // The scene order

    @JvmField
    internal val SCENE_EVENTS: EventSystem<SceneEvent> = EventSystem()
    private val sceneEventBuffer: Queue<SceneEvent> = ArrayDeque()
    private val sceneEventCallbacks: Queue<SceneEventCallback> = ArrayDeque()

    init {
        SCENE_EVENTS.subscribe { event -> sceneEventBuffer.offer(event) }
    }

    /** The scene that the application is actively running. */
    @JvmStatic
    lateinit var currentScene: Scene

    // Game Loop Methods
    @JvmStatic
    @JvmName("updateScene")
    internal fun updateScene(dt: Float) {
        // Finish updating current scene
        currentScene.update(dt)

        // Execute all queued callbacks
        while (sceneEventBuffer.isNotEmpty()) {
            val event = sceneEventBuffer.poll()
            if (event.state == SceneState.STOPPED) {  // TODO better event checking
                val callback = sceneEventCallbacks.poll()
                callback?.execute(event)
            }
        }
    }

    @JvmStatic
    @JvmName("renderScene")
    // TODO remove g2 from param?
    internal fun renderScene(g2: Graphics2D?) {
        currentScene.render(g2)
    }

    // Scene Control Methods

    /**
     * Pauses or unpauses the current scene. Does nothing if the scene is stopped.
     */
    @JvmStatic
    fun toggleScenePaused() {
        if (currentScene.isPaused) {
            currentScene.resume()
            Logger.debug("Resumed scene \"${currentScene.name}\"")
        } else if (currentScene.isRunning) {
            currentScene.pause()
            Logger.debug("Paused scene \"${currentScene.name}\"")
        }
    }

    /**
     * Restarts the current scene and reinitializes all its game objects. If the scene
     * was stopped, it will simply be started.
     */
    @JvmStatic
    fun restartScene() {
        destroyScene()

        // Start scene later
        sceneEventCallbacks.offer { startScene() }
    }

    /**
     * Switches the active scene to the given scene. The old scene may be paused or stopped,
     * and the new scene may be started or resumed. If the new scene was not in the scene pool,
     * It will automatically be added.
     *
     * @param scene the new scene, does nothing if null
     * @param stopOld whether to stop and unload the old scene
     * @param restartNew whether to restart the new scene if already loaded
     */
    @JvmStatic
    fun changeScene(scene: Scene?, stopOld: Boolean, restartNew: Boolean) {
        if (scene == null) return  // Don't set a null scene
        else if (!this::currentScene.isInitialized) return // Don't switch if no scene

        Logger.debug("Switching scenes (stop old = %s, restart new = %s)", stopOld, restartNew)

        // Old scene behavior
        // Switch and start later
        if (stopOld) {
            destroyScene()
            sceneEventCallbacks.offer { setNewSceneAndStart(scene, restartNew) }
        } else {
            pauseScene()
            setNewSceneAndStart(scene, restartNew)
        }
    }

    private fun setNewSceneAndStart(scene: Scene, restartNew: Boolean) {
        // Switch Scene
        setScene(scene)

        // New scene behavior
        if (restartNew) {
            stopScene() // Should stop, not destroy, and no callback
            startScene()
        } else {
            startScene()
            resumeScene()
        }
    }

    // Scene State Methods

    /** Starts the current scene and initializes all its game objects. */
    @JvmStatic
    @JvmName("startScene")
    internal fun startScene() {
        if (currentScene.isStopped || currentScene.isDestroyed) {
            currentScene.start()
            MouseInput.setPointTransformer(currentScene.camera)
            Logger.debug("Started scene \"${currentScene.name}\"")
        }
    }

    /** Signals the current scene to stop. */
    internal fun destroyScene() {
        if (!currentScene.isStopped) {
            currentScene.destroy()
            Logger.debug("Stopped scene \"${currentScene.name}\"")
        }
    }

    /** Stops the current scene and destroys all its game objects. */
    @JvmStatic
    @JvmName("stopScene")
    internal fun stopScene() {
        if (!currentScene.isStopped) {
            currentScene.stop()
            sceneEventCallbacks.offer { } // Do nothing
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
     * Get the number of scenes stored in the scene pool.
     *
     * @return the number of scenes
     */
    @JvmStatic
    fun numScenes(): Int = scenes.size

    /**
     * Saves a scene to the scene pool without initializing it and allows it to
     * be retrieved later. If any scene is stored under the same name, it will
     * be overwritten, but the index will not be updated.
     *
     * @param scene the scene to add
     */
    @JvmStatic
    fun addScene(scene: Scene?) {
        if (scene == null) return
        if (scene.name in sceneNames) {
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
     * @return the scene, or null if the name did not match
     */
    @JvmStatic
    fun getScene(name: String?): Scene? = scenes[name ?: "null"]

    /**
     * Retrieves the scene stored in the scene pool with the given name.
     *
     * @param index the order of the stored scene
     * @return the scene, or null if the index is invalid
     */
    @JvmStatic
    fun getScene(index: Int): Scene? {
        return if (index !in sceneNames.indices) null
        else scenes[sceneNames[index]]
    }

    /**
     * Sets the current scene.
     *
     * @param scene the new scene
     */
    internal fun setScene(scene: Scene) {
        currentScene = scene
        if (scene.name !in sceneNames) {
            addScene(scene) // Auto-add scene if new
        }
    }

}