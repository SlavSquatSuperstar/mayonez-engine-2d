package slavsquatsuperstar.mayonez

import slavsquatsuperstar.fileio.AssetType
import slavsquatsuperstar.fileio.TextFile
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Prints messages to the console that can be formatted and given a priority level.
 *
 * @author SlavSquatSuperstar
 */
object Logger {

    @JvmField
    var logLevel: Int = Preferences.LOG_LEVEL
    private val saveLogs: Boolean = Preferences.SAVE_LOGS
    private lateinit var logFilename: String
    private var logFile: TextFile? = null

    init {
        if (saveLogs) {
            // Create the log directory if needed
            val logsDirectory = File(Preferences.LOGS_DIRECTORY)
            if (!logsDirectory.exists()) logsDirectory.mkdir()

            // Count number of log files with the same date
            val today = LocalDate.now()
            var logCount = 0
            for (f in logsDirectory.listFiles()!!)
                if (f.name.startsWith(today.toString()))
                    logCount++

            logFilename = "${logsDirectory.path}/$today-${++logCount}.log"
            logFile = TextFile(logFilename, AssetType.OUTPUT)
        }
    }

    private fun logInternal(msg: Any?, vararg args: Any?, level: LogLevel) {
        val output = StringBuilder("[%02d:%.4f] ".format((Time.time / 60).toInt(), Time.time % 60)) // Time stamp
        try {
            output.append(msg.toString().format(*args)) // Level prefix
            if (saveLogs) // Always save to log regardless of level
                logFile!!.append(output.toString())
        } catch (e: IllegalFormatException) {
            output.append("Logger: Could not format message \"$msg\"")
        } finally {
            if (level.level >= this.logLevel) // Print to console if high enough level
                println(output.toString())
        }
    }

    /**
     * Prints a formatted message to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun log(msg: Any?, vararg args: Any?) {
        logInternal(msg, *args, level = LogLevel.NORMAL)
    }

    /**
     * Prints a debug message to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun trace(msg: Any?, vararg args: Any?) {
        logInternal("[DEBUG] $msg", *args, level = LogLevel.TRACE)
    }

    /**
     * Prints a warning to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) {
        logInternal("[WARNING] $msg", *args, level = LogLevel.WARNING)
    }

    @JvmStatic
    fun printExitMessage() {
        if (saveLogs) log("Logger: Saved log to file \"%s\"", logFilename)
    }

    enum class LogLevel(val level: Int) {
        /**
         * A low priority debug message.
         */
        TRACE(0),

        /**
         * A normal priority infomational message.
         */
        NORMAL(1),

        /**
         * A high priority warning message
         */
        WARNING(2)
    }

}