package mayonez

/**
 * Different logger priority levels to be used ƒor reporting different
 * situations. Higher levels are more important, while lower levels are less
 * important. The minimum log level can be set through preferences, allowing
 * lower level messages to be filtered out.
 *
 * @author SlavSquatSuperstar
 */
enum class LogLevel(internal val level: Int) {

    /** Denotes that all log messages should be printed to the console. */
    ALL(0),

    /** A low-priority debug message for developers. */
    DEBUG(1),

    /** A normal-priority informational message for end-users. */
    INFO(2),

    /** A high-priority alert that indicates a potentially harmful situation. */
    WARNING(3),

    /** A severe alert that indicates an issue that may crash the program. */
    ERROR(4),

    /** Denotes that no log messages should be printed to the console. */
    NONE(5);

    override fun toString(): String = "$name (Level $level)"

}
