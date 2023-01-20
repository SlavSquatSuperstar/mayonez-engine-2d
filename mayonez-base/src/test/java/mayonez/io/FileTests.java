package mayonez.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link java.io.File} class.
 *
 * @author SlavSquatSuperstar
 */
public class FileTests {

    @Test
    public void checkExternalFile() {
        File file = new File("src/test/resources/testassets/images/mario.png");
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertNull(file.listFiles());
        assertTrue(file.canRead());
        assertTrue(file.canWrite());
    }

    @Test
    public void checkClasspathFile() throws URISyntaxException {
        URI resource = ClassLoader.getSystemResource("testassets/images/mario.png").toURI();
        File file = new File(resource);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertNull(file.listFiles());
        assertTrue(file.canRead());
//        assertTrue(file.canWrite()); // not supported by engine
    }

    @Test
    public void checkExternalFolder() {
        File dir = new File("src/test/resources/testassets/out");
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
        assertTrue(dir.listFiles().length > 0);
        assertTrue(dir.canRead());
        assertTrue(dir.canWrite());
    }

    @Test
    public void checkClasspathFolder() throws URISyntaxException {
        URI resource = ClassLoader.getSystemResource("testassets/out").toURI();
        File dir = new File(resource);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
        assertTrue(dir.listFiles().length > 0);
        assertTrue(dir.canRead());
//        assertTrue(dir.canWrite()); // not supported by engine
    }

    @Test
    public void scanExternalFolder() {
        List<String> files = Assets.scanFiles("src/test/resources/testassets/");
        assertTrue(files.size() > 0);
    }

    @Test
    public void scanClasspathFolder() {
        List<String> resources = Assets.scanResources("testassets");
        assertTrue(resources.size() > 0);
    }

}
