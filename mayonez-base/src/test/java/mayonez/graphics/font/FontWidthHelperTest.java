package mayonez.graphics.font;

import mayonez.assets.text.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.graphics.font.FontWidthHelper} class.
 *
 * @author SlavSquatSuperstar
 */
class FontWidthHelperTest {

    private static FontBlock block;
    private static int[] fileWidths;

    @BeforeAll
    static void readFontFiles() {
        // Font metadata
        var json = new JSONFile("testassets/font/test_ascii_block.json");
        var record = json.readJSON();
        block = new FontBlock(record);

        // Widths file
        readGlyphWidths();
    }

    private static void readGlyphWidths() {
        var widthsFile = new TextFile("testassets/font/test_ascii_widths.txt");
        var widthsLines = widthsFile.readLines();
        var widthsStr = String.join("", widthsLines);

        fileWidths = new int[widthsStr.length()];
        for (int i = 0; i < fileWidths.length; i++) {
            fileWidths[i] = Integer.parseInt(widthsStr, i, i + 1, 10);
        }
    }

    @Test
    void numGlyphsIsCorrect() {
        assertEquals(block.numCharacters(), fileWidths.length);
    }

    // TODO GL tests
    @Test
    void glyphWidthsFromImageIsCorrect() {
        // Font widths
        var imgWidths = FontWidthHelper.getGlyphWidths(block);
        assertArrayEquals(fileWidths, imgWidths);
    }

}
