package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.assets.TextFile;

import java.io.File;
import java.time.LocalDate;
import java.util.IllegalFormatException;

/**
 * Prints messages to the console that can be formatted and given a priority level.
 *
 * @author SlavSquatSuperstar
 */
public final class Logger {

    public static boolean saveLogs = true;
    static String logFilename;
    private static TextFile logFile;

    static {
        if (saveLogs) {
            // Create the log directory if needed
            File logDirectory = new File("logs/");
            if (!logDirectory.exists())
                logDirectory.mkdir();

            // Count number of log files with the same date
            LocalDate today = LocalDate.now();
            int logCount = 0;
            for (File f : logDirectory.listFiles())
                if (f.getName().startsWith(today.toString()))
                    logCount++;

            logFilename = String.format("%s/%s-%d.log", logDirectory.getPath(), today, ++logCount);
            logFile = new TextFile(logFilename, false);
        }
    }

    private Logger() {}

    /**
     * Prints a formatted message to the console.
     *
     * @param msg  a formatted string
     * @param args (optional) string format arguments
     */
    public static void log(Object msg, Object... args) {
        String output = String.format("[%02d:%.4f] ", (int) (Game.getTime() / 60), Game.getTime() % 60);
        try {
            output += String.format("%s", String.format(msg.toString(), args));
            if (saveLogs)
                logFile.append(output);
        } catch (NullPointerException e) {
            output += "(null)";
        } catch (IllegalFormatException e) {
            output += String.format("Logger: Could not format message \"%s\"", msg);
        } finally {
            System.out.println(output);
        }
    }

    /**
     * Prints a message to the console.
     *
     * @param msg an object
     */
    public static void log(Object msg) {
        log(msg, new Object[0]);
    }

    public static void warn(Object msg, Object... args) {
        log("[WARNING] " + msg, args);
    }

}
