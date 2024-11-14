package mayonez.io.text;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

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
        try (var input = filePath.openInputStream()) {
            var text = new TextIOManager().read(input);
            assertFalse(text.isEmpty());
            IOTestUtils.assertInputStreamOpen(input);
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
        try (var output = filePath.openOutputStream(false)) {
            new TextIOManager().write(output, text);
            IOTestUtils.assertOutputStreamOpen(output);
        } catch (IOException e) {
            fail();
        }
    }

}
