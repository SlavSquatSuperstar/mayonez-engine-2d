package slavsquatsuperstar.mayonez.util

/**
 * Different logger priority levels to be used ƒor reporting different situations. Higher levels are more important,
 * while lower level messages can be filtered out by changing the log level preference.
 *
 * @author SlavSquatSuperstar
 */
enum class LogLevel(private val level: Int) {
    /**
     * Denotes that all log messages should be printed to the console.
     */
    ALL(0),

    /**
     * A low-priority debug message for developers.
     */
    DEBUG(1),

    /**
     * A normal-priority informational message for end-users.
     */
    INFO(2),

    /**
     * A high-priority alert that indicates a potentially harmful situation.
     */
    WARNING(3),

    /**
     * A severe alert that indicates an issue that may crash the program.
     */
    ERROR(4),

    /**
     * Denotes that no log messages should be printed to the console.
     */
    NONE(5);

    override fun toString(): String {
        return "$name (Level $level)"
    }
}