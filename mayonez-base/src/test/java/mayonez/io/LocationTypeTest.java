package mayonez.io;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.LocationType} class.
 *
 * @author SlavSquatSuperstar
 */
public class LocationTypeTest {

    private final String classpathFileValid = "testassets/text/foo.txt";
    private final String classpathFileInvalid = "testassets/text/bar.txt";
    private final String externalFileValid =
            PathUtil.convertPath("src/test/resources/testassets/text/foo.txt");
    private final String externalFileInvalid =
            PathUtil.convertPath("src/test/resources/testassets/text/bar.txt");

    // Classpath Location Tests

    @Test
    void classpathFilenameIsAlwaysSame() {
        var windowsClasspathFile = "testassets\\text\\foo.txt";
        assertEquals(classpathFileValid, LocationType.CLASSPATH.getFilename(classpathFileValid));
        assertEquals(classpathFileValid, LocationType.CLASSPATH.getFilename(windowsClasspathFile));
    }

    @Test
    void validClasspathURLNotNull() {
        assertNotNull(LocationType.CLASSPATH.getURL(classpathFileValid));
    }

    @Test
    void invalidClasspathURLIsNull() {
        assertNull(LocationType.CLASSPATH.getURL(classpathFileInvalid));
    }

    @Test
    void validClasspathInputStreamSucceeds() {
        assertDoesNotThrow(() -> openInputStream(LocationType.CLASSPATH, classpathFileValid));
    }

    @Test
    void invalidClasspathInputStreamFails() {
        assertThrows(IOException.class,
                () -> openInputStream(LocationType.CLASSPATH, classpathFileInvalid));
    }

    @Test
    void classpathOutputStreamAlwaysFails() {
        assertThrows(IOException.class,
                () -> openOutputStream(LocationType.CLASSPATH, classpathFileValid));
        assertThrows(IOException.class,
                () -> openOutputStream(LocationType.CLASSPATH, classpathFileInvalid));
    }

    // External Location Tests

    @Test
    void anyExternalURLNotNull() {
        assertNotNull(LocationType.EXTERNAL.getURL(externalFileValid));
        assertNotNull(LocationType.EXTERNAL.getURL(externalFileInvalid));
    }

    @Test
    void validExternalInputStreamSucceeds() {
        assertDoesNotThrow(() -> openInputStream(LocationType.EXTERNAL, externalFileValid));
    }

    @Test
    void invalidExternalInputStreamFails() {
        assertThrows(IOException.class,
                () -> openInputStream(LocationType.EXTERNAL, externalFileInvalid));
    }

    @Test
    void externalOutputStreamAlwaysSucceeds() {
        assertDoesNotThrow(() -> openOutputStream(LocationType.EXTERNAL, externalFileValid));
        assertDoesNotThrow(() -> openOutputStream(LocationType.EXTERNAL, externalFileInvalid));
        // Delete created file so tests pass
        var file = new File("src/test/resources/testassets/text/bar.txt");
        file.delete();
    }

    private static void openInputStream(LocationType type, String filename) throws IOException {
        var stream = type.openInputStream(filename);
        stream.close();
    }

    private static void openOutputStream(LocationType type, String filename) throws IOException {
        var stream = type.openOutputStream(filename, false);
        stream.close();
    }

}
