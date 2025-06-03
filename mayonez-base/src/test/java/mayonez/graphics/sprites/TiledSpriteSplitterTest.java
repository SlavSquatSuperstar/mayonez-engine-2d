package mayonez.graphics.sprites;

import org.junit.jupiter.api.*;

import static mayonez.graphics.sprites.SpriteTestUtils.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.TiledSpriteSplitter} class.
 *
 * @author SlavSquatSuperstar
 */
class TiledSpriteSplitterTest {

    @Test
    void awtSubTextureRegionsCorrect() {
        var spriteXs = new float[]{0, 8, 16, 24};
        var spriteYs = new float[]{0, 8};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(false, false);
        testRegionsCorrect(regions, spriteOrigins);
    }

    @Test
    void awtSpacedSubTextureRegionsCorrect() {
        var spriteXs = new float[]{0, 9, 18, 27};
        var spriteYs = new float[]{0, 9};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(true, false);
        testRegionsCorrect(regions, spriteOrigins);
    }

    @Test
    void glSubTextureRegionsCorrect() {
        // Get expected regions
        var spriteXs = new float[]{0, 8, 16, 24};
        var spriteYs = new float[]{8, 0};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(false, true);
        testRegionsCorrect(regions, spriteOrigins);
    }

    @Test
    void glSpacedSubTextureRegionsCorrect() {
        // Get expected regions
        var spriteXs = new float[]{0, 9, 18, 27};
        var spriteYs = new float[]{10, 1};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(true, true);
        testRegionsCorrect(regions, spriteOrigins);
    }

}
