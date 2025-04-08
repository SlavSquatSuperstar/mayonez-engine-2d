package mayonez.graphics.font;

import mayonez.assets.text.*;
import mayonez.graphics.textures.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.graphics.font.FontWidthHelper} class.
 *
 * @author SlavSquatSuperstar
 */
class FontWidthHelperTest {

    private static FontBlockMetadata metadata;
    private static int[] fileWidths;

    @BeforeAll
    static void readFontFiles() {
        // Font metadata
        var json = new JSONFile("testassets/font/test_ascii_block.json");
        var record = json.readJSON();
        metadata = new FontBlockMetadata(record);

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
        assertEquals(metadata.numCharacters(), fileWidths.length);
    }

    // TODO GL tests
    @Test
    void glyphWidthsFromImageIsCorrect() {
        // Font widths
        var fontTexture = Textures.getJTexture(metadata.textureFile());
        var imgWidths = FontWidthHelper.getGlyphWidths(metadata, fontTexture);
        assertArrayEquals(fileWidths, imgWidths);
    }

}
