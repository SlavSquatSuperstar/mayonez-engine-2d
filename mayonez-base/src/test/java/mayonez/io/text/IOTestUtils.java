package mayonez.io.text;

import mayonez.assets.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Utility methods for I/O unit tests.
 *
 * @author SlavSquatSuperstar
 */
public final class IOTestUtils {

    private IOTestUtils() {
    }

    /**
     * Make sure a given file is present so a test succeeds.
     *
     * @param filename the filename
     */
    public static void checkFileExists(String filename) {
        var file = new File(filename);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Attempt to open an input stream from a file path and close it.
     *
     * @param filePath the file path to use
     * @throws IOException if the stream cannot be opened
     */
    public static void assertInputStreamExists(FilePath filePath) throws IOException {
        var stream = filePath.openInputStream();
        stream.close();
    }

    /**
     * Attempt to open an output stream from a file path and close it.
     *
     * @param filePath the file path to use
     * @throws IOException if the stream cannot be opened
     */
    public static void assertOutputStreamExists(FilePath filePath) throws IOException {
        var stream = filePath.openOutputStream(false);
        stream.close();
    }

    /**
     * Check that an input stream is still open.
     *
     * @param stream the stream to check
     */
    public static void assertInputStreamOpen(InputStream stream) {
        assertDoesNotThrow(() -> stream.read());
    }

    /**
     * Check that an output stream is still open.
     *
     * @param stream the stream to check
     */
    public static void assertOutputStreamOpen(OutputStream stream) {
        assertDoesNotThrow(() -> stream.write("foo\n".getBytes(StandardCharsets.UTF_8)));
    }

}
