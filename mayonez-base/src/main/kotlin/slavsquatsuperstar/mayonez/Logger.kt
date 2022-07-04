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

    // TODO eliminate variables or allow mutable
    // Log Parameters
    private var logLevel: Int // Minimum priority required to print messages to console
    private var saveLogs: Boolean // Whether to save console messages to a log file

    // Log File Output
    private var logFilename: String = "<No log file>"
    private lateinit var logFile: TextFile
    private var printQueue: Queue<String> = LinkedList() // Buffer for writing messages to log file


    init {
        logLevel = Preferences.logLevel
        saveLogs = Preferences.saveLogs
    }

    internal fun createLogFile() {
        if (!Mayonez.INIT_ASSETS || !Mayonez.INIT_PREFERENCES) Mayonez.init() // should prevent file from not being read

        logLevel = Preferences.logLevel
        saveLogs = Preferences.saveLogs

        if (saveLogs && !Mayonez.INIT_LOGGER) {
            val logDirectory = File(Preferences.logDirectory)
            if (!logDirectory.exists()) logDirectory.mkdir()

            // Count number of log files with the same date
            val today = LocalDate.now()
            var logCount = 0
            for (f in logDirectory.listFiles()!!)
                if (f.name.startsWith(today.toString())) logCount++

            // Set name of log file as YYYY-MM-DD_#
            logFilename = "${logDirectory.path}/${today}_${++logCount}.log"
            logFile = TextFile(logFilename)

            while (printQueue.isNotEmpty()) logFile.append(printQueue.poll()) // log everything in print queue
        }
    }

    private fun printMessage(msg: Any?, vararg args: Any?, level: LogLevel) {
        // Format the message and add timestamp
        val time = Mayonez.time
        val fmt = StringBuilder("[%02d:%02.3f] ".format((time / 60).toInt(), time % 60)) // Timestamp
        try {
            fmt.append(msg.toString().format(*args)) // Level prefix
        } catch (e: IllegalFormatException) {
            fmt.append("Logger: Could not format message \"$msg\"")
        }
        val output = fmt.toString()

        // Print message and write to file
        if (level.level >= this.logLevel) { // Print to console if high enough level
            if (level == LogLevel.WARNING) System.err.println(fmt.toString())
            else println(output)
        }
        // Always save to log regardless of level
        if (saveLogs) {
            if (Mayonez.INIT_LOGGER) logFile.append(output) // log file has been created
            else printQueue.offer(output) // save to buffer
        }

    }

    /**
     * Prints a normal-priority informational message to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun log(msg: Any?, vararg args: Any?) = printMessage(msg, *args, level = LogLevel.NORMAL)

    /**
     * Prints a low-priority debug message to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun trace(msg: Any?, vararg args: Any?) = printMessage("[DEBUG] $msg", *args, level = LogLevel.TRACE)

    /**
     * Prints a high-priority warning to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) = printMessage("[WARNING] $msg", *args, level = LogLevel.WARNING)

    @JvmStatic
    fun printExitMessage() {
        if (saveLogs) log("Logger: Saved log to file \"%s\"", logFilename)
    }

    override fun toString(): String {
        return "Logger ($logFilename)"
    }

    private enum class LogLevel(val level: Int) {
        /**
         * A low-priority debug message.
         */
        TRACE(0),

        /**
         * A normal-priority informational message.
         */
        NORMAL(1),

        /**
         * A high-priority warning message
         */
        WARNING(2)
    }

}