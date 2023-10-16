package mayonez.assets;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.Asset} class.
 *
 * @author SlavSquatSuperstar
 */
class AssetTest {

    private final String classpathImg = "testassets/images/mario.png";
    private final String externalImg = "src/test/resources/testassets/images/mario.png";

    @Test
    void assetIsClasspath() {
        var asset = new Asset(classpathImg);
        assertEquals(LocationType.CLASSPATH, asset.getLocationType());
    }

    @Test
    void assetIsExternal() {
        var asset = new Asset(externalImg);
        assertEquals(LocationType.EXTERNAL, asset.getLocationType());
    }

    @Test
    void classpathAssetIsReadOnly() {
        var asset = new Asset(classpathImg);
        assertDoesNotThrow(() -> {
            InputStream in = asset.openInputStream();
            in.close();
        });
        assertThrows(IOException.class, () -> {
            OutputStream out = asset.openOutputStream(true);
            out.close();
        });
    }

    @Test
    void externalAssetCanReadAndWrite() {
        var asset = new Asset(externalImg);
        assertDoesNotThrow(() -> {
            InputStream in = asset.openInputStream();
            in.close();
        });
        assertDoesNotThrow(() -> {
            OutputStream out = asset.openOutputStream(true);
            out.close();
        });
    }

    @Test
    void nonExistingAssetIsWriteOnly() {
        var outputFilename = "src/test/resources/testassets/out/output.txt";
        var asset = new Asset(outputFilename);

        var file = new File(outputFilename);
        if (file.exists()) file.delete(); // Delete so the test works

        assertThrows(IOException.class, () -> {
            InputStream in = asset.openInputStream();
            in.close();
        });
	    assertDoesNotThrow(() -> {
            // Requires parent directory to exist but not file
            OutputStream out = asset.openOutputStream(true);
            out.close();
        });

        file.delete(); // Delete so the test still works next time
    }


}
