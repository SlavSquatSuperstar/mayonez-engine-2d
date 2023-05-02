package mayonez.init

class LoggerConfig(
    /** Whether to save logged messages to log files. */
    val saveLogs: Boolean,
    /** The minimum priority required to print messages to console. */
    val logLevel: Int,
    /** The folder to save logger output files. */
    val logDirectory: String
) {
    companion object {
        const val DEFAULT_SAVE_LOGS: Boolean = true
        const val DEFAULT_LOG_LEVEL: Int = 2
        const val DEFAULT_LOG_DIRECTORY: String = "logs"

        val DEFAULT_CONFIG: LoggerConfig = LoggerConfig(
            DEFAULT_SAVE_LOGS, DEFAULT_LOG_LEVEL, DEFAULT_LOG_DIRECTORY
        )
    }
}