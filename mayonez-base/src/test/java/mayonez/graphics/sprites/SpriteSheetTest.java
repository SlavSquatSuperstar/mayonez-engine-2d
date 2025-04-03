package mayonez.graphics.sprites;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Constant values and helper functions for the {@link mayonez.graphics.sprites} tests.
 *
 * @author SlavSquatSuperstar
 */
abstract class SpriteSheetTest {

    static final String SPRITE_SHEET_FILENAME = "testassets/images/squares8.png";
    static final String SPACED_SPRITE_SHEET_FILENAME
            = "testassets/images/squares8_spaced.png";
    static final int SPRITE_LENGTH = 8;
    static final int NUM_SPRITES = 8;
    static final int NO_SPACING = 0;
    static final int SPACING = 1;

    static Color[] SPRITE_COLORS;
    static final Color MARKER_COLOR = Color.grayscale(128);

    @BeforeAll
    static void setup() {
        SPRITE_COLORS = new Color[]{
                new Color(255, 0, 0), new Color(0, 255, 0),
                new Color(0, 0, 255), Color.grayscale(255),
                new Color(0, 255, 255), new Color(255, 0, 255),
                new Color(255, 255, 0), Color.grayscale(0),
        };
    }

    protected static void testSubTexture(Texture texture, Color spriteColor) {
        assertEquals(new Vec2(SPRITE_LENGTH), texture.getSize());
        assertEquals(MARKER_COLOR, texture.getImageData()
                .getPixelColor(0, 0));
        assertEquals(spriteColor, texture.getImageData()
                .getPixelColor(SPRITE_LENGTH - 1, SPRITE_LENGTH - 1));
    }

}
