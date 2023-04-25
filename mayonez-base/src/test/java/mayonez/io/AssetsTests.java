package mayonez.io;

import mayonez.io.text.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.Assets} class.
 *
 * @author SlavSquatSuperstar
 */
public class AssetsTests {

    private void reloadAssets() {
        Assets.clearAssets();
        Assets.scanResources("testassets/");
    }

    @Test
    public void validAssetNotNull() {
        reloadAssets();
        assertNotNull(Assets.getAsset("testassets/images/mario.png"));
    }

    @Test
    public void invalidAssetNull() {
        reloadAssets();
        assertNull(Assets.getAsset("testassets/luigi.png"));
    }

    @Test
    public void scanClasspathFolderAddsToStorage() {
        reloadAssets();
        assertTrue(Assets.hasAsset("testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("testassets/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void scanExternalFolderAddsToStorage() {
        Assets.scanFiles("src/test/resources/testassets");
        assertTrue(Assets.hasAsset("src/test/resources/testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("src/test/resources/testassets/images/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }

    @Test
    public void scanEmptyFolderReturnsNothing() {
        var files = Assets.scanFiles("src/test/resources/testassets/mario.png");
        assertTrue(files.isEmpty());
    }

    @Test
    public void createAssetAsType() {
        reloadAssets();

        var filename = "testassets/text/properties.txt";
        var asset = Assets.getAsset(filename);
        assertInstanceOf(Asset.class, asset);
        assertEquals(asset.getLocationType(), LocationType.CLASSPATH);

        var textFile = Assets.getTextFile(filename);
        assertInstanceOf(TextFile.class, textFile);
        assertEquals(asset.getLocationType(), LocationType.CLASSPATH);
        assertEquals(filename, textFile.getFilename());
    }

}
