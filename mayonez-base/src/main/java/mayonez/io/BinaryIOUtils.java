package mayonez.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Reads and writes raw bytes to binary files.
 *
 * @author SlavSquatSuperstar
 */
public final class BinaryIOUtils {

    private BinaryIOUtils() {
    }

    /**
     * Reads data from a stream as an array of bytes.
     *
     * @param input the input stream
     * @return the binary data
     * @throws java.io.IOException if the file cannot be read
     */
    public static byte[] readBytes(InputStream input) throws IOException {
        if (input == null) {
            throw new IOException("File does not exist or is not readable");
        }
        try {
            return input.readAllBytes();
        } catch (IOException e) {
            throw new IOException("Error while reading file");
        }
    }

    /**
     * Writes data to a stream an array of bytes.
     *
     * @param output the output stream
     * @param bytes  the binary data
     * @throws java.io.IOException if the file cannot be written to
     */
    public static void writeBytes(OutputStream output, byte[] bytes) throws IOException {
        if (output == null) {
            throw new IOException("File is not writable");
        } else {
            if (bytes == null) return;
        }
        try {
            output.write(bytes);
        } catch (IOException e) {
            throw new IOException("Error while writing to file");
        }
    }

}
