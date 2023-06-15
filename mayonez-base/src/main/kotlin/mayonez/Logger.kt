package mayonez

import mayonez.init.*
import mayonez.io.text.*
import java.io.File
import java.time.LocalDate
import java.util.*

/**
 * Prints messages to the console that can be formatted and assigned a
 * priority level. All log messages are saved to a log file.
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
        if (initialized) return

        this.config = config
        initialized = true
        createLogFile()
    }

    private fun createLogFile() {
//        if (!Mayonez.INIT_ASSETS) Mayonez.init() // Should prevent file from not being read
        if (this::logFile.isInitialized) return

        if (config.saveLogs) {
            logFile = TextFile(getLogFilename())
            while (printQueue.isNotEmpty()) logFile.append(printQueue.poll()) // log everything in print queue
        }
    }

    private fun getLogFilename(): String {
        val logDirectory = File(config.logDirectory)
        if (!logDirectory.exists()) logDirectory.mkdir()

        // Count number of log files with the same date
        val today = LocalDate.now().toString()
        var logCount = logDirectory.listFiles()!!
            .count { f -> f.name.startsWith(today) }

        // Set name of log file as YYYY-MM-DD_#
        return "${logDirectory.path}/${today}_${++logCount}.log"
    }

    // Private Log Methods

    /**
     * Formats a message and adds a timestamp, prints it to the console if
     * the priority is over the log level, and saves it to a log file if the
     * settings allow.
     *
     * @param msg the message as a Java format string
     * @param args (optional) the format arguments
     * @param level the log priority level
     */
    private fun logMessage(msg: Any?, vararg args: Any?, level: LogLevel) {
        val message = msg.formatMessage(args, level)
        if (level.ordinal >= config.logLevel) message.printToConsole(level)
        if (config.saveLogs) message.appendToFile()
    }

    private fun Any?.formatMessage(args: Array<out Any?>, level: LogLevel): String {
        val fmt = StringBuilder("[${Mayonez.seconds.toFmtString()}] ") // Timestamp
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
    fun log(msg: Any?, vararg args: Any?) = logMessage(msg, *args, level = LogLevel.INFO)

    /**
     * Prints a low-priority debug message to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun debug(msg: Any?, vararg args: Any?) = logMessage(msg, *args, level = LogLevel.DEBUG)

    /**
     * Prints a high-priority warning to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun warn(msg: Any?, vararg args: Any?) = logMessage(msg, *args, level = LogLevel.WARNING)

    /**
     * Prints a severe-priority error to the console.
     *
     * @param msg an object or formatted string
     * @param args (optional) string format arguments
     */
    @JvmStatic
    fun error(msg: Any?, vararg args: Any?) = logMessage(msg, *args, level = LogLevel.ERROR)

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
     * Returns a String version of an [java.lang.Exception]'s stack trace.
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