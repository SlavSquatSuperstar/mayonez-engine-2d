package mayonez.config

/**
 * A set of parameters for the [mayonez.Logger] class.
 *
 * @author SlavSquatSuperstar
 */
internal data class LoggerConfig(
    /** Whether to save logged messages to log files. */
    val saveLogs: Boolean,
    /** The minimum priority required to print messages to the console. */
    val logLevel: Int,
    /** The folder to save logger output files. */
    val logDirectory: String
) {
    companion object {
        internal const val DEFAULT_SAVE_LOGS: Boolean = true
        internal const val DEFAULT_LOG_LEVEL: Int = 2
        internal const val DEFAULT_LOG_DIRECTORY: String = "logs/"

        val DEFAULT_CONFIG: LoggerConfig = LoggerConfig(
            DEFAULT_SAVE_LOGS, DEFAULT_LOG_LEVEL, DEFAULT_LOG_DIRECTORY
        )
    }
}