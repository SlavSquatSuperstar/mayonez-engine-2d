package mayonez.io.text;

/**
 * A plain-text file (usually .txt) that can be read from and written to.
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
    @Override
    public String readText() {
        return super.readText();
    }

    /**
     * Reads text from this file line by line.
     *
     * @return the text as an array
     */
    @Override
    public String[] readLines() {
        return super.readLines();
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
