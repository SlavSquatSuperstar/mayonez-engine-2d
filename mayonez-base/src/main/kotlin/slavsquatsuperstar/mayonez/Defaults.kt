package slavsquatsuperstar.mayonez

/**
 * The default, hardcoded settings for the game engine.
 *
 * @author SlavSquatSuperstar
 */
object Defaults : GameConfig() {

    init {
        // Window
        this["title"] = "<Unknown Application>"
        this["version"] = "<Unknown Version>"
        this["screen_width"] = 1080
        this["screen_height"] = 720

        // Logging
        this["log_level"] = 2
        this["save_logs"] = true
        this["log_directory"] = "logs/"

        // File I/O
        this["file_charset"] = "utf-8"

        // Rendering
        this["buffers"] = 2
        this["fps"] = 60
        this["max_batch_size"] = 100
        this["max_texture_size"] = 8

        // Physics
        this["physics_iterations"] = 4
    }

}