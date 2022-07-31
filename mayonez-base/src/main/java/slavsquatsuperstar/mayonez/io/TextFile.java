package slavsquatsuperstar.mayonez.io;

import slavsquatsuperstar.util.StringUtils;

// TODO support .csv and .cfg files

/**
 * Reading lines of text data and saves it to a plain text (usually .txt) file.
 *
 * @author SlavSquatSuperstar
 */
public class TextFile extends TextAsset {

    public TextFile(String filename) {
        super(filename);
    }

    /**
     * Reads text from this file as a single string.
     *
     * @return the text as a string
     */
    public String readText() {
        return super.read();
    }

    /**
     * Reads text from this file line by line.
     *
     * @return the text as an array
     */
    public String[] readLines() {
        return StringUtils.toLines(readText());
    }

    /**
     * Saves any number of lines of text to this file, overwriting its contents.
     *
     * @param text the lines of text to write
     */
    public void write(String... text) {
        super.save(false, text);
    }

    /**
     * Adds any number of lines of text to this file, preserving its contents.
     *
     * @param text the lines of text to append
     */
    public void append(String... text) {
        super.save(true, text);
    }

}
