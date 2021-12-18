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

    private val saveLogs = Preferences.SAVE_LOGS
    private lateinit var logFilename: String
    private var logFile: TextFile? = null

    init {
        if (saveLogs) {
            // Create the log directory if needed
            val logsDirectory = File(Preferences.LOGS_DIRECTORY)
            if (!logsDirectory.exists())
                logsDirectory.mkdir()

            // Count number of log files with the same date
            val today = LocalDate.now()
            var logCount = 0
            for (f in logsDirectory.listFiles()!!)
                if (f.name.startsWith(today.toString()))
                    logCount++

            logFilename = "${logsDirectory.path}/$today-${++logCount}.log"
            logFile = TextFile(logFilename, false)
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
        val output = StringBuilder("[%02d:%.4f] ".format((Game.getTime() / 60).toInt(), Game.getTime() % 60))
        try {
            output.append(msg.toString().format(*args))
            if (saveLogs)
                logFile!!.append(output.toString())
        } catch (e: IllegalFormatException) {
            output.append("Logger: Could not format message \"$msg\"")
        } finally {
            println(output.toString())
        }
    }

    /**
     * Prints a warning to the console.
     *
     * @param msg  an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) {
        log("[WARNING] $msg", *args)
    }

    @JvmStatic
    fun printExitMessage() {
        if (saveLogs) log("Logger: Saved log to file \"%s\"", logFilename)
    }

}