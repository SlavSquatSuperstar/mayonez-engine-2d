package mayonez.graphics.sprites;

import org.junit.jupiter.api.*;

import static mayonez.graphics.sprites.SpriteSheetTest.*;

/**
 * Unit tests for the {@link mayonez.graphics.sprites.TiledSpriteSplitter} class
 * using AWT coordinates.
 *
 * @author SlavSquatSuperstar
 */
class JTiledSpriteSplitterTest {

    @Test
    void subTextureRegionsCorrect() {
        var spriteXs = new float[]{0, 8, 16, 24};
        var spriteYs = new float[]{0, 8};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(false, false);
        testRegionsCorrect(regions, spriteOrigins);
    }

    @Test
    void spacedSubTextureRegionsCorrect() {
        var spriteXs = new float[]{0, 9, 18, 27};
        var spriteYs = new float[]{0, 9};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(true, false);
        testRegionsCorrect(regions, spriteOrigins);
    }

}
