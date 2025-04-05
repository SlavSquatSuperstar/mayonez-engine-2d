package mayonez.graphics.sprites;

import mayonez.assets.image.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Constant values and helper functions for the {@link mayonez.graphics.sprites}
 * tests.
 *
 * @author SlavSquatSuperstar
 */
abstract class SpriteSheetTest {

    // Sprite Sheet Files
    private static final String SPRITE_SHEET_FILENAME = "testassets/images/squares8.png";
    private static final String SPACED_SPRITE_SHEET_FILENAME
            = "testassets/images/squares8_spaced.png";

    // Sprite Sheet Parameters
    private static final int SPRITE_LENGTH = 8;
    private static final int NUM_SPRITES = 8;
    private static final int NO_SPACING = 0;
    private static final int SPACING = 1;

    // Color constants
    static final Color[] SPRITE_COLORS;
    static final Color MARKER_COLOR = Color.grayscale(128);

    static {
        SPRITE_COLORS = new Color[]{
                new Color(255, 0, 0),
                new Color(0, 255, 0),
                new Color(0, 0, 255),
                Color.grayscale(255),

                new Color(0, 255, 255),
                new Color(255, 0, 255),
                new Color(255, 255, 0),
                Color.grayscale(0),
        };
    }

    // Texture Helpers

    static Texture getTexture(boolean spaced, boolean useGL) {
        var filename = spaced ? SPACED_SPRITE_SHEET_FILENAME : SPRITE_SHEET_FILENAME;
        if (useGL) return Textures.getGLTexture(filename);
        else return Textures.getJTexture(filename);
    }

    // Sprite Sheet Helpers

    static Texture[] getTextures(boolean spaced, boolean useGL) {
        var sheetTexture = getTexture(spaced, useGL);
        var spacing = spaced ? SPACING : NO_SPACING;

        SpriteSheet spriteSheet;
        if (useGL) {
            spriteSheet = new GLSpriteSheet((GLTexture) sheetTexture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, spacing);
        } else {
            spriteSheet = new JSpriteSheet((JTexture) sheetTexture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, spacing);
        }
        return spriteSheet.getTextures();
    }

    static void testTexturesCorrect(Texture[] textures) {
        assertEquals(NUM_SPRITES, textures.length);
        for (int i = 0; i < NUM_SPRITES; i++) {
            var texture = textures[i];
            assertEquals(new Vec2(SPRITE_LENGTH), texture.getSize());
            assertEquals(MARKER_COLOR, texture.getImageData()
                    .getPixelColor(0, 0));
            assertEquals(SPRITE_COLORS[i], texture.getImageData()
                    .getPixelColor(SPRITE_LENGTH - 1, SPRITE_LENGTH - 1));
        }
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

    static ImageRegion[] getSpriteRegions(boolean spaced, boolean useGL) {
        var sheetTexture = getTexture(spaced, useGL);
        var spacing = spaced ? SPACING : NO_SPACING;

        SpriteSplitter splitter;
        if (useGL) {
            splitter = SpriteSplitters.getGLSpriteSplitter(
                    sheetTexture.getSize(), new Vec2(SPRITE_LENGTH),
                    new Vec2(spacing), NUM_SPRITES
            );
        } else {
            splitter = SpriteSplitters.getJSpriteSplitter(
                    sheetTexture.getSize(), new Vec2(SPRITE_LENGTH),
                    new Vec2(spacing), NUM_SPRITES
            );
        }
        return splitter.getSpriteRegions();
    }

    static void testRegionsCorrect(ImageRegion[] regions, Vec2[] spriteOrigins) {
        assertEquals(NUM_SPRITES, regions.length);
        for (int i = 0; i < NUM_SPRITES; i++) {
            assertEquals(spriteOrigins[i], regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }

}
