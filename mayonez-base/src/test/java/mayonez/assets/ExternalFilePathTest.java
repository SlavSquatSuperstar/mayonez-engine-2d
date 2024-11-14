package mayonez.assets;

import mayonez.io.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.ExternalFilePath} class.
 *
 * @author SlavSquatSuperstar
 */
class ExternalFilePathTest {

    private final FilePath filePathValid = new ExternalFilePath("src/test/resources/testassets/text/foo.txt");
    private final FilePath filePathInvalid = new ExternalFilePath("src/test/resources/testassets/text/bar.txt");

    // File URL Tests

    @Test
    void anyExternalURLNotNull() {
        assertNotNull(filePathValid.getURL());
        assertNotNull(filePathInvalid.getURL());
    }

    // File Permission Tests

    @Test
    void validExternalPathIsReadableAndWritable() {
        assertTrue(filePathValid.exists());
        assertTrue(filePathValid.isReadable());
        assertTrue(filePathValid.isWritable());
    }

    @Test
    void invalidExternalPathIsOnlyWritable() {
        assertFalse(filePathInvalid.exists());
        assertFalse(filePathInvalid.isReadable());
        assertTrue(filePathInvalid.isWritable());
    }

    @Test
    void anyFolderNotReadableOrWritable() {
        var folderPath = new ExternalFilePath("src/test/resources/testassets/");
        assertTrue(folderPath.exists());
        assertFalse(folderPath.isReadable());
        assertFalse(folderPath.isWritable());
    }

    // File Stream Tests

    @Test
    void validExternalInputStreamSucceeds() {
        assertDoesNotThrow(() -> IOTestUtils.assertInputStreamExists(filePathValid));
    }

    @Test
    void invalidExternalInputStreamFails() {
        assertThrows(IOException.class, () -> IOTestUtils.assertInputStreamExists(filePathInvalid));
    }

    @Test
    void externalOutputStreamAlwaysSucceeds() {
        assertDoesNotThrow(() -> IOTestUtils.assertOutputStreamExists(filePathValid));
        assertDoesNotThrow(() -> IOTestUtils.assertOutputStreamExists(filePathInvalid));
        // Delete created file so tests pass
        var file = filePathInvalid.getFile();
        file.delete();
    }


}
