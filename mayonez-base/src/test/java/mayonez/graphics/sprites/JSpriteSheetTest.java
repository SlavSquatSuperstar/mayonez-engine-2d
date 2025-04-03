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
    private Vec2[] spriteOrigins, spacedSpriteOrigins;

    @BeforeEach
    void getSpriteSheet() {
        var texture = Textures.getJTexture(SPRITE_SHEET_FILENAME);
        spriteSheet = new JSpriteSheet(texture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, NO_SPACING);

        var spacedTexture = Textures.getJTexture(SPACED_SPRITE_SHEET_FILENAME);
        spacedSpriteSheet = new JSpriteSheet(spacedTexture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, SPACING);

        spriteOrigins = new Vec2[NUM_SPRITES];
        spacedSpriteOrigins = new Vec2[NUM_SPRITES];

        var rows = 2;
        var cols = NUM_SPRITES / rows;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                spriteOrigins[y * cols + x] = new Vec2(x, y).mul(SPRITE_LENGTH);
                spacedSpriteOrigins[y * cols + x] = new Vec2(x, y)
                        .mul(SPRITE_LENGTH + SPACING);
            }
        }
    }

    @Test
    void subTextureContentsCorrect() {
        var textures = spriteSheet.getTextures();
        assertEquals(NUM_SPRITES, textures.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            var texture = textures[i];
            testSubTexture(texture, SPRITE_COLORS[i]);
        }
    }

    @Test
    void subTextureOriginsCorrect() {
        var regions = spriteSheet.getSpriteRegions();
        assertEquals(NUM_SPRITES, regions.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            assertEquals(spriteOrigins[i], regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }

    @Test
    void spacedSubTextureContentsCorrect() {
        var textures = spacedSpriteSheet.getTextures();
        assertEquals(NUM_SPRITES, textures.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            var texture = textures[i];
            testSubTexture(texture, SPRITE_COLORS[i]);
        }
    }

    @Test
    void spacedSubTextureOriginsCorrect() {
        var regions = spacedSpriteSheet.getSpriteRegions();
        assertEquals(NUM_SPRITES, regions.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            assertEquals(spacedSpriteOrigins[i], regions[i].origin());
            assertEquals(new Vec2(SPRITE_LENGTH), regions[i].size());
        }
    }

}
