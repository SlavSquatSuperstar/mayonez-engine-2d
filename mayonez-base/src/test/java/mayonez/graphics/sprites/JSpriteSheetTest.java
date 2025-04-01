package mayonez.graphics.sprites;

import mayonez.graphics.textures.*;
import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.JSpriteSheet} class.
 *
 * @author SlavSquatSuperstar
 */
class JSpriteSheetTest extends SpriteSheetTest {

    private JSpriteSheet spriteSheet, spacedSpriteSheet;

    @BeforeEach
    void getSpriteSheet() {
        var texture = Textures.getJTexture(SPRITE_SHEET_FILENAME);
        spriteSheet = new JSpriteSheet(texture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, NO_SPACING);

        var spacedTexture = Textures.getJTexture(SPACED_SPRITE_SHEET_FILENAME);
        spacedSpriteSheet = new JSpriteSheet(spacedTexture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, SPACING);
    }

    @Test
    void subTextureContentsCorrect() {
        var textures = spriteSheet.getTextures();
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
    void subTextureOriginsCorrect() {
        var regions = spriteSheet.getSpriteRegions();
        assertEquals(NUM_SPRITES, regions.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            assertEquals(SPRITE_ORIGINS[i], regions[i].origin());
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

        for (int i = 0; i < NUM_SPRITES; i++) {
            assertEquals(SPACED_SPRITE_ORIGINS[i], regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }

}
