package slavsquatsuperstar.mayonez

import slavsquatsuperstar.mayonez.assets.TextFile
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Prints messages to the console that can be formatted and given a priority level.
 *
 * @author SlavSquatSuperstar
 */
class Logger private constructor() {
    companion object {

        @JvmField
        val saveLogs = Preferences.SAVE_LOGS

        @JvmField
        var logFilename: String? = null
        private var logFile: TextFile? = null

        init {
            if (saveLogs) {
                // Create the log directory if needed
                val logDirectory = File("logs/")
                if (!logDirectory.exists())
                    logDirectory.mkdir()

                // Count number of log files with the same date
                val today = LocalDate.now()
                var logCount = 0
                for (f in logDirectory.listFiles())
                    if (f.name.startsWith(today.toString()))
                        logCount++

                logFilename = "${logDirectory.path}/$today-${++logCount}.log"
                logFile = TextFile(logFilename, false)
            }
        }

        /**
         * Prints a formatted message to the console.
         *
         * @param fmt  a formatted string
         * @param args (optional) string format arguments
         */
        @JvmStatic
        fun log(fmt: Any?, vararg args: Any?) {
            val output = StringBuilder("[%02d:%.4f] ".format((Game.getTime() / 60).toInt(), Game.getTime() % 60))
            try {
                output.append(fmt.toString().format(*args))
                if (saveLogs)
                    logFile!!.append(output.toString())
            } catch (e: IllegalFormatException) {
                output.append("Logger: Could not format message \"$fmt\"")
            } finally {
                println(output.toString())
            }
        }

        /**
         * Prints a message to the console.
         *
         * @param msg an object
         */
        @JvmStatic
        fun log(msg: Any?) {
            log(msg, null)
        }

        /**
         * Prints a warning to the console.
         *
         * @param msg an object
         */
        @JvmStatic
        fun warn(msg: Any?, vararg args: Any?) {
            log("[WARNING] $msg", *args)
        }

    }
}