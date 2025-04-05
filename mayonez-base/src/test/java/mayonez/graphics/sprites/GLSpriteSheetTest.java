package mayonez.graphics.sprites;

import org.junit.jupiter.api.*;

import static mayonez.graphics.sprites.SpriteSheetTest.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.GLSpriteSheet} class.
 *
 * @author SlavSquatSuperstar
 */
class GLSpriteSheetTest {

    @Test
    void subTextureContentsCorrect() {
        var textures = getTextures(false, true);
        testTexturesCorrect(textures);
    }

    @Test
    void spacedSubTextureContentsCorrect() {
        var textures = getTextures(true, true);
        testTexturesCorrect(textures);
    }

}
