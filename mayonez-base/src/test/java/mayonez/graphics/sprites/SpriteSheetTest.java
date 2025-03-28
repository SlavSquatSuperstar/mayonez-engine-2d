package mayonez.graphics.sprites;

import mayonez.graphics.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

/**
 * Constant values and helper functions for the {@link mayonez.graphics.sprites} tests.
 *
 * @author SlavSquatSuperstar
 */
abstract class SpriteSheetTest {

    static final String SPRITE_SHEET_FILENAME
            = "testassets/images/squares-spritesheet.png";
    static final int SPRITE_LENGTH = 8;
    static final int NUM_SPRITES = 8;
    static final int SPACING = 0;

    static Vec2[] SPRITE_ORIGINS;
    static Color[] SPRITE_COLORS;

    @BeforeAll
    static void setup() {
        SPRITE_ORIGINS = new Vec2[SpriteSheetTest.NUM_SPRITES];

        var rows = 2;
        var cols = SpriteSheetTest.NUM_SPRITES / rows;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                SPRITE_ORIGINS[y * cols + x] = new Vec2(x, y).mul(SpriteSheetTest.SPRITE_LENGTH);
            }
        }

        SPRITE_COLORS = new Color[]{
                new Color(255, 0, 0), new Color(0, 255, 0),
                new Color(0, 0, 255), Color.grayscale(255),
                new Color(0, 255, 255), new Color(255, 0, 255),
                new Color(255, 255, 0), Color.grayscale(0),
        };
    }

}
