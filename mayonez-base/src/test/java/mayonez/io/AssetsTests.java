package mayonez.io;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.Assets} class.
 *
 * @author SlavSquatSuperstar
 */
public class AssetsTests {

    @BeforeEach
    public void reloadAssets() {
        Assets.clearAssets();
        Assets.scanResources("testassets");
    }

    @Test
    public void validAssetNotNull() {
        assertNotNull(Assets.getAsset("testassets/images/mario.png"));
    }

    @Test
    public void invalidAssetNull() {
        assertNull(Assets.getAsset("testassets/luigi.png"));
    }

    @Test
    public void scanResourcesAddsToStorage() {
        assertTrue(Assets.hasAsset("testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void scanFilesAddsToStorage() {
        Assets.clearAssets();
        Assets.scanFiles("src/test/resources/testassets");
        assertTrue(Assets.hasAsset("src/test/resources/testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("src/test/resources/testassets/images/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void scanFolderAddsNothing() {
        var files = Assets.scanFiles("src/test/resources/testassets/mario.png");
        assertTrue(files.isEmpty());
    }

    @Test
    public void getResourceInputStreamSuccess() {
        var in = getFileFromResourceAsStream("testassets/text/properties.txt");
        assertNotNull(in);
        printInputStream(in);
    }

    @Test
    public void getResourceInputStreamFail() {
        assertThrows(IllegalArgumentException.class, () -> getFileFromResourceAsStream("testassets/luigi.png"));
    }

    private static InputStream getFileFromResourceAsStream(String fileName) {
        var inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        // The stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found: " + fileName);
        } else {
            return inputStream;
        }
    }

    private static void printInputStream(InputStream is) {
        try (var streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             var reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createAssetWithReflection() {
        var t = Assets.getTextFile("testassets/text/properties.txt");
        assertEquals("testassets/text/properties.txt", t.getFilename());
    }

}
