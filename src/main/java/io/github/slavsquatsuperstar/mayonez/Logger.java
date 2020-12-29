package io.github.slavsquatsuperstar.mayonez;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Logger {

	public static String outputPath;
	private static BufferedWriter writer;

	static {
		// create the log directory if needed
		File logDirectory = new File("logs/");
		if (!logDirectory.exists())
			logDirectory.mkdir();

		// count number of log files with the same date
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

	public static void log(Object msg, Object... args) {
		String output = String.format("[%s] %s", LocalTime.now(), msg.toString().formatted(args));
		System.out.println(output);

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
