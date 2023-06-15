package mayonez.io;

import mayonez.io.text.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.Assets} class.
 *
 * @author SlavSquatSuperstar
 */
class AssetsTest {

    private void reloadAssets() {
        Assets.clearAssets();
        Assets.scanResources("testassets/");
    }

    @Test
    void validAssetNotNull() {
        reloadAssets();
        assertNotNull(Assets.getAsset("testassets/images/mario.png"));
    }

    @Test
    void invalidAssetNull() {
        reloadAssets();
        assertNull(Assets.getAsset("testassets/luigi.png"));
    }

    @Test
    void scanClasspathFolderAddsToStorage() {
        reloadAssets();
        assertTrue(Assets.hasAsset("testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    void scanExternalFolderAddsToStorage() {
        Assets.scanFiles("src/test/resources/testassets");
        assertTrue(Assets.hasAsset("src/test/resources/testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("src/test/resources/testassets/images/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }
    
    @Test
    void createAssetAsType() {
        reloadAssets();

        var filename = "testassets/text/properties.txt";
        var asset = Assets.getAsset(filename);
        assertNotNull(asset);
        assertInstanceOf(Asset.class, asset);
        assertEquals(asset.getLocationType(), LocationType.CLASSPATH);

        var textFile = Assets.getAsset(filename, TextFile.class);
        assertNotNull(textFile);
        assertInstanceOf(TextFile.class, textFile);
        assertEquals(textFile.getLocationType(), LocationType.CLASSPATH);
        assertEquals(filename, textFile.getFilename());
    }

}
