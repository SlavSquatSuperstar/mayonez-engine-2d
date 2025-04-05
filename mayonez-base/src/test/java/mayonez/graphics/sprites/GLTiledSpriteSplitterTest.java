package mayonez.graphics.sprites;

import mayonez.assets.image.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
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

        var regions = getSpriteRegions(SPRITE_SHEET_FILENAME, NO_SPACING);
        testRegionsCorrect(regions, spriteOrigins);
    }

    @Test
    void spacedSubTextureRegionsCorrect() {
        // Get expected regions
        var spriteXs = new float[]{0, 9, 18, 27};
        var spriteYs = new float[]{10, 1};
        var spriteOrigins = getSpriteOrigins(spriteXs, spriteYs);

        var regions = getSpriteRegions(SPACED_SPRITE_SHEET_FILENAME, SPACING);
        testRegionsCorrect(regions, spriteOrigins);
    }

    private static ImageRegion[] getSpriteRegions(String filename, int spacing) {
        var texture = Textures.getGLTexture(filename);
        var splitter = SpriteSplitters.getGLSpriteSplitter(
                texture.getSize(), new Vec2(SPRITE_LENGTH),
                new Vec2(spacing), NUM_SPRITES
        );
        return splitter.getSpriteRegions();
    }

}
