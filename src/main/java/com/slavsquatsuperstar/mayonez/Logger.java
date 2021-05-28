package com.slavsquatsuperstar.mayonez;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.IllegalFormatException;

public final class Logger {

	private Logger() {
	}

	public static boolean saveLogs = false;
	public static String outputPath;
	private static BufferedWriter writer;

	static {
		if (saveLogs) {
			// Create the log directory if needed
			File logDirectory = new File("logs/");
			if (!logDirectory.exists())
				logDirectory.mkdir();

			// Count number of log files with the same date
			LocalDate today = LocalDate.now();
			int count = 0;
			for (File f : logDirectory.listFiles())
				if (f.getName().startsWith(today.toString()))
					count++;

			outputPath = String.format("%s/%s-%d.log", logDirectory.getPath(), today, ++count);

			try {
				writer = new BufferedWriter(new FileWriter(outputPath, true));
			} catch (IOException e) {
				System.out.println("Error: Could not open file");
				e.printStackTrace();
			}
		}
	}

	public static void log(Object msg, Object... args) {
		String output = String.format("[%.4f] ", Game.getTime());

		try {
			output += String.format("%s", String.format(msg.toString(), args));

			if (saveLogs) {
				writer.write(output);
				writer.newLine();
				writer.flush();
			}
		} catch (NullPointerException e) {
			output += "Logger: Null message";
		} catch (IllegalFormatException e) {
			output += "Logger: Could not format message";
		} catch (IOException e) {
			output += "Logger: Could not write to log file";
		} finally {
			System.out.println(output);
		}
	}

	public static void log(Object msg) {
		log(msg, new Object[0]);
	}

}
