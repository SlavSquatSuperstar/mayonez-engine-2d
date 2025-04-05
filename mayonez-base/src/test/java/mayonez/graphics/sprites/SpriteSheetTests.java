package mayonez.graphics.sprites;

import org.junit.jupiter.api.*;

import static mayonez.graphics.sprites.SpriteTestUtils.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.SpriteSheet} class.
 *
 * @author SlavSquatSuperstar
 */
class SpriteSheetTests {

    @Test
    void awtSubTextureContentsCorrect() {
        var textures = getTextures(false, false);
        testTexturesCorrect(textures);
    }

    @Test
    void awtSpacedSubTextureContentsCorrect() {
        var textures = getTextures(true, false);
        testTexturesCorrect(textures);
    }

    @Test
    void glSubTextureContentsCorrect() {
        var textures = getTextures(false, true);
        testTexturesCorrect(textures);
    }

    @Test
    void glSpacedSubTextureContentsCorrect() {
        var textures = getTextures(true, true);
        testTexturesCorrect(textures);
    }

}
