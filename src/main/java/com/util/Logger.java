package com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.mayonez.Game;

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
		String output = String.format("[%.6f] %s", Game.getTime(), String.format(msg.toString(), args));
		System.out.println(output);

		if (saveLogs) {
			try {
				writer.write(output);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				System.out.println("Error: Could not write to file");
				e.printStackTrace();
			}
		}

	}

	public static void log(Object msg) {
		log(msg, new Object[0]);
	}

}
