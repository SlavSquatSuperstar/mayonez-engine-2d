package mayonez.assets.text;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.text.TextIOUtils} class.
 *
 * @author SlavSquatSuperstar
 */
class TextIOUtilsTest {

    private final FilePath inputFilePath = new ClasspathFilePath("testassets/text/foo.txt");
    private final String outputText = """
            Hello there!
            General Kenobi, you are a bold one!
            Your move.""";

    // Input Tests

    @Test
    void readTextFromFileSuccess() {
        try (var input = inputFilePath.openInputStream()) {
            var text = TextIOUtils.readText(input);
            assertFalse(text.isEmpty());
            IOTestUtils.assertInputStreamOpen(input);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void readLinesFromFileSuccess() {
        try (var input = inputFilePath.openInputStream()) {
            var lines = TextIOUtils.readLines(input);
            assertTrue(lines.length > 0);
            IOTestUtils.assertInputStreamOpen(input);
        } catch (IOException e) {
            fail();
        }
    }

    // Output Tests

    @Test
    void writeTextToFileSuccess() {
        var outputFilePath = new ExternalFilePath("src/test/resources/testassets/out/out2.txt");
        IOTestUtils.checkFileExists(outputFilePath.getFilename());

        try (var output = outputFilePath.openOutputStream(false)) {
            TextIOUtils.write(output, outputText);
            IOTestUtils.assertOutputStreamOpen(output);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void writeLinesToFileSuccess() {
        var outputFilePath = new ExternalFilePath("src/test/resources/testassets/out/out3.txt");
        IOTestUtils.checkFileExists(outputFilePath.getFilename());

        try (var output = outputFilePath.openOutputStream(false)) {
            TextIOUtils.write(output, outputText.split("\n"));
            IOTestUtils.assertOutputStreamOpen(output);
        } catch (IOException e) {
            fail();
        }
    }

}
