package mayonez.io.text;

import mayonez.io.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.text.LinesIOManager} class.
 *
 * @author SlavSquatSuperstar
 */
class LinesIOManagerTest {

    @Test
    void readLinesFromFileSuccess() {
        var asset = new Asset("testassets/text/properties.txt");
        try {
            var input = asset.openInputStream();
            var lines = new LinesIOManager().read(input);
            assertTrue(lines.length > 0);
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
        String[] lines = """
                Hello there!
                General Kenobi, you are a bold one!
                Your move.
                """.split("\n");
        try {
            var output = asset.openOutputStream(false);
            new LinesIOManager().write(output, lines);
            // make sure stream closed
            assertThrows(IOException.class,
                    () -> output.write(lines[0].getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            fail();
        }
    }

}