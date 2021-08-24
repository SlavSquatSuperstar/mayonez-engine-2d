package testbed.iotests;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import slavsquatsuperstar.fileio.Asset;
import slavsquatsuperstar.fileio.Assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Asset} class.
 *
 * @author SlavSquatSuperstar
 */
public class AssetTests {

    @Test
    public void classpathAssetExists() {
        Asset a = new Asset("testassets/mario.png", true);
        assertTrue(a.exists());
    }

    @Test
    public void classpathAssetNotExists() {
        Asset a = new Asset("testassets/luigi.png", true);
        assertFalse(a.exists());
    }

    @Test
    public void localAssetExists() {
        Asset a = new Asset("src/test/resources/testassets/mario.png", false);
        assertTrue(a.exists());
    }

    @Test
    public void localAssetNotExists() {
        Asset a = new Asset("src/test/resources/testassets/luigi.png", false);
        assertFalse(a.exists());
    }

    @Test
    public void getInvalidAssetReturnsNull() {
        assertNull(Assets.getAsset("testassets/luigi.png", true));
    }

    @Test
    public void getValidAssetReturnsNull() {
        assertNotNull(Assets.getAsset("testassets/mario.png", true));
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

    @Test
    public void getResourcesFromClasspathFolder() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("testassets")).setScanners(new ResourcesScanner())
        );
        Set<String> pictures = reflections.getResources(Pattern.compile(".*\\.*"));
        assertTrue(pictures.contains("testassets/mario.png"));
        assertTrue(pictures.contains("testassets/foobar/foobar.txt"));
        assertFalse(pictures.contains("testassets/luigi.png"));
        assertFalse(pictures.contains("mario.png"));
    }

    @Test
    public void scanAllAssetsOnStartup() {
        Assets.scanAll("testassets");
        assertTrue(Assets.hasAsset("testassets/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
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


}
