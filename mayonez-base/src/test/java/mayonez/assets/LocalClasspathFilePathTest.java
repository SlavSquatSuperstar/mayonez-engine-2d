package mayonez.assets;

import org.junit.jupiter.api.Test;

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

}
