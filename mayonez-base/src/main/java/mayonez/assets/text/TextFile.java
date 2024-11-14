package mayonez.assets.text;

import mayonez.*;
import mayonez.assets.*;
import mayonez.io.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A plain-text file (usually .txt) that can be read from and written to.
 *
 * @author SlavSquatSuperstar
 */
public class TextFile extends Asset {

    private OutputStream output;
    private boolean autoClose;

    public TextFile(String filename) {
        super(filename);
        autoClose = true;
    }

    /**
     * Sets whether this output streams should be closed after every write, true by
     * default. If false, the stream will be kept open for future write until the
     * file closes.
     *
     * @param autoClose whether to close the output stream
     */
    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    /**
     * Reads text from this file as a single string.
     *
     * @return the text as a string, empty if the file does not exist
     */
    public String readText() {
        try (var stream = openInputStream()) {
            return TextIOUtils.readText(stream);
        } catch (IOException e) {
            Logger.error("Could not read file %s", getFilenameInQuotes());
            return "";
        }
    }

    /**
     * Reads text from this file line by line.
     *
     * @return the text as an array, empty if the file does not exist
     */
    public String[] readLines() {
        try (var stream = openInputStream()) {
            return TextIOUtils.readLines(stream);
        } catch (IOException e) {
            Logger.error("Could not read file %s", getFilenameInQuotes());
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
            if (output == null) output = openOutputStream(append);
            TextIOUtils.write(output, text);
            if (autoClose) {
                output.close();
                output = null;
            }
        } catch (IOException e) {
            Logger.error("Could not save to file %s", getFilenameInQuotes());
        }
    }

    @Override
    public void free() {
        if (!autoClose && output != null) {
            try {
                output.close();
            } catch (IOException ignored) {
            }
        }
    }

}
