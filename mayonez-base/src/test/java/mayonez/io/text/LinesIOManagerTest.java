package mayonez.io.text;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

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
            IOTestUtils.assertInputStreamOpen(input);
            input.close();
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
            IOTestUtils.assertOutputStreamOpen(output);
            output.close();
        } catch (IOException e) {
            fail();
        }
    }

}
