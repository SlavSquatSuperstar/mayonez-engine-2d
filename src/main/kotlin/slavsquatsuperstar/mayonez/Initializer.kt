package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.fileio.Assets

object Initializer {

    var INIT_ASSETS = false
        private set
    var INIT_PREFERENCES = false
        private set
    var INIT_LOGGER = false
        private set

    init {
        init()
    }

    /**
     * Instantiate singleton objects in the correct order to avoid ExceptionInInitializer errors from circular dependencies.
     */
    @JvmStatic
    fun init() {
        // Set up Assets system
        if (!INIT_ASSETS) {
            INIT_ASSETS = true
            val assets = Assets
//            println("Initialized assets")
        }
        // Read Preferences
        if (!INIT_PREFERENCES) {
            INIT_PREFERENCES = true
            val prefs = Preferences
//            println("Initialized preferences")
        }
        // Configure Logger
        if (!INIT_LOGGER) {
            INIT_LOGGER = true
            val logger = Logger
//            println("Initialized Logger")
        }
        // Scan Assets
        // Load Scene
    }

}