package mayonez.graphics.sprites;

import org.junit.jupiter.api.*;

import static mayonez.graphics.sprites.SpriteSheetTest.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.JSpriteSheet} class.
 *
 * @author SlavSquatSuperstar
 */
class JSpriteSheetTest {

    @Test
    void subTextureContentsCorrect() {
        var textures = getTextures(false, false);
        testTexturesCorrect(textures);
    }

    @Test
    void spacedSubTextureContentsCorrect() {
        var textures = getTextures(true, false);
        testTexturesCorrect(textures);
    }

}
