package mayonez.graphics.sprites;

import mayonez.graphics.textures.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.GLSpriteSheet} class.
 *
 * @author SlavSquatSuperstar
 */
class GLSpriteSheetTest extends SpriteSheetTest {

    private GLSpriteSheet spriteSheet, spacedSpriteSheet;

    @BeforeEach
    void getSpriteSheet() {
        var texture = Textures.getGLTexture(SpriteSheetTest.SPRITE_SHEET_FILENAME);
        spriteSheet = new GLSpriteSheet(texture, new Vec2(SpriteSheetTest.SPRITE_LENGTH), SpriteSheetTest.NUM_SPRITES, SpriteSheetTest.NO_SPACING);

        var spacedTexture = Textures.getGLTexture(SPACED_SPRITE_SHEET_FILENAME);
        spacedSpriteSheet = new GLSpriteSheet(spacedTexture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, SPACING);
    }

    @Test
    void subTextureContentsCorrect() {
        var textures = spriteSheet.getTextures();
        assertEquals(SpriteSheetTest.NUM_SPRITES, textures.length);

        for (int i = 0; i < SpriteSheetTest.NUM_SPRITES; i++) {
            var texture = textures[i];
            assertEquals(new Vec2(SPRITE_LENGTH), texture.getSize());
            assertEquals(MARKER_COLOR,
                    texture.getImageData().getPixelColor(0, 0));
            assertEquals(SPRITE_COLORS[i],
                    texture.getImageData().getPixelColor(SpriteSheetTest.SPRITE_LENGTH - 1, SpriteSheetTest.SPRITE_LENGTH - 1));
        }
    }

    @Test
    void subTextureOriginsCorrect() {
        var regions = spriteSheet.getSpriteRegions();
        assertEquals(NUM_SPRITES, regions.length);

        // Need to flip for GL
        var start = spriteSheet.getSheetTexture().getHeight() - SPRITE_LENGTH;
        for (int i = 0; i < NUM_SPRITES; i++) {
            var origin = new Vec2(SPRITE_ORIGINS[i].x, start - SPRITE_ORIGINS[i].y);
            assertEquals(origin, regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }

    @Test
    void spacedSubTextureContentsCorrect() {
        var textures = spacedSpriteSheet.getTextures();
        assertEquals(NUM_SPRITES, textures.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            var texture = textures[i];
            assertEquals(new Vec2(SPRITE_LENGTH), texture.getSize());
            assertEquals(MARKER_COLOR,
                    texture.getImageData().getPixelColor(0, 0));
            assertEquals(SPRITE_COLORS[i],
                    texture.getImageData().getPixelColor(SPRITE_LENGTH - 1, SPRITE_LENGTH - 1));
        }
    }

    @Test
    void spacedSubTextureOriginsCorrect() {
        var regions = spacedSpriteSheet.getSpriteRegions();
        assertEquals(NUM_SPRITES, regions.length);

        // Need to flip for GL
        var start = spriteSheet.getSheetTexture().getHeight() - SPRITE_LENGTH + SPACING;
        for (int i = 0; i < NUM_SPRITES; i++) {
            var origin = new Vec2(SPACED_SPRITE_ORIGINS[i].x, start - SPACED_SPRITE_ORIGINS[i].y + SPACING);
            assertEquals(origin, regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }

}
