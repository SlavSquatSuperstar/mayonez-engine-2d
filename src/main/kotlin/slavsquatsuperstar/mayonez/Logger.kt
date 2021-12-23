package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.fileio.TextFile
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Prints messages to the console that can be formatted and given a priority level.
 *
 * @author SlavSquatSuperstar
 */
object Logger {

    private val logLevel: Int // minimum priority to print messages to console
    private val saveLogs: Boolean

    // Log File Output
    private var logFilename: String? = null
    private var logFile: TextFile? = null

    init {
        if (!Initializer.INIT_ASSETS || !Initializer.INIT_PREFERENCES) Initializer.init() // should prevent file from not being read

        logLevel = Preferences.LOG_LEVEL
        saveLogs = Preferences.SAVE_LOGS

        if (saveLogs) {
            // Create the log directory if needed
            val logsDirectory = File(Preferences.LOGS_DIRECTORY)
            if (!logsDirectory.exists()) logsDirectory.mkdir()

            // Count number of log files with the same date
            val today = LocalDate.now()
            var logCount = 0
            for (f in logsDirectory.listFiles()!!)
                if (f.name.startsWith(today.toString())) logCount++

            logFilename = "${logsDirectory.path}/$today-${++logCount}.log"
            logFile = TextFile(logFilename)
        }
    }

    private fun logInternal(msg: Any?, vararg args: Any?, level: LogLevel) {
        val output = StringBuilder("[%02d:%.4f] ".format((Time.time / 60).toInt(), Time.time % 60)) // Time stamp
        try {
            output.append(msg.toString().format(*args)) // Level prefix
            if (saveLogs) logFile?.append(output.toString()) // Always save to log regardless of level
        } catch (e: IllegalFormatException) {
            output.append("Logger: Could not format message \"$msg\"")
        } finally {
            if (level.level >= this.logLevel) { // Print to console if high enough level
                if (level == LogLevel.WARNING) System.err.println(output.toString())
                else println(output.toString())
            }
        }
    }

    /**
     * Prints a formatted message to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun log(msg: Any?, vararg args: Any?) = logInternal(msg, *args, level = LogLevel.NORMAL)

    /**
     * Prints a debug message to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun trace(msg: Any?, vararg args: Any?) = logInternal("[DEBUG] $msg", *args, level = LogLevel.TRACE)

    /**
     * Prints a warning to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) = logInternal("[WARNING] $msg", *args, level = LogLevel.WARNING)

    @JvmStatic
    fun printExitMessage() {
        if (saveLogs) log("Logger: Saved log to file \"%s\"", logFilename)
    }

    private enum class LogLevel(val level: Int) {
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