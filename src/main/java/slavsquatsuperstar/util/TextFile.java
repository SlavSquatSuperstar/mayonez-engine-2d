package slavsquatsuperstar.util;

import slavsquatsuperstar.mayonez.Assets;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

// TODO one method for handling exceptions
// TODO store text in class
// TODO manipulate config file
public class TextFile {

    private final String filename;

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
        String text = "";
        try (FileInputStream in = FileUtils.openInputStream(FileUtils.toFile(Assets.getFile(filename)))) {
            text = IOUtils.toString(in, Preferences.CHARSET);
            Logger.log("TextFile: Read successful");
        } catch (FileNotFoundException e) {
            Logger.log("TextFile: File \"%s\" not found\n", filename);
        } catch (IOException e) {
            Logger.log("TextFile: Could not read file");
        }
        return text;
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
        try (FileOutputStream out = FileUtils.openOutputStream(FileUtils.toFile(Assets.getFile(filename)), false)) {
            IOUtils.writeLines(Arrays.asList(text), "\n", out, StandardCharsets.UTF_8);
            Logger.log("TextFile: Save successful");
        } catch (FileNotFoundException e) {
            Logger.log("TextFile: File \"%s\" not found\n", filename);
        } catch (IOException e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            Logger.log("TextFile: Could not save to file");
        }
    }

    /**
     * Adds any number of lines of text to this file, preserving its contents.
     *
     * @param text The line or lines to append.
     */
    public void append(String... text) {
        try (FileOutputStream out = FileUtils.openOutputStream(FileUtils.toFile(Assets.getFile(filename)), true)) {
            IOUtils.writeLines(Arrays.asList(text), "\n", out, StandardCharsets.UTF_8);
            Logger.log("TextFile: Save successful");
        } catch (IOException e) {
            Logger.log("TextFile: Could not append to file");
        } catch (NullPointerException e) {
            Logger.log("TextFile: File contents empty");
        }
    }

}
