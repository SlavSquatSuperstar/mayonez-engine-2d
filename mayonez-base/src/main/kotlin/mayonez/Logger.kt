package mayonez

import mayonez.assets.FilePath
import mayonez.assets.text.*
import mayonez.config.*
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDate
import java.util.*

/**
 * Prints formatted messages to the console and saves them to a log file for future use.
 * Each message can be assigned a [LogLevel] to indicate its priority.
 *
 * Usage: Call [Logger.log] anywhere to print a normal message to the console.
 * Log messages can also use format specifiers and arguments, similar to [String.format].
 * Different log priorities are also available through the [Logger.debug], [Logger.warn],
 * and [Logger.error] methods.
 *
 * The log level indicates the severity of a message. Messages with level [LogLevel.WARN]
 * and above are printed to standard output. The user can set the minimum log level,
 * [LogLevel.INFO] by default, required for a message to be displayed in `preferences.json`. The special log levels
 * [LogLevel.ALL] allows messages to be printed, and [LogLevel.NONE] disables all log
 * messages. See [LogLevel] for more information.
 *
 * By default, log files are saved to the `logs/` folder inside the working directory,
 * but the user can change the location or disable log output files in preferences.
 *
 * @author SlavSquatSuperstar
 */
object Logger {

    private const val STACK_TRACE_INDEX = 5 // Elements to jump up stack trace

    // Logger Config
    private var initialized: Boolean = false
    private var config: LoggerConfig = LoggerConfig.DEFAULT_CONFIG

    // Log File Output
    private lateinit var logFileStream: OutputStream
    private val printQueue: Queue<String> = ArrayDeque()

    // Logger Init Methods

    internal fun setConfig(config: LoggerConfig) {
        if (!initialized) {
            Logger.config = config
            debug("The log level has been set to ${config.logLevel}")
            try {
                createLogFile()
                initialized = true
            } catch (_: IOException) {
                // Could not create log file
                initialized = false
            }
        }
    }

    internal fun shutdown() {
        if (initialized) {
            logFileStream.close()
            initialized = false
        }
    }

    @Throws(IOException::class)
    private fun createLogFile() {
        if (this::logFileStream.isInitialized) return
        if (config.saveLogs) {
            val logFilePath = FilePath.of(getLogFilePath())
            logFileStream = logFilePath.openOutputStream(true)
            while (printQueue.isNotEmpty()) {
                // Log everything in print queue
                TextIOUtils.write(logFileStream, printQueue.poll())
            }
        }
    }

    private fun getLogFilePath(): String {
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
        if (level < config.logLevel) return // Check if log level high enough
        val message = msg.formatMessage(args, level)
        message.printToConsole(level)
        if (config.saveLogs) message.appendToFile()
    }

    private fun Any?.formatMessage(args: Array<out Any?>, level: LogLevel): String {
        val fmt = StringBuilder("[${Time.getTotalProgramSeconds().toFmtString()}] ") // Timestamp
        fmt.append("[${level.name}] ") // Log level
        fmt.append("[${getStackSource()}] ") // Log source
        try {
            fmt.append(this.toString().format(*args))
        } catch (_: IllegalFormatException) {
            fmt.append("Logger: Could not format message \"$this\"")
        }
        return fmt.toString()
    }

    /** Returns a timestamp in hh:mm:ss.SSSS format. */
    private fun Float.toFmtString(): String {
        val min: Int = (this / 60).toInt()
        return "%02d:%02d:%07.4f".format(min / 60, min, this % 60)
    }

    /** Prints a message to stdout or stderr. */
    private fun String.printToConsole(level: LogLevel) {
        // Use stderr (red text) for errors
        if (level >= LogLevel.WARN) System.err.println(this)
        else println(this)
    }

    private fun String.appendToFile() {
        // Check if log file is created
        if (initialized) TextIOUtils.write(logFileStream, this)
        else printQueue.offer(this) // Save to buffer
    }

    // Public Log Methods

    /**
     * Prints a minor debug message to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun trace(msg: Any?, vararg args: Any?) {
        printFormattedMessage(msg, *args, level = LogLevel.TRACE)
    }

    /**
     * Prints a low-priority debug message to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun debug(msg: Any?, vararg args: Any?) {
        printFormattedMessage(msg, *args, level = LogLevel.DEBUG)
    }

    /**
     * Prints a normal-priority informational message to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun log(msg: Any?, vararg args: Any?) {
        printFormattedMessage(msg, *args, level = LogLevel.INFO)
    }

    /**
     * Prints a high-priority warning to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) {
        printFormattedMessage(msg, *args, level = LogLevel.WARN)
    }

    /**
     * Prints a severe-priority error to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun error(msg: Any?, vararg args: Any?) {
        printFormattedMessage(msg, *args, level = LogLevel.ERROR)
    }

    /**
     * Prints a critical-priority error to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun fatal(msg: Any?, vararg args: Any?) {
        printFormattedMessage(msg, *args, level = LogLevel.FATAL)
    }

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