package com.slavsquatsuperstar.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.slavsquatsuperstar.mayonez.Logger;

// TODO one method for handling exceptions
// TODO store text in class
public class TextFile {

    private String filename;

    public TextFile(String filename) {
        this.filename = filename;
        // TODO assert file exists?
    }

    /**
     * Reads plain text data from this file.
     *
     * @return The body of text read from this file.
     */
    public String readText() {
        StringBuilder text = new StringBuilder("");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            for (String textLine = reader.readLine(); textLine != null; textLine = reader.readLine())
                text.append(textLine + "\n");
            Logger.log("TextFile: Read successful");
            reader.close();
        } catch (FileNotFoundException e) {
        	Logger.log("TextFile: File \"%s\" not found\n", filename);
        } catch (IOException e) {
        	Logger.log("TextFile: Could not read file");
        }
        return text.toString();
    }

    /**
     * Reads text from this file line by line.
     *
     * @return An array of lines read from this file.
     */
    public String[] readLines() {
        return readText().split("\n");
    }

    /**
     * Saves any number of lines of text to this file, overwriting its contents.
     *
     * @param text The line or lines to save.
     */
    public void save(String... text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            //TODO what to do if file not found
            for (String line : text) {
                writer.write(line);
                writer.newLine();
            }
			Logger.log("TextFile: Save successful");
            writer.close();
        } catch (IOException e) {
            Logger.log("TextFile: Could not save to file");
        } catch (NullPointerException e) {
            Logger.log("TextFile: File contents empty");
        }
    }

    /**
     * Adds any number of lines of text to this file, preserving its contents.
     *
     * @param text The line or lines to append.
     */
    public void append(String... text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            for (String line : text) {
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            Logger.log("TextFile: Could not append to file");
        } catch (NullPointerException e) {
            Logger.log("TextFile: File contents empty");
		}
	}

}
