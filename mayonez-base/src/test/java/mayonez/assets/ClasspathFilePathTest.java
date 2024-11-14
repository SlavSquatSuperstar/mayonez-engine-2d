package mayonez.assets;

import mayonez.io.text.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.ClasspathFilePath} class.
 *
 * @author SlavSquatSuperstar
 */
class ClasspathFilePathTest {

    private final FilePath filePathValid = new ClasspathFilePath("testassets/text/foo.txt");
    private final FilePath filePathInvalid = new ClasspathFilePath("testassets/text/bar.txt");

    // Filename Tests

    @Test
    void classpathFilenameIsAlwaysSame() {
        var windowsFilePath = new ClasspathFilePath("testassets\\text\\foo.txt");
        assertEquals(filePathValid.getFilename(), windowsFilePath.getFilename());
    }

    // File URL Tests
    @Test
    void validClasspathURLNotNull() {
        assertNotNull(filePathValid.getURL());
    }

    @Test
    void invalidClasspathURLIsNull() {
        assertNull(filePathInvalid.getURL());
    }

    // File Permissions Tests

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
        assertDoesNotThrow(() -> IOTestUtils.assertInputStreamExists(filePathValid));
    }

    @Test
    void invalidClasspathInputStreamFails() {
        assertThrows(IOException.class, () -> IOTestUtils.assertInputStreamExists(filePathInvalid));
    }

    @Test
    void classpathOutputStreamAlwaysFails() {
        assertThrows(IOException.class, () -> IOTestUtils.assertOutputStreamExists(filePathValid));
        assertThrows(IOException.class, () -> IOTestUtils.assertOutputStreamExists(filePathInvalid));
    }

}
