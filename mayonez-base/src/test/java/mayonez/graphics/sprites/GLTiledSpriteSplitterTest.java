package mayonez.graphics.sprites;

import org.junit.jupiter.api.*;

import static mayonez.graphics.sprites.SpriteSheetTest.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.TiledSpriteSplitter} class
 * using GL coordinates.
 *
 * @author SlavSquatSuperstar
 */
class GLTiledSpriteSplitterTest {

    @Test
    void subTextureRegionsCorrect() {
        // Get expected regions
        var spriteXs = new float[]{0, 8, 16, 24};
        var spriteYs = new float[]{8, 0};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(false, true);
        testRegionsCorrect(regions, spriteOrigins);
    }

    @Test
    void spacedSubTextureRegionsCorrect() {
        // Get expected regions
        var spriteXs = new float[]{0, 9, 18, 27};
        var spriteYs = new float[]{10, 1};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(true, true);
        testRegionsCorrect(regions, spriteOrigins);
    }

}
