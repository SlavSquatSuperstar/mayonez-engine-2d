package mayonez.graphics.sprites;

import mayonez.assets.image.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Constant values and helper functions for the {@link mayonez.graphics.sprites}
 * tests.
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

    // Sprite Sheet Helpers

    protected static void testSubTexture(Texture texture, Color spriteColor) {
        assertEquals(new Vec2(SPRITE_LENGTH), texture.getSize());
        assertEquals(MARKER_COLOR, texture.getImageData()
                .getPixelColor(0, 0));
        assertEquals(spriteColor, texture.getImageData()
                .getPixelColor(SPRITE_LENGTH - 1, SPRITE_LENGTH - 1));
    }

    // Sprite Splitter Helpers

    static Vec2[] getSpriteOrigins(float[] spriteXs, float[] spriteYs) {
        var cols = spriteXs.length;
        var rows = spriteYs.length;

        var spriteOrigins = new Vec2[cols * rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                spriteOrigins[y * cols + x] = new Vec2(spriteXs[x], spriteYs[y]);
            }
        }
        return spriteOrigins;
    }

    static void testRegionsCorrect(ImageRegion[] regions, Vec2[] spriteOrigins) {
        assertEquals(NUM_SPRITES, regions.length);
        for (int i = 0; i < NUM_SPRITES; i++) {
            assertEquals(spriteOrigins[i], regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }
}
