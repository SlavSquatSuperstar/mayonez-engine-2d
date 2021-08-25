package testbed.iotests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.fileio.Asset;
import slavsquatsuperstar.fileio.AssetType;
import slavsquatsuperstar.fileio.Assets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Asset} class.
 *
 * @author SlavSquatSuperstar
 */
public class AssetTests {

    @Test
    public void scanAllResourcesFromDirectory() {
        Assets.scanAllResources("testassets");
        assertTrue(Assets.hasAsset("testassets/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertTrue(Assets.hasAsset("testassets/mario.png"));
        assertTrue(Assets.hasAsset("testassets/foobar/foobar.txt"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void classpathAssetExists() {
        Asset a = new Asset("testassets/mario.png", AssetType.CLASSPATH);
        assertTrue(a.isValid());
    }

    @Test
    public void classpathAssetNotExists() {
        Asset a = new Asset("testassets/luigi.png", AssetType.CLASSPATH);
        assertFalse(a.isValid());
    }

    @Test
    public void localAssetExists() {
        Asset a = new Asset("src/test/resources/testassets/mario.png", AssetType.LOCAL);
        assertTrue(a.isValid());
    }

    @Test
    public void localAssetNotExists() {
        Asset a = new Asset("src/test/resources/testassets/luigi.png", AssetType.LOCAL);
        assertFalse(a.isValid());
    }

    @Test
    public void getInvalidAssetReturnsNull() {
        assertNull(Assets.getAsset("testassets/luigi.png", AssetType.CLASSPATH));
    }

    @Test
    public void getValidAssetReturnsNull() {
        assertNotNull(Assets.getAsset("testassets/mario.png", AssetType.CLASSPATH));
    }

//    @Test
//    public void getResourceInputStreamSuccess() {
//        InputStream in = getFileFromResourceAsStream("testassets/properties.txt");
//        assertNotNull(in);
//        printInputStream(in);
//    }
//
//    @Test
//    public void getResourceInputStreamFail() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            getFileFromResourceAsStream("testassets/luigi.png");
//        });
//    }
//
//    private static InputStream getFileFromResourceAsStream(String fileName) {
//        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
//        // The stream holding the file content
//        if (inputStream == null) {
//            throw new IllegalArgumentException("file not found: " + fileName);
//        } else {
//            return inputStream;
//        }
//    }
//
//    private static void printInputStream(InputStream is) {
//        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
//            BufferedReader reader = new BufferedReader(streamReader)) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
