package mayonez.assets;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.Assets} class.
 *
 * @author SlavSquatSuperstar
 */
class AssetsTest {

    private void reloadAssets() {
        Assets.clearAssets();
        Assets.scanDirectory("testassets/");
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
        Assets.scanDirectory("src/test/resources/testassets");
        assertTrue(Assets.hasAsset("src/test/resources/testassets/images/mario.png"));
        assertFalse(Assets.hasAsset("src/test/resources/testassets/images/luigi.png"));
        assertFalse(Assets.hasAsset("mario.png"));
    }
    
    @Test
    void createAssetAsSubclass() {
        reloadAssets();
        var path = "testassets/text/foo.txt";

        var asset = Assets.getAsset(path);
        assertNotNull(asset);
        assertInstanceOf(Asset.class, asset);
        assertFalse(asset instanceof TestAssetA);

        var textFile = Assets.getAsset(path, TestAssetA.class);
        assertNotNull(textFile);
        assertInstanceOf(TestAssetA.class, textFile);
        assertFalse(asset instanceof TestAssetB);
    }

    private static class TestAssetA extends Asset {
        public TestAssetA(String path) {
            super(path);
        }
    }

    private static class TestAssetB extends Asset {
        public TestAssetB(String path) {
            super(path);
        }
    }

}
