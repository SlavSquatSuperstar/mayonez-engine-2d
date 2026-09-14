package mayonez.assets;

import org.junit.jupiter.api.*;

import java.io.IOException;

import static mayonez.assets.IOTestUtils.assertInputStreamExists;
import static mayonez.assets.IOTestUtils.assertOutputStreamExists;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.ClasspathFilePath} class.
 *
 * @author SlavSquatSuperstar
 */
class ClasspathFilePathTest {

    private final FilePath filePathValid = new LocalClasspathFilePath("testassets/text/foo.txt");
    private final FilePath filePathInvalid = new LocalClasspathFilePath("testassets/text/bar.txt");

    // Path Name Tests

    @Test
    void classpathFilenameAlwaysUsesForwardSlashes() {
        var windowsFilePath = new LocalClasspathFilePath("testassets\\text\\foo.txt");
        assertEquals(filePathValid.getFilename(), windowsFilePath.getFilename());
    }

    // Path URL Tests

    @Test
    void validClasspathURLNotNull() {
        assertNotNull(filePathValid.getURL());
    }

    @Test
    void invalidClasspathURLIsNull() {
        assertNull(filePathInvalid.getURL());
    }

    // Path Status Tests

    @Test
    void validClasspathPathIsOnlyReadable() {
        assertTrue(filePathValid.exists());
        assertTrue(filePathValid.isReadable());
        assertFalse(filePathValid.isWritable());
    }

    @Test
    void invalidClassPathNotReadableOrWritable() {
        assertFalse(filePathInvalid.exists());
        assertFalse(filePathInvalid.isReadable());
        assertFalse(filePathInvalid.isWritable());
    }

    // File System Tests

    @Test
    void createClasspathFileFails() {
        var filePath = new LocalClasspathFilePath("testassets/out/test_file");
        assertFalse(filePath.exists());

        assertFalse(filePath.createFile());
        assertFalse(filePath.exists());
    }

    @Test
    void createClasspathDirectoryFails() {
        var filePath = new LocalClasspathFilePath("testassets/out/test_directory");
        assertFalse(filePath.exists());

        assertFalse(filePath.createDirectory());
        assertFalse(filePath.exists());
    }

    @Test
    void deleteClasspathPathFails() {
        var filePath1 = new LocalClasspathFilePath("testassets/text/");
        assertTrue(filePath1.isDirectory());

        assertFalse(filePath1.delete());
        assertTrue(filePath1.exists());

        var filePath2 = new LocalClasspathFilePath("testassets/text/foo.txt");
        assertFalse(filePath2.isDirectory());

        assertFalse(filePath2.delete());
        assertTrue(filePath2.exists());
    }

    // File Stream Tests

    @Test
    void validClasspathInputStreamSucceeds() {
        assertDoesNotThrow(() -> assertInputStreamExists(filePathValid));
    }

    @Test
    void invalidClasspathInputStreamFails() {
        assertThrows(IOException.class, () -> assertInputStreamExists(filePathInvalid));
    }

    @Test
    void classpathOutputStreamAlwaysFails() {
        assertThrows(IOException.class, () -> assertOutputStreamExists(filePathValid));
        assertThrows(IOException.class, () -> assertOutputStreamExists(filePathInvalid));
    }

}
