package mayonez.config

import mayonez.*
import mayonez.util.*

/**
 * A set of parameters for the [mayonez.Logger] class.
 *
 * @author SlavSquatSuperstar
 */
data class LoggerConfig(
    /** Whether to save logged messages to log files. */
    val saveLogs: Boolean,
    /** The minimum priority required to print messages to the console. */
    val logLevel: LogLevel,
    /** The folder to save logger output files. */
    val logDirectory: String
) {

    constructor(saveLogs: Boolean, logLevel: String, logDirectory: String) :
            this(saveLogs, findWithName(logLevel), logDirectory)

    companion object {
        internal const val DEFAULT_SAVE_LOGS: Boolean = true
        internal val DEFAULT_LOG_LEVEL: LogLevel = LogLevel.INFO

        internal const val DEFAULT_LOG_DIRECTORY: String = "logs"

        val DEFAULT_CONFIG: LoggerConfig = LoggerConfig(
            DEFAULT_SAVE_LOGS, DEFAULT_LOG_LEVEL, DEFAULT_LOG_DIRECTORY
        )

        private fun findWithName(name: String): LogLevel {
            return StringUtils.findWithName(LogLevel.entries, name) ?: DEFAULT_LOG_LEVEL
        }
    }

}