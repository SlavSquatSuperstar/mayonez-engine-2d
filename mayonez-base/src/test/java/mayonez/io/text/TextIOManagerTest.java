package mayonez.io.text;

import mayonez.io.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.text.TextIOManager} class.
 *
 * @author SlavSquatSuperstar
 */
class TextIOManagerTest {

    @Test
    void readTextFromFileSuccess() {
        var asset = new Asset("testassets/text/properties.txt");
        try {
            var input = asset.openInputStream();
            var text = new TextIOManager().read(input);
            assertTrue(text.length() > 0);
            assertThrows(IOException.class, input::readAllBytes); // make sure stream closed
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void writeTextToFileSuccess() {
        var filename = "src/test/resources/testassets/out/out2.txt";

        // Make sure file is present so the test works
        var file = new File(filename);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var asset = new Asset(filename);
        String text = """
                Hello there!
                General Kenobi, you are a bold one!
                Your move.
                """;
        try {
            var output = asset.openOutputStream(false);
            new TextIOManager().write(output, text);
            // make sure stream closed
            assertThrows(IOException.class, () -> output.write(text.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            fail();
        }
    }

}