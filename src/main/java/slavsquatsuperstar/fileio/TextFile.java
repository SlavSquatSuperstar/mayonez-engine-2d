package slavsquatsuperstar.fileio;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

// TODO manipulate config file

/**
 * Facilitates reading from and saving data to a plain text file.
 *
 * @author SlavSquatSuperstar
 */
public class TextFile {

    private Asset file;

    public TextFile(String filename) {
        file = Assets.getAsset(filename);
        if (file == null)
            file = Assets.createAsset(filename, AssetType.LOCAL);
    }

    /**
     * Reads plain text data from this file.
     *
     * @return The body of text read from this file.
     */
    public String readText() {
        try (InputStream in = file.inputStream()) {
            return IOUtils.toString(in, Preferences.CHARSET);
        } catch (FileNotFoundException e) {
            Logger.warn("TextFile: File \"%s\" not found");
        } catch (IOException e) {
            Logger.warn("TextFile: Could not read file");
        }
        return "";
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
    public void write(String... text) {
        try (OutputStream out = file.outputStream(false)) {
            IOUtils.writeLines(Arrays.asList(text), "\n", out, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            Logger.warn("TextFile: File \"%s\" not found");
        } catch (IOException e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            Logger.warn("TextFile: Could not save to file");
        }
    }

    /**
     * Adds any number of lines of text to this file, preserving its contents.
     *
     * @param text The line or lines to append.
     */
    public void append(String... text) {
        try (OutputStream out = file.outputStream(true)) {
            IOUtils.writeLines(Arrays.asList(text), "\n", out, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.warn("TextFile: Could not append to file");
        }
    }

}
