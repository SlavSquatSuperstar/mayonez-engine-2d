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

    private JSpriteSheet spriteSheet;

    @BeforeEach
    void getSpriteSheet() {
        var texture = Textures.getJTexture(SPRITE_SHEET_FILENAME);
        spriteSheet = new JSpriteSheet(texture, new Vec2(SPRITE_LENGTH), NUM_SPRITES, SPACING);
    }

    @Test
    void subTexturesInCorrectOrder() {
        var textures = spriteSheet.getTextures();
        assertEquals(NUM_SPRITES, textures.length);

        for (int i = 0; i < NUM_SPRITES; i++) {
            var texture = textures[i];
            assertEquals(SPRITE_COLORS[i],
                    texture.getImageData().getPixelColor(0, 0));
            assertEquals(SPRITE_COLORS[i],
                    texture.getImageData().getPixelColor(SPRITE_LENGTH - 1, SPRITE_LENGTH - 1));
        }
    }

}
