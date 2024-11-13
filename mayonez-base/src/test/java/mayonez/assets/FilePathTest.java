package mayonez.assets;

import mayonez.io.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.FilePath} class.
 *
 * @author SlavSquatSuperstar
 */
class FilePathTest {

    private final String classpathFileValid = "testassets/text/foo.txt";
    private final String classpathFileInvalid = "testassets/text/bar.txt";
    private final String externalFileValid =
            PathUtil.convertPath("src/test/resources/testassets/text/foo.txt");
    private final String externalFileInvalid =
            PathUtil.convertPath("src/test/resources/testassets/text/bar.txt");

    // Auto-Guess Location Tests

    @Test
    void filePathLocationIsClasspath() {
        var path = new FilePath(classpathFileValid);
        assertEquals(LocationType.CLASSPATH, path.getLocationType());
    }

    @Test
    void filePathLocationIsExternal() {
        var path = new FilePath(externalFileValid);
        assertEquals(LocationType.EXTERNAL, path.getLocationType());
    }

    // Classpath File Tests

    @Test
    void classpathFilenameIsAlwaysSame() {
        var unixFilePath = new FilePath(classpathFileValid, LocationType.CLASSPATH);
        var windowsFilePath = new FilePath("testassets\\text\\foo.txt", LocationType.CLASSPATH);
        assertEquals(unixFilePath.getFilename(), windowsFilePath.getFilename());
    }

    @Test
    void validClasspathPathIsOnlyReadable() {
        var path = new FilePath(classpathFileValid, LocationType.CLASSPATH);
        assertTrue(path.exists());
        assertTrue(path.isReadable());
        assertFalse(path.isWritable());
    }

    @Test
    void invalidClassPathNotReadableOrWritable() {
        var classpathFileInvalid = "testassets/images/luigi.png";
        var path = new FilePath(classpathFileInvalid, LocationType.CLASSPATH);
        assertFalse(path.exists());
        assertFalse(path.isReadable());
        assertFalse(path.isWritable());
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

    // External File Tests

    @Test
    void validExternalPathIsReadableAndWritable() {
        var path = new FilePath(externalFileValid, LocationType.EXTERNAL);
        assertTrue(path.exists());
        assertTrue(path.isReadable());
        assertTrue(path.isWritable());
    }

    @Test
    void invalidExternalPathIsOnlyWritable() {
        var externalFileInvalid = "src/test/resources/testassets/images/luigi.png";
        var path = new FilePath(externalFileInvalid, LocationType.EXTERNAL);
        assertFalse(path.exists());
        assertFalse(path.isReadable());
        assertTrue(path.isWritable());
    }

    @Test
    void anyFolderNotReadableOrWritable() {
        var path = new FilePath("src/test/resources/testassets/", LocationType.EXTERNAL);
        assertTrue(path.exists());
        assertFalse(path.isReadable());
        assertFalse(path.isWritable());
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
        var file = new File(externalFileInvalid);
        file.delete();
    }

    // Helper Methods

    private static void openInputStream(LocationType type, String filename) throws IOException {
        var filePath = new FilePath(filename, type);
        var stream = filePath.openInputStream();
        stream.close();
    }

    private static void openOutputStream(LocationType type, String filename) throws IOException {
        var filePath = new FilePath(filename, type);
        var stream = filePath.openOutputStream(false);
        stream.close();
    }

}
