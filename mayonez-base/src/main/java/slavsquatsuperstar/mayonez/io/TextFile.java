package slavsquatsuperstar.mayonez.io;

import slavsquatsuperstar.mayonez.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO manipulate .cfg file

/**
 * Facilitates reading from and saving data to a plain text file.
 *
 * @author SlavSquatSuperstar
 */
public class TextFile extends Asset {

    public TextFile(String filename) {
        super(filename);
        Assets.setAsset(filename, this);
    }

    /**
     * Reads plain text data from this file.
     *
     * @return the text from the file
     */
    public String readText() {
        try (InputStream in = inputStream()) {
            return IOUtils.readText(in);
        } catch (FileNotFoundException e) {
            Logger.error("TextFile: File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("TextFile: Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return "";
    }

    /**
     * Reads text from this file line by line.
     *
     * @return an array of lines from the file
     */
    public String[] readLines() {
        try (InputStream in = inputStream()) {
            return IOUtils.readLines(in);
        } catch (FileNotFoundException e) {
            Logger.error("TextFile: File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("TextFile: Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return new String[] {""};
    }

    /**
     * Saves any number of lines of text to this file, overwriting its contents.
     *
     * @param text the lines of text to write
     */
    public void write(String... text) {
        try (OutputStream out = outputStream(false)) {
            IOUtils.writeLines(out, text);
        } catch (FileNotFoundException e) {
            Logger.error("TextFile: File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("TextFile: Could not save to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }

    /**
     * Adds any number of lines of text to this file, preserving its contents.
     *
     * @param text the lines of text to append
     */
    public void append(String... text) {
        try (OutputStream out = outputStream(true)) {
            IOUtils.writeLines(out, text);
        } catch (IOException e) {
            Logger.error("TextFile: Could not append to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }

}
