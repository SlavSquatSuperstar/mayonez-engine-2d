package mayonez.graphics.font;

import mayonez.assets.text.*;
import mayonez.graphics.textures.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Font} class.
 *
 * @author SlavSquatSuperstar
 */
class FontTest {

    private static FontMetadata metadata;
    private static int[] fileWidths;

    @BeforeAll
    static void readFontFiles() {
        // Font metadata
        var json = new JSONFile("assets/fonts/font_pixel.json");
        var record = json.readJSON();
        metadata = new FontMetadata(record);

        // Widths file
        readGlyphWidths();
    }

    private static void readGlyphWidths() {
        var widthsFile = new TextFile("testassets/text/font_pixel_widths.txt");
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

    @Test
    void glyphWidthsFromImageIsCorrect() {
        // Font widths
        var fontTexture = Textures.getJTexture("assets/fonts/font_pixel.png");
        var imgWidths = Font.getGlyphWidths(metadata, fontTexture);
        assertArrayEquals(fileWidths, imgWidths);
    }

}
