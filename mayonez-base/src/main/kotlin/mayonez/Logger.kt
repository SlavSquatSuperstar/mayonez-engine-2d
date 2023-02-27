package mayonez

import mayonez.io.text.TextFile
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

    // Log Parameters
    private var minLogLevel: Int = Preferences.logLevel // Minimum priority required to print messages to console
    private var saveLogs: Boolean = Preferences.saveLogs // Whether to save console messages to a log file

    // Log File Output
    private var logFilename: String = "<No log file>"
    private lateinit var logFile: TextFile
    private val printQueue: Queue<String> = LinkedList() // Save log messages in case log file isn't created

    // Logger Init Methods

    internal fun initLogger() {
        if (!Mayonez.INIT_ASSETS || !Mayonez.INIT_PREFERENCES) Mayonez.init() // Should prevent file from not being read

        minLogLevel = Preferences.logLevel
        saveLogs = Preferences.saveLogs

        if (saveLogs && !Mayonez.INIT_LOGGER) {
            createLogFile()
            while (printQueue.isNotEmpty()) logFile.append(printQueue.poll()) // log everything in print queue
        }
    }

    private fun createLogFile() {
        logFilename = getLogFilename()
        logFile = TextFile(logFilename)
        log("Logger: Created log file \"%s\"", logFilename)
    }

    private fun getLogFilename(): String {
        val logDirectory = File(Preferences.logDirectory)
        if (!logDirectory.exists()) logDirectory.mkdir()

        // Count number of log files with the same date
        val today = LocalDate.now().toString()
        var logCount = 0
        for (f in logDirectory.listFiles()!!)
            if (f.name.startsWith(today)) logCount++

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
        if (level.ordinal >= minLogLevel) message.printToConsole(level)
        if (saveLogs) message.appendToFile()
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
        if (Mayonez.INIT_LOGGER) logFile.append(this) // has log file been created
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

    override fun toString(): String = "Logger ($logFilename)"

}