package mayonez.assets;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

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
        var directoryPath = new ExternalFilePath("src/test/resources/testassets/");
        assertTrue(directoryPath.exists());
        assertTrue(directoryPath.isDirectory());
        assertFalse(directoryPath.isReadable());
        assertFalse(directoryPath.isWritable());
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

    // File Hierarchy Tests

    @Test
    void parentOfFilePathIsDirectory() {
        var parent = filePathValid.getParent();
        assertNotNull(parent);
        assertEquals("src/test/resources/testassets/text", parent.getFilename());
        assertTrue(parent.isDirectory());
    }

    @Test
    void parentOfRootIsNull() {
        var path = new ExternalFilePath("/");
        assertNull(path.getParent());
    }

    @Test
    void combinedPathIsChild() {
        var path = new ExternalFilePath("src/test/resources/testassets");
        var combined = path.combine("text");
        assertNotNull(combined);
        assertPathNameEquals(combined, "src/test/resources/testassets/text");
        assertTrue(combined.isDirectory());
    }

    @Test
    void combinedPathIsGrandchild() {
        var path = new ExternalFilePath("src/test/resources/testassets");
        var combined = path.combine("text/foo.txt");
        assertNotNull(combined);
        assertPathNameEquals(combined, "src/test/resources/testassets/text/foo.txt");
        assertTrue(combined.isFile());
    }

    @Test
    void combinedPathDoesNotExist() {
        var path = new ExternalFilePath("src/test/resources/testassets");
        var combined = path.combine("text/bar.txt");
        assertNotNull(combined);
        assertPathNameEquals(combined, "src/test/resources/testassets/text/bar.txt");
        assertFalse(combined.exists());
    }

    @Test
    void scanValidDirectoryIsNotEmpty() {
        var files = scanFiles("src/test/resources/testassets");
        assertFalse(files.isEmpty());
        assertTrue(directoryContainsFile(files, "src/test/resources/testassets/text/foo.txt"));
        assertTrue(directoryContainsFile(files, "src/test/resources/testassets/images/mario.png"));
        assertFalse(directoryContainsFile(files, "src/test/resources/testassets/"));
        assertFalse(directoryContainsFile(files, "src/test/resources/testassets/.DS_Store"));
        assertFalse(directoryContainsFile(files, "src/test/java/mayonez/assets/ExternalFilePathTest.class"));
    }

    @Test
    void scanInvalidDirectoryIsEmpty() {
        assertTrue(scanFiles("src/test/resources/testasset").isEmpty());
    }

    @Test
    void scanFileIsEmpty() {
        assertTrue(scanFiles("src/test/resources/testassets/images/mario.png").isEmpty());
    }

    // Helper Methods

    private static List<FilePath> scanFiles(String directoryPath) {
        return new ExternalFilePath(directoryPath).scanFiles();
    }

    private static boolean directoryContainsFile(List<FilePath> files, String filename) {
        return files.contains(new ExternalFilePath(filename));
    }

    private static void assertPathNameEquals(FilePath path, String name) {
        // OS-independent path name check
        assertEquals(new ExternalFilePath(name).getFilename(), path.getFilename());
    }

}
