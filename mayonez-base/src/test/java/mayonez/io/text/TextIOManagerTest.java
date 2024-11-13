package mayonez.io.text;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

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
        var filePath = new ClasspathFilePath("testassets/text/foo.txt");
        try {
            var input = filePath.openInputStream();
            var text = new TextIOManager().read(input);
            assertFalse(text.isEmpty());
            assertThrows(IOException.class, input::readAllBytes); // make sure stream closed
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void writeTextToFileSuccess() {
        var filePath = new ExternalFilePath("src/test/resources/testassets/out/out2.txt");
        IOTestUtils.checkFileExists(filePath.getFilename());

        var text = """
                Hello there!
                General Kenobi, you are a bold one!
                Your move.
                """;
        try {
            var output = filePath.openOutputStream(false);
            new TextIOManager().write(output, text);
            // make sure stream closed
            assertThrows(IOException.class,
                    () -> output.write(text.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            fail();
        }
    }

}