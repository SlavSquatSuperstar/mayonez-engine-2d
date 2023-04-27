package mayonez.io;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.FilePath} class.
 *
 * @author SlavSquatSuperstar
 */
public class FilePathTests {

    private final String classpathMarioImg = "testassets/images/mario.png";
    private final String externalMarioImg = "src/test/resources/testassets/images/mario.png";

    @Test
    public void classpathFilePathIsAlwaysSame() {
        var path = new FilePath(classpathMarioImg, LocationType.CLASSPATH);
        assertEquals(classpathMarioImg, path.getFilename());
    }

    @Test
    public void filePathLocationIsClasspath() {
        var path = new FilePath(classpathMarioImg);
        assertEquals(LocationType.CLASSPATH, path.getLocationType());
    }

    @Test
    public void filePathLocationIsExternal() {
        var path = new FilePath(externalMarioImg);
        assertEquals(LocationType.EXTERNAL, path.getLocationType());
    }

    @Test
    public void classpathFilePathIsValid() {
        var path = new FilePath(classpathMarioImg, LocationType.CLASSPATH);
        assertTrue(path.exists());
        assertTrue(path.isReadable());
        assertFalse(path.isWritable());
    }

    @Test
    public void classpathFilePathIsNotValid() {
        var classpathLuigiImg = "testassets/images/luigi.png";
        var path = new FilePath(classpathLuigiImg, LocationType.CLASSPATH);
        assertFalse(path.exists());
        assertFalse(path.isReadable());
        assertFalse(path.isWritable());
    }

    @Test
    public void externalFilePathIsValid() {
        var path = new FilePath(externalMarioImg, LocationType.EXTERNAL);
        assertTrue(path.exists());
        assertTrue(path.isReadable());
        assertTrue(path.isWritable());
    }

    @Test
    public void externalFilePathIsNotValid() {
        var externalLuigiImg = "src/test/resources/testassets/images/luigi.png";
        var path = new FilePath(externalLuigiImg, LocationType.EXTERNAL);
        assertFalse(path.exists());
        assertFalse(path.isReadable());
        assertTrue(path.isWritable());
    }

    @Test
    public void folderIsNotReadableOrWritable() {
        var path = new FilePath("src/test/resources/testassets/", LocationType.EXTERNAL);
        assertTrue(path.exists());
        assertFalse(path.isReadable());
        assertFalse(path.isWritable());
    }

}
