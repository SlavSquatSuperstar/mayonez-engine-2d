package mayonez.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Saves or appends data to a file in a specified format.
 *
 * @param <T> what data type to read
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface AssetWriter<T> {

    /**
     * Writes data to the asset and closes the output stream.
     *
     * @param output the output stream
     * @param data   data of the given type
     * @throws java.io.IOException           if the file cannot be saved
     * @throws java.io.FileNotFoundException if the file does not exist
     */
    void write(OutputStream output, T data) throws IOException;

}
