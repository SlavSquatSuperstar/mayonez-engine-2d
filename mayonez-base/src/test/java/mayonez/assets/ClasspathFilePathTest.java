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

    private final FilePath filePathValid = new ClasspathFilePath("testassets/text/foo.txt");
    private final FilePath filePathInvalid = new ClasspathFilePath("testassets/text/bar.txt");

    // Path Name Tests

    @Test
    void classpathFilenameAlwaysUsesForwardSlashes() {
        var windowsFilePath = new ClasspathFilePath("testassets\\text\\foo.txt");
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
