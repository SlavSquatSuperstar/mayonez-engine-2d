package mayonez.io.text;

import mayonez.*;
import mayonez.io.*;

import java.io.IOException;

/**
 * A plain-text file (usually .txt) that can be read from and written to.
 *
 * @author SlavSquatSuperstar
 */
public class TextFile extends Asset {

    public TextFile(String filename) {
        super(filename);
    }

    /**
     * Reads text from this file as a single string.
     *
     * @return the text as a string, empty if the file does not exist
     */
    public String readText() {
        try {
            return new TextIOManager().read(openInputStream());
        } catch (IOException e) {
            Logger.error("Could not read file \"%s\"", getFilename());
            return "";
        }
    }

    /**
     * Reads text from this file line by line.
     *
     * @return the text as an array, empty if the file does not exist
     */
    public String[] readLines() {
        try {
            return new LinesIOManager().read(openInputStream());
        } catch (IOException e) {
            Logger.error("Could not read file \"%s\"", getFilename());
            return new String[0];
        }
    }

    /**
     * Saves any number of lines of text to this file, overwriting its contents.
     *
     * @param text the lines of text to write
     */
    public void write(String... text) {
        saveText(text, false);
    }

    /**
     * Adds any number of lines of text to this file, preserving its contents.
     *
     * @param text the lines of text to append
     */
    public void append(String... text) {
        saveText(text, true);
    }

    private void saveText(String[] text, boolean append) {
        try {
            if (text.length == 1) {
                new TextIOManager().write(openOutputStream(append), text[0]);
            } else {
                new LinesIOManager().write(openOutputStream(append), text);
            }
        } catch (IOException e) {
            Logger.error("Could not save to file \"%s\"", getFilename());
        }
    }

}
