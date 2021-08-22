package testbed.iotests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.assets.Asset;
import slavsquatsuperstar.mayonez.assets.Assets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Asset} class.
 *
 * @author SlavSquatSuperstar
 */
public class AssetTests {

    @Test
    public void classpathAssetExists() {
        Asset a = new Asset("mario.png", true);
        assertTrue(a.exists());
    }

    @Test
    public void classpathAssetNotExists() {
        Asset a = new Asset("luigi.png", true);
        assertFalse(a.exists());
    }

    @Test
    public void localAssetExists() {
        Asset a = new Asset("src/test/resources/mario.png", false);
        assertTrue(a.exists());
    }

    @Test
    public void localAssetNotExists() {
        Asset a = new Asset("src/test/resources/luigi.png", false);
        assertFalse(a.exists());
    }

    @Test
    public void getInvalidAssetReturnsNull() {
        assertNull(Assets.getAsset("luigi.png", true));
    }

    @Test
    public void getValidAssetReturnsNull() {
        assertNotNull(Assets.getAsset("mario.png", true));
    }

}
