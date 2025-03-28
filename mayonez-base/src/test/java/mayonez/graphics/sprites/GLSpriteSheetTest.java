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

    private GLSpriteSheet spriteSheet;

    @BeforeEach
    void getSpriteSheet() {
        var texture = Textures.getGLTexture(SpriteSheetTest.SPRITE_SHEET_FILENAME);
        spriteSheet = new GLSpriteSheet(texture, new Vec2(SpriteSheetTest.SPRITE_LENGTH), SpriteSheetTest.NUM_SPRITES, SpriteSheetTest.SPACING);
    }

    @Test
    void subTexturesInCorrectOrder() {
        var textures = spriteSheet.getTextures();
        assertEquals(SpriteSheetTest.NUM_SPRITES, textures.length);

        for (int i = 0; i < SpriteSheetTest.NUM_SPRITES; i++) {
            var texture = textures[i];
            assertEquals(SPRITE_COLORS[i],
                    texture.getImageData().getPixelColor(0, 0));
            assertEquals(SPRITE_COLORS[i],
                    texture.getImageData().getPixelColor(SpriteSheetTest.SPRITE_LENGTH - 1, SpriteSheetTest.SPRITE_LENGTH - 1));
        }
    }

}
