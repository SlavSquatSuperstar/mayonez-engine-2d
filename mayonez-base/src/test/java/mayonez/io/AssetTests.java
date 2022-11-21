package mayonez.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mayonez.io.text.TextFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.Asset} class.
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
        assertTrue(Assets.hasAsset("testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
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
        List<String> files = Assets.scanFiles("src/test/resources/testassets/mario.png");
        assertTrue(files.isEmpty());
    }

    @Test
    public void classpathAssetExists() {
        Asset a = Assets.createAsset("testassets/images/mario.png");
        assertTrue(a.isValid());
    }

    @Test
    public void classpathAssetNotExists() {
        Asset a = Assets.createAsset("testassets/luigi.png");
        assertFalse(a.isValid());
    }

    @Test
    public void localAssetExists() {
        Asset a = Assets.createAsset("src/test/resources/testassets/images/mario.png");
        assertTrue(a.isValid());
    }

    @Test
    public void localAssetNotExists() {
        Asset a = Assets.createAsset("src/test/resources/testassets/images/luigi.png");
        assertFalse(a.isValid());
    }

    @Test
    public void getValidAssetReturnsNull() {
        assertNotNull(Assets.getAsset("testassets/images/mario.png"));
    }

    @Test
    public void getInvalidAssetReturnsNull() {
        assertNull(Assets.getAsset("testassets/luigi.png"));
    }

    @Test
    public void getResourceInputStreamSuccess() {
        InputStream in = getFileFromResourceAsStream("testassets/text/properties.txt");
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
        TextFile t = Assets.getTextFile("testassets/text/properties.txt");
        assertEquals("testassets/text/properties.txt", t.getFilename());
    }

}
