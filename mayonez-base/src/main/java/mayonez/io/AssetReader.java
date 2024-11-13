package mayonez.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads data from a file in a specified format.
 *
 * @param <T> what data type to read
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface AssetReader<T> {

    /**
     * Reads data from the asset and closes the input stream.
     *
     * @param input the input stream
     * @return the data as the given type
     * @throws java.io.IOException           if the file cannot be read
     * @throws java.io.FileNotFoundException if the file does not exist
     */
    T read(InputStream input) throws IOException;

}
