package mayonez

import mayonez.assets.text.*
import mayonez.config.*
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Prints messages to the console that can be formatted and assigned a
 * priority level, and saves them to a log file.
 *
 * Usage: Call [Logger.log] anywhere to print an info message to the console.
 * Log messages can also use format specifiers and arguments, similar to [String.format].
 * Different log priorities are also available through [Logger.debug], [Logger.warn], and
 * [Logger.error]. By default, log files are saved to the `logs/` folder, but the user can
 * change the location or disable log output files in `preferences.json`.
 *
 * The log level controls which messages are visible to the console. For example, a log level
 * of 3 ([LogLevel.WARNING]) indicates only messages with severity `WARNING` or above will be
 * printed. Setting a priority to 0 ([LogLevel.ALL]) or 5 ([LogLevel.NONE]) will also allow
 * all or none of the message to be displayed. All messages will be written to the output file
 * regardless of priority. The default log level is also 2 (`INFO` or above), and is also
 * changeable in the preferences. The See [LogLevel] for more information.
 *
 * @author SlavSquatSuperstar
 */
object Logger {

    private const val STACK_TRACE_INDEX = 5 // number of times to jump up stack trace

    // Logger Config
    private var initialized: Boolean = false
    private var config: LoggerConfig = LoggerConfig.DEFAULT_CONFIG

    // Log File Output
    private lateinit var logFile: TextFile
    private val printQueue: Queue<String> = LinkedList() // Save log messages in case log file isn't created

    // Logger Init Methods

    internal fun setConfig(config: LoggerConfig) {
        if (!initialized) {
            Logger.config = config
            debug("The log level has been set to ${config.logLevel}")
            createLogFile()
            initialized = true
        }
    }

    internal fun shutdown(status: Int) {
        if (!initialized) return
        val message = "Exited program with code $status"
        if (status == 0) log("$message (Success)")
        else error("$message (Error)")
        logFile.free()
        initialized = false
    }

    private fun createLogFile() {
        if (this::logFile.isInitialized) return
        if (config.saveLogs) {
            logFile = TextFile(getLogFilename())
            logFile.setAutoClose(false)
            while (printQueue.isNotEmpty()) {
                logFile.append(printQueue.poll()) // log everything in print queue
            }
        }
    }

    private fun getLogFilename(): String {
        val logDirectory = File(config.logDirectory)
        if (!logDirectory.exists()) logDirectory.mkdir()

        val today = LocalDate.now().toString()
        var logFilesCountToday = logDirectory.listFiles()!!
            .count { f -> f.name.startsWith(today) }

        // Set output filename as YYYY-MM-DD_#.log
        return "${logDirectory.path}/${today}_${++logFilesCountToday}.log"
    }

    // Private Log Methods

    /**
     * Formats a message and adds a timestamp, and prints it to the console if
     * the priority is over the log levels. If log output files are enabled, then
     * the message will also be saved to a log file.
     *
     * @param msg the message as a Java format string
     * @param args (optional) the format arguments
     * @param level the log priority level
     */
    private fun printFormattedMessage(msg: Any?, vararg args: Any?, level: LogLevel) {
        val message = msg.formatMessage(args, level)
        if (level.level >= config.logLevel) message.printToConsole(level)
        if (config.saveLogs) message.appendToFile()
    }

    private fun Any?.formatMessage(args: Array<out Any?>, level: LogLevel): String {
        val fmt = StringBuilder("[${Time.getTotalProgramSeconds().toFmtString()}] ") // Timestamp
        fmt.append("[${level.name}] ") // Log level
        fmt.append("[${getStackSource()}] ") // Log source
        try {
            fmt.append(this.toString().format(*args)) // Level prefix
        } catch (e: IllegalFormatException) {
            fmt.append("Logger: Could not format message \"$this\"")
        }
        return fmt.toString()
    }

    /** Returns a timestamp in hh:mm:ss.SSSS format. */
    private fun Float.toFmtString(): String {
        val min: Int = (this / 60).toInt()
        return "%02d:%02d:%07.4f".format(min / 60, min, this % 60)
    }

    private fun String.printToConsole(level: LogLevel) {
        if (level >= LogLevel.WARNING) System.err.println(this) // red text for errors
        else println(this)
    }

    private fun String.appendToFile() {
        if (initialized) logFile.append(this) // has log file been created
        else printQueue.offer(this) // save to buffer
    }

    // Public Log Methods

    /**
     * Prints a normal-priority informational message to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun log(msg: Any?, vararg args: Any?) = printFormattedMessage(msg, *args, level = LogLevel.INFO)

    /**
     * Prints a low-priority debug message to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun debug(msg: Any?, vararg args: Any?) = printFormattedMessage(msg, *args, level = LogLevel.DEBUG)

    /**
     * Prints a high-priority warning to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) = printFormattedMessage(msg, *args, level = LogLevel.WARNING)

    /**
     * Prints a severe-priority error to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun error(msg: Any?, vararg args: Any?) = printFormattedMessage(msg, *args, level = LogLevel.ERROR)

    // Stack Helper Methods

    /**
     * Returns the source class that called a log() function.
     *
     * Source: Azurite util.Log.source()
     */
    private fun getStackSource(): String {
        return Thread.currentThread().stackTrace[STACK_TRACE_INDEX].className
    }

    /**
     * Converts a [java.lang.Exception]'s stack trace to a string.
     *
     * @param throwable a throwable object, i.e. an Exception
     * @return the stack trace
     */
    @JvmStatic
    fun getStackTrace(throwable: Throwable): String = throwable.stackTraceToString()

    /** Logs the stack trace of an exception with an "Error" priority level. */
    @JvmStatic
    fun printStackTrace(throwable: Throwable) = error(getStackTrace(throwable))

    override fun toString(): String {
        return "Logger (Save Logs = ${config.saveLogs}, Log Level = ${config.logLevel})"
    }

}