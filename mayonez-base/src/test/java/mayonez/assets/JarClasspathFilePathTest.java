package mayonez.assets;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JarClasspathFilePath} class.
 * 
 * @author SlavSquatSuperstar
 */
class JarClasspathFilePathTest {

    // Path Status/Conversion Tests
    
    @Test
    void validPathIsFile() {
        var filePath = new JarClasspathFilePath("foo/a.txt");
        assertTrue(filePath.isFile());
        assertNull(filePath.getFile()); // File is always null
    }

    @Test
    void validPathIsDirectory() {
        var filePath = new JarClasspathFilePath("foo/");
        assertTrue(filePath.isDirectory());
        assertNull(filePath.getFile()); // File is always null
    }
    
    @Test
    void invalidPathHasNullFile() {
        var filePath = new JarClasspathFilePath("foo/quux");
        assertFalse(filePath.isFile());
        assertFalse(filePath.isDirectory());
        assertNull(filePath.getFile());
    }

    @Test
    void validPathWithSpaceExists() {
        var filePath = new JarClasspathFilePath("foo/path with spaces.txt");
        assertTrue(filePath.exists());
        assertTrue(filePath.isFile());
    }

    // File Hierarchy Tests

    @Test
    void parentOfFileIsDirectory() {
        var path = new JarClasspathFilePath("foo/a.txt");
        var parent = path.getParent();
        assertNotNull(parent);
        assertEquals("foo", parent.getPath());
        assertTrue(parent.isDirectory());
    }

    @Test
    void parentOfTopLevelIsNull() {
        var path = new JarClasspathFilePath("foo/");
        assertNull(path.getParent());
    }

    @Test
    void combinedPathIsChild() {
        var path = new JarClasspathFilePath("foo");
        var combined = path.combine("bar");
        assertNotNull(combined);
        assertEquals("foo/bar", combined.getPath());
        assertTrue(combined.isDirectory());
    }

    @Test
    void combinedPathIsGrandchild() {
        var path = new JarClasspathFilePath("foo");
        var combined = path.combine("baz/c.txt");
        assertNotNull(combined);
        assertEquals("foo/baz/c.txt", combined.getPath());
        assertTrue(combined.isFile());
    }

    @Test
    void combinedPathDoesNotExist() {
        var path = new JarClasspathFilePath("foo");
        var combined = path.combine("quux");
        assertNotNull(combined);
        assertEquals("foo/quux", combined.getPath());
        assertFalse(combined.exists());
    }

    @Test
    void scanValidDirectoryIsNotEmpty() {
        var files = scanFiles("foo");
        assertFalse(files.isEmpty());
        assertTrue(directoryContainsFile(files, "foo/a.txt"));
        assertTrue(directoryContainsFile(files, "foo/bar/b.txt"));
        assertTrue(directoryContainsFile(files, "foo/baz/c.txt"));
        assertFalse(directoryContainsFile(files, "foo/"));
        assertFalse(directoryContainsFile(files, "foo/.DS_Store"));
        assertFalse(directoryContainsFile(files, "Test.class"));
    }

    @Test
    void scanInvalidDirectoryIsEmpty() {
        assertTrue(scanFiles("foo/quux/").isEmpty());
    }

    @Test
    void scanFileIsEmpty() {
        assertTrue(scanFiles("foo/a.txt").isEmpty());
    }

    // Helper Methods

    private static List<FilePath> scanFiles(String directoryPath) {
        return new JarClasspathFilePath(directoryPath).scanFiles();
    }

    private static boolean directoryContainsFile(List<FilePath> files, String path) {
        return files.contains(new JarClasspathFilePath(path));
    }

}
