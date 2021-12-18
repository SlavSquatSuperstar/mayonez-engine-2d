package slavsquatsuperstar.test.iotests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.fileio.Asset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}
