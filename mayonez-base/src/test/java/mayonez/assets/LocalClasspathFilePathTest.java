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

    // File Hierarchy Methods

    @Test
    void parentOfFileIsDirectory() {
        var path = new LocalClasspathFilePath("testassets/text/foo.txt");
        var parent = path.getParent();
        assertNotNull(parent);
        assertEquals("testassets/text", parent.getPath());
        assertTrue(parent.isDirectory());
    }

    @Test
    void parentOfTopLevelIsNull() {
        var path = new LocalClasspathFilePath("testassets/");
        assertNull(path.getParent());
    }

    @Test
    void combinedPathIsChild() {
        var path = new LocalClasspathFilePath("testassets");
        var combined = path.combine("text");
        assertNotNull(combined);
        assertEquals("testassets/text", combined.getPath());
        assertTrue(combined.isDirectory());
    }

    @Test
    void combinedPathIsGrandchild() {
        var path = new LocalClasspathFilePath("testassets");
        var combined = path.combine("text/foo.txt");
        assertNotNull(combined);
        assertEquals("testassets/text/foo.txt", combined.getPath());
        assertTrue(combined.isFile());
    }

    @Test
    void combinedPathDoesNotExist() {
        var path = new LocalClasspathFilePath("testassets");
        var combined = path.combine("text/bar.txt");
        assertNotNull(combined);
        assertEquals("testassets/text/bar.txt", combined.getPath());
        assertFalse(combined.exists());
    }

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

    private static boolean directoryContainsFile(List<FilePath> files, String path) {
        return files.contains(new LocalClasspathFilePath(path));
    }

}
