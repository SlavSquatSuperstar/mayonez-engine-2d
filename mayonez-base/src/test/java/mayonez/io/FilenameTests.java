package mayonez.io;

import mayonez.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.Filename} class.
 *
 * @author SlavSquatSuperstar
 */
public class FilenameTests {

    @Test
    public void filenameIsCorrectForOS() {
        var filenameStr = "testassets/images/mario.png";
        var filename = new Filename(filenameStr);
        var separator = OperatingSystem.getCurrentOS().getFileSeparator();

        var windowsFilename = filenameStr.replace('/', '\\');

        if (separator.equals("/"))
            assertEquals(filenameStr, filename.getName());
        else if (separator.equals("\\"))
            assertEquals(windowsFilename, filename.getName());
    }

    @Test
    public void assetTypeIsClasspath() {
        var filename = new Filename("testassets/images/mario.png");
        assertEquals(AssetLocation.CLASSPATH, filename.getLocation());
    }

    @Test
    public void assetTypeIsExternal() {
        var filename = new Filename("src/test/resources/testassets/images/mario.png");
        assertEquals(AssetLocation.EXTERNAL, filename.getLocation());
    }

    @Test
    public void classpathFilenameIsValid() {
        var filename = new Filename("testassets/images/mario.png", AssetLocation.CLASSPATH);
        assertTrue(filename.exists());
        assertTrue(filename.isReadable());
        assertFalse(filename.isWritable());
    }

    @Test
    public void classpathFilenameIsNotValid() {
        var filename = new Filename("testassets/images/luigi.png", AssetLocation.CLASSPATH);
        assertFalse(filename.exists());
        assertFalse(filename.isReadable());
        assertFalse(filename.isWritable());
    }

    @Test
    public void externalFilenameIsValid() {
        var filename = new Filename("src/test/resources/testassets/images/mario.png", AssetLocation.EXTERNAL);
        assertTrue(filename.exists());
        assertTrue(filename.isReadable());
        assertTrue(filename.isWritable());
    }

    @Test
    public void externalFilenameIsNotValid() {
        var filename = new Filename("src/test/resources/testassets/images/luigi.png", AssetLocation.EXTERNAL);
        assertFalse(filename.exists());
        assertFalse(filename.isReadable());
        assertTrue(filename.isWritable());
    }

    @Test
    public void folderIsNotReadableOrWritable() {
        var filename = new Filename("src/test/resources/testassets/", AssetLocation.EXTERNAL);
        assertTrue(filename.exists());
        assertFalse(filename.isReadable());
        assertFalse(filename.isWritable());
    }

}
