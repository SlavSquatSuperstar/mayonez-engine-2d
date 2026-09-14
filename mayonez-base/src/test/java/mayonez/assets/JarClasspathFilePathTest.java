package mayonez.assets;

import org.junit.jupiter.api.Test;

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
    
}
