package slavsquatsuperstar.mayonez.io;

import slavsquatsuperstar.mayonez.util.Logger;

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
     * Reads data from thus file. Should not be called if this file has not been created and is meant for output only.
     *
     * @return the text data as a string
     */
    protected String read() {
        try (InputStream in = inputStream()) {
            return IOUtils.read(in);
        } catch (FileNotFoundException e) {
            Logger.error("I/O: File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("I/O: Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return "";
    }

    /**
     * Saves data to this file. Will not work if this asset is a classpath resource.
     *
     * @param append whether to add data to the file instead of overwriting it
     * @param text   the text or lines of text to save
     */
    protected void save(boolean append, String... text) {
        try (OutputStream out = outputStream(append)) {
            if (text.length == 1) IOUtils.write(out, text[0]);
            else IOUtils.writeLines(out, text);
        } catch (FileNotFoundException e) {
            Logger.error("I/O: File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("I/O: Could not save to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }


}
