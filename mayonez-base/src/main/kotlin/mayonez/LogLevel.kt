package mayonez

/**
 * Different logger priority levels to be used ƒor reporting different
 * situations. Higher levels are more important, while lower levels are less
 * important. The minimum log level can be set through preferences, allowing
 * lower level messages to be filtered out.
 *
 * @author SlavSquatSuperstar
 */
enum class LogLevel(val level: Int) {

    /** Denotes that all log messages should be printed to the console. */
    ALL(0),

    /** A minor debug message for tracking detailed program flow. */
    TRACE(1),

    /** A low-priority debug message for developers. */
    DEBUG(2),

    /** A normal-priority informational message for end-users. */
    INFO(3),

    /** A high-priority alert that indicates a potentially harmful situation. */
    WARN(4),

    /** A severe alert that indicates an issue that may crash the program. */
    ERROR(5),

    /** A critical alert that indicates the program will crash. */
    FATAL(6),

    /** Denotes that no log messages should be printed to the console. */
    NONE(10);

}
