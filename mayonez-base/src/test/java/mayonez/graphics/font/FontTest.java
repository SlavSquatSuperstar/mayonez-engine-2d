package mayonez.graphics.font;

import mayonez.assets.text.*;
import mayonez.util.Record;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Font} class.
 *
 * @author SlavSquatSuperstar
 */
class FontTest {

    private static Record record;
    private static String widthsStr;

    @BeforeAll
    static void readFontFiles() {
        var json = new JSONFile("assets/fonts/font_pixel.json");
        record = json.readJSON();

        var text = new TextFile("assets/fonts/font_pixel_widths.txt");
        var widthsLines = text.readLines();
        widthsStr = String.join("", widthsLines);
    }

    @Test
    void widthsLengthAndNumCharactersAreSame() {
        var data = new FontMetadata(record);
        assertEquals(data.numCharacters(), widthsStr.length());
    }

    @Test
    void readWidthsFromString() {
        var widths = new int[widthsStr.length()];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = Integer.parseInt(widthsStr, i, i + 1, 10);
        }
        System.out.println(Arrays.toString(widths));
    }

}
