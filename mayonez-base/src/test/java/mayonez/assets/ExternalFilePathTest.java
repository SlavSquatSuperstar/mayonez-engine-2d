package mayonez.assets;

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

    // Path Name Tests

    @Test
    void externalFilenameContainsSystemSeparators() {
        var unixFilePath = new ExternalFilePath("src/test/resources/testassets/text/foo.txt");
        assertTrue(unixFilePath.getFilename().contains(PathUtil.CURRENT_SEPARATOR));

        var windowsFilePath = new ExternalFilePath("src\\test\\resources\\testassets\\text\\foo.txt");
        assertTrue(windowsFilePath.getFilename().contains(PathUtil.CURRENT_SEPARATOR));
    }

    // Path Conversion Tests

    @Test
    void allExternalFileNotNull() {
        assertNotNull(filePathValid.getFile());
        assertNotNull(filePathInvalid.getFile());
    }

    @Test
    void allExternalURLNotNull() {
        assertNotNull(filePathValid.getURL());
        assertNotNull(filePathInvalid.getURL());
    }

    // Path Status Tests

    @Test
    void validExternalFileIsReadableAndWritable() {
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
    void externalDirectoryNeverReadableOrWritable() {
        var folderPath = new ExternalFilePath("src/test/resources/testassets/");
        assertTrue(folderPath.exists());
        assertTrue(folderPath.isDirectory());
        assertFalse(folderPath.isReadable());
        assertFalse(folderPath.isWritable());
    }

    // File System Tests

    @Test
    void createAndDeleteNewFileSuccess() {
        var filePath = new ExternalFilePath("src/test/resources/testassets/out/test_file");
        assertFalse(filePath.exists());

        assertTrue(filePath.createFile());
        assertTrue(filePath.isFile());

        assertTrue(filePath.delete());
        assertFalse(filePath.exists());
    }

    @Test
    void createAndDeleteNewDirectorySuccess() {
        var filePath = new ExternalFilePath("src/test/resources/testassets/out/test_directory");
        assertFalse(filePath.exists());

        assertTrue(filePath.createDirectory());
        assertTrue(filePath.isDirectory());

        assertTrue(filePath.delete());
        assertFalse(filePath.exists());
    }

    @Test
    void createExistingPathFails() {
        var filePath1 = new ExternalFilePath("src/test/resources/testassets/text/");
        assertTrue(filePath1.isDirectory());

        assertFalse(filePath1.createDirectory());
        assertFalse(filePath1.createFile());

        var filePath2 = new ExternalFilePath("src/test/resources/testassets/text/foo.txt");
        assertTrue(filePath2.isFile());

        assertFalse(filePath2.createDirectory());
        assertFalse(filePath2.createFile());
    }

    @Test
    void createPathWithNoParentFails() {
        var filePath = new ExternalFilePath("src/test/resources/testassets/out/foo/bar");
        assertFalse(filePath.exists());

        assertFalse(filePath.createFile());
        assertFalse(filePath.createDirectory());
    }

    @Test
    void deleteNonExistingPathFails() {
        var filePath = new ExternalFilePath("src/test/resources/testassets/out/test_directory");
        assertFalse(filePath.exists());

        assertFalse(filePath.delete());
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
    void validExternalOutputStreamSucceeds() {
        assertDoesNotThrow(() -> IOTestUtils.assertOutputStreamExists(filePathValid));
    }

    @Test
    void invalidOutputStreamWithDirSucceeds() {
        assertDoesNotThrow(() -> IOTestUtils.assertOutputStreamExists(filePathInvalid));
        // Delete created file so other tests pass
        filePathInvalid.delete();
    }

    @Test
    void invalidOutputStreamWithoutDirFails() {
        var filePathInvalid2 = new ExternalFilePath("src/test/resources/testassets/text/baz/bar.txt");
        assertThrows(IOException.class, () -> IOTestUtils.assertOutputStreamExists(filePathInvalid2));
    }

}
