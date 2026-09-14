package mayonez.assets;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.LocalClasspathFilePath} class.
 *
 * @author SlavSquatSuperstar
 */
class LocalClasspathFilePathTest {

    // Path Status/Conversion Tests

    @Test
    void validPathIsFile() {
        var filePath = new LocalClasspathFilePath("testassets/text/foo.txt");
        assertTrue(filePath.isFile());
        assertNotNull(filePath.getFile());
    }

    @Test
    void validPathIsDirectory() {
        var filePath = new LocalClasspathFilePath("testassets/text/");
        assertTrue(filePath.isDirectory());
        assertNotNull(filePath.getFile());
    }

    @Test
    void invalidPathHasNullFile() {
        var filePath = new LocalClasspathFilePath("testassets/text/bar.txt");
        assertFalse(filePath.isFile());
        assertFalse(filePath.isDirectory());
        assertNull(filePath.getFile());
    }

    @Test
    void validPathWithSpaceExists() {
        var filePath = new LocalClasspathFilePath("testassets/text/path with spaces.txt");
        assertTrue(filePath.exists());
        assertTrue(filePath.isFile());
    }

    // File Scanner Methods

    @Test
    void scanValidDirectoryIsNotEmpty() {
        var files = scanFiles("testassets");
        assertFalse(files.isEmpty());
        assertTrue(directoryContainsFile(files, "testassets/text/foo.txt"));
        assertTrue(directoryContainsFile(files, "testassets/images/mario.png"));
        assertFalse(directoryContainsFile(files, "testassets/"));
        assertFalse(directoryContainsFile(files, "testassets/.DS_Store"));
        assertFalse(directoryContainsFile(files, "mayonez/assets/LocalClasspathFilePathTest.class"));
    }

    @Test
    void scanInvalidDirectoryIsEmpty() {
        assertTrue(scanFiles("testasset/foo").isEmpty());
    }

    @Test
    void scanFileIsEmpty() {
        assertTrue(scanFiles("testassets/images/mario.png").isEmpty());
    }

    // Helper Methods

    private static List<FilePath> scanFiles(String directoryPath) {
        return new LocalClasspathFilePath(directoryPath).scanFiles();
    }

    private static boolean directoryContainsFile(List<FilePath> files, String filename) {
        return files.contains(new LocalClasspathFilePath(filename));
    }

}
