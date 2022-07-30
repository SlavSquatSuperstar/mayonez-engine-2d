package slavsquatsuperstar.test.iotests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.io.Asset;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.TextFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Asset} class.
 *
 * @author SlavSquatSuperstar
 */
public class AssetTests {

    @BeforeEach
    public void reloadAssets() {
        Assets.clearAssets();
        Assets.scanResources("testassets");
    }

    @Test
    public void scanResourcesAddsToStorage() {
        assertTrue(Assets.hasAsset("testassets/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertTrue(Assets.hasAsset("testassets/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void scanFilesAddsToStorage() {
        Assets.clearAssets();
        Assets.scanFiles("src/test/resources/testassets");
        assertTrue(Assets.hasAsset("src/test/resources/testassets/mario.png"));
        assertFalse(Assets.hasAsset("src/test/resources/testassets/luigi.png"));
        assertTrue(Assets.hasAsset("src/test/resources/testassets/mario.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void classpathAssetExists() {
        Asset a = Assets.createAsset("testassets/mario.png");
        assertTrue(a.isValid());
    }

    @Test
    public void classpathAssetNotExists() {
        Asset a = Assets.createAsset("testassets/luigi.png");
        assertFalse(a.isValid());
    }

    @Test
    public void localAssetExists() {
        Asset a = Assets.createAsset("src/test/resources/testassets/mario.png");
        assertTrue(a.isValid());
    }

    @Test
    public void localAssetNotExists() {
        Asset a = Assets.createAsset("src/test/resources/testassets/luigi.png");
        assertFalse(a.isValid());
    }

    @Test
    public void getValidAssetReturnsNull() {
        assertNotNull(Assets.getAsset("testassets/mario.png"));
    }

    @Test
    public void getInvalidAssetReturnsNull() {
        assertNull(Assets.getAsset("testassets/luigi.png"));
    }

    @Test
    public void getResourceInputStreamSuccess() {
        InputStream in = getFileFromResourceAsStream("testassets/properties.txt");
        assertNotNull(in);
        printInputStream(in);
    }

    @Test
    public void getResourceInputStreamFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            getFileFromResourceAsStream("testassets/luigi.png");
        });
    }

    private static InputStream getFileFromResourceAsStream(String fileName) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        // The stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found: " + fileName);
        } else {
            return inputStream;
        }
    }

    private static void printInputStream(InputStream is) {
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
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
        TextFile t = Assets.createAsset("testassets/properties.txt", TextFile.class);
        assertEquals("testassets/properties.txt", t.getFilename());
    }

}
