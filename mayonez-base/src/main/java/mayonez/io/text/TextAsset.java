package mayonez.io.text;

import mayonez.Logger;
import mayonez.io.Asset;
import mayonez.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
     * Reads data from this file as a single string. Should not be called if this file has not been created and is meant
     * for output only.
     *
     * @return the text data as a string
     */
    protected final String read() {
        try (InputStream in = inputStream()) {
            return IOUtils.read(in);
        } catch (FileNotFoundException e) {
            Logger.error("File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return "";
    }

    /**
     * Reads data from this file as a string array. Should not be called if this file has not been created and is meant
     * for output only.
     *
     * @return the text data as an array
     */
    protected final String[] readArray() {
        try (InputStream in = inputStream()) {
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
     * Saves data to this file. Will not work if this asset is a classpath resource.
     *
     * @param append whether to add data to the file instead of overwriting it
     * @param text   the text or lines of text to save
     */
    protected final void save(boolean append, String... text) {
        try (OutputStream out = outputStream(append)) {
            if (text.length == 1) IOUtils.write(out, text[0]); // single line
            else IOUtils.writeLines(out, text);
        } catch (FileNotFoundException e) {
            Logger.error("File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("Could not save to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }


}
