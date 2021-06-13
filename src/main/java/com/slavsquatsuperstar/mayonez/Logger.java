package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.util.TextFile;

import java.io.File;
import java.time.LocalDate;
import java.util.IllegalFormatException;

public final class Logger {

    public static boolean saveLogs;
    public static String outputFile;
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

            outputFile = String.format("%s/%s-%d.log", logDirectory.getPath(), today, ++logCount);
            logFile = new TextFile(outputFile);
        }
    }

    private Logger() {}

    public static void log(Object msg, Object... args) {
        String output = String.format("[%.4f] ", Game.getTime());
        try {
            output += String.format("%s", String.format(msg.toString(), args));
            if (saveLogs)
                logFile.append(output);
        } catch (NullPointerException e) {
            output += "null";
        } catch (IllegalFormatException e) {
            output += "Logger: Could not format message";
        } finally {
            System.out.println(output);
        }
    }

    public static void log(Object msg) {
        log(msg, new Object[0]);
    }

}
