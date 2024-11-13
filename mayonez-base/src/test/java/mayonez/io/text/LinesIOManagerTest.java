package mayonez.io.text;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

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
        var filePath = new ClasspathFilePath("testassets/text/foo.txt");
        try {
            var input = filePath.openInputStream();
            var lines = new LinesIOManager().read(input);
            assertTrue(lines.length > 0);
            assertThrows(IOException.class, input::readAllBytes); // make sure stream closed
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void writeTextToFileSuccess() {
        var filePath = new ExternalFilePath("src/test/resources/testassets/out/out2.txt");
        IOTestUtils.checkFileExists(filePath.getFilename());

        var lines = """
                Hello there!
                General Kenobi, you are a bold one!
                Your move.
                """.split("\n");
        try {
            var output = filePath.openOutputStream(false);
            new LinesIOManager().write(output, lines);
            // make sure stream closed
            assertThrows(IOException.class,
                    () -> output.write(lines[0].getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            fail();
        }
    }

}