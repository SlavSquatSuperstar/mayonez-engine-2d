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

    private final FilePath filePathValid = fromFilename("testassets/text/foo.txt");
    private final FilePath filePathInvalid = fromFilename("testassets/text/bar.txt");

    // Path Name Tests

    @Test
    void classpathFilenameAlwaysUsesForwardSlashes() {
        var windowsFilePath = fromFilename("testassets\\text\\foo.txt");
        assertEquals(filePathValid.getPath(), windowsFilePath.getPath());
    }

    // Path Conversion Tests

    @Test
    void validClasspathFileNotNull() {
        assertNotNull(filePathValid.getFile());
    }

    @Test
    void invalidClasspathFileIsNull() {
        assertNull(filePathInvalid.getFile());
    }

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
        var filePath = fromFilename("testassets/out/test_file");
        assertFalse(filePath.exists());

        assertFalse(filePath.createFile());
        assertFalse(filePath.exists());
    }

    @Test
    void createClasspathDirectoryFails() {
        var filePath = fromFilename("testassets/out/test_directory");
        assertFalse(filePath.exists());

        assertFalse(filePath.createDirectory());
        assertFalse(filePath.exists());
    }

    @Test
    void deleteClasspathFileFails() {
        var filePath1 = fromFilename("testassets/text/");
        assertTrue(filePath1.isDirectory());

        assertFalse(filePath1.delete());
        assertTrue(filePath1.exists());
    }

    @Test
    void deleteClasspathDirectoryFails() {
        var filePath2 = fromFilename("testassets/text/foo.txt");
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

    // Helper Method

    private static ClasspathFilePath fromFilename(String filename) {
        var url = PathUtil.getResourceURL(filename);
        if (url == null || url.getProtocol().equals("file")) {
            return new LocalClasspathFilePath(filename);
        } else {
            return new JarClasspathFilePath(filename);
        }
    }

}
