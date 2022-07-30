package slavsquatsuperstar.mayonez

/**
 * Different logger priority levels to be used ƒor reporting different situations. Higher levels are more important,
 * while lower level messages can be filtered out by changing the log level preference.
 *
 * @author SlavSquatSuperstar
 */
enum class LogLevel {
    /**
     * Denotes that all log messages should be printed to the console.
     */
    ALL, // Level 0

    /**
     * A low-priority debug message for developers.
     */
    DEBUG, // Level 1

    /**
     * A normal-priority informational message for end-users.
     */
    INFO, // Level 2

    /**
     * A high-priority alert that indicates a potentially harmful situation.
     */
    WARNING, // Level 3

    /**
     * A severe alert that indicates an issue that may crash the program.
     */
    ERROR, // Level 4

    /**
     * Denotes that no log messages should be printed to the console.
     */
    NONE; // Level 5

    override fun toString(): String {
        return "$name (Level $ordinal)"
    }
}