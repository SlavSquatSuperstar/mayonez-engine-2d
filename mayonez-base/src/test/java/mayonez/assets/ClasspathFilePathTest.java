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

    private final FilePath2 filePathValid = new ClasspathFilePath("testassets/text/foo.txt");
    private final FilePath2 filePathInvalid = new ClasspathFilePath("testassets/text/bar.txt");

    // File Path Tests

    @Test
    void classpathFilenameIsAlwaysSame() {
        var windowsFilePath = new ClasspathFilePath("testassets\\text\\foo.txt");
        assertEquals(filePathValid.getFilename(), windowsFilePath.getFilename());
    }

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
        assertDoesNotThrow(() -> IOTestUtils.openInputStream(filePathValid));
    }

    @Test
    void invalidClasspathInputStreamFails() {
        assertThrows(IOException.class, () -> IOTestUtils.openInputStream(filePathInvalid));
    }

    @Test
    void classpathOutputStreamAlwaysFails() {
        assertThrows(IOException.class, () -> IOTestUtils.openOutputStream(filePathValid));
        assertThrows(IOException.class, () -> IOTestUtils.openOutputStream(filePathInvalid));
    }

}
