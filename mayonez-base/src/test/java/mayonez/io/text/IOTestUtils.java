package mayonez.io.text;

import mayonez.assets.*;

import java.io.File;
import java.io.IOException;

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
    static void checkFileExists(String filename) {
        var file = new File(filename);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public static void openInputStream(FilePath2 filePath) throws IOException {
        var stream = filePath.openInputStream();
        stream.close();
    }

    public static void openOutputStream(FilePath2 filePath) throws IOException {
        var stream = filePath.openOutputStream(false);
        stream.close();
    }

}
