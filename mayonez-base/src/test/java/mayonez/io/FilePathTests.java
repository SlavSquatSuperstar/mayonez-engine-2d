package mayonez.io;

import mayonez.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.FilePath} class.
 *
 * @author SlavSquatSuperstar
 */
public class FilePathTests {

    @Test
    public void filenameIsCorrectForOS() {
        var filename = "testassets/images/mario.png";
        var windowsFilename = filename.replace('/', '\\');
        var separator = OperatingSystem.getCurrentOS().getFileSeparator();

        var path = new FilePath(filename);
        if (separator.equals("/"))
            assertEquals(filename, path.getFilename());
        else if (separator.equals("\\"))
            assertEquals(windowsFilename, path.getFilename());
    }

    @Test
    public void assetTypeIsClasspath() {
        var path = new FilePath("testassets/images/mario.png");
        assertEquals(LocationType.CLASSPATH, path.getLocationType());
    }

    @Test
    public void assetTypeIsExternal() {
        var path = new FilePath("src/test/resources/testassets/images/mario.png");
        assertEquals(LocationType.EXTERNAL, path.getLocationType());
    }

    @Test
    public void classpathFilenameIsValid() {
        var path = new FilePath("testassets/images/mario.png", LocationType.CLASSPATH);
        assertTrue(path.exists());
        assertTrue(path.isReadable());
        assertFalse(path.isWritable());
    }

    @Test
    public void classpathFilenameIsNotValid() {
        var path = new FilePath("testassets/images/luigi.png", LocationType.CLASSPATH);
        assertFalse(path.exists());
        assertFalse(path.isReadable());
        assertFalse(path.isWritable());
    }

    @Test
    public void externalFilenameIsValid() {
        var path = new FilePath("src/test/resources/testassets/images/mario.png", LocationType.EXTERNAL);
        assertTrue(path.exists());
        assertTrue(path.isReadable());
        assertTrue(path.isWritable());
    }

    @Test
    public void externalFilenameIsNotValid() {
        var path = new FilePath("src/test/resources/testassets/images/luigi.png", LocationType.EXTERNAL);
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
