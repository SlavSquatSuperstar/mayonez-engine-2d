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
        var filename = "testassets/text/foo.txt";
        try {
            var input = LocationType.CLASSPATH.openInputStream(filename);
            var text = new TextIOManager().read(input);
            assertFalse(text.isEmpty());
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
            fail(e.getMessage());
        }

        String text = """
                Hello there!
                General Kenobi, you are a bold one!
                Your move.
                """;
        try {
            var output = LocationType.EXTERNAL.openOutputStream(filename, false);
            new TextIOManager().write(output, text);
            // make sure stream closed
            assertThrows(IOException.class,
                    () -> output.write(text.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            fail();
        }
    }

}