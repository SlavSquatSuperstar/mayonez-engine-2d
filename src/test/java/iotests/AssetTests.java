package iotests;

import com.slavsquatsuperstar.mayonez.assets.Asset;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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

}
