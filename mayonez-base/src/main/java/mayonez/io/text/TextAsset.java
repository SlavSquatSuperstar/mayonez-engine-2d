package mayonez.io.text;

import mayonez.*;
import mayonez.io.*;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A superclass for all text-based files used in this program.
 *
 * @author SlavSquatSuperstar
 */
public abstract class TextAsset extends Asset {

    public TextAsset(String filename) {
        super(filename);
    }

    /**
     * Reads text from this file as a single string. Will return nothing if the file does not exist.
     *
     * @return the text data as a string
     */
    protected String readText() {
        try (var in = inputStream()) {
            return IOUtils.readString(in);
        } catch (FileNotFoundException e) {
            Logger.error("File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return "";
    }

    /**
     * Reads text from this file line by line. Will return nothing if the file does not exist.
     *
     * @return the text data as an array
     */
    protected String[] readLines() {
        try (var in = inputStream()) {
            return IOUtils.readLines(in);
        } catch (FileNotFoundException e) {
            Logger.error("File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return new String[]{};
    }

    /**
     * Saves text to this file. Classpath resources cannot be written to.
     *
     * @param append whether to add data to the file instead of overwriting it
     * @param text   the text or lines of text to save
     */
    protected void save(boolean append, String... text) {
        try (var out = outputStream(append)) {
            if (text.length == 1) IOUtils.writeString(out, text[0]); // single line
            else IOUtils.writeLines(out, text);
        } catch (FileNotFoundException e) {
            Logger.error("File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("Could not save to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }

}
