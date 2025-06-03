package mayonez.graphics.sprites;

import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Constructs {@link SpriteSplitters} for different types of textures.
 *
 * @author SlavSquatSuperstar
 */
public final class SpriteSplitters {

    private SpriteSplitters() {
    }

    /**
     * Create a sprite splitter with the given tiling parameters.
     *
     * @param sheetTexture the parent texture
     * @param spriteSize   the sub-sprite size
     * @param spacing      the spacing between sprites
     * @param maxSprites   the maximum number of sprites
     * @return the sprite splitter
     */
    public static SpriteSplitter getSpriteSplitter(
            Texture sheetTexture, Vec2 spriteSize, Vec2 spacing, int maxSprites
    ) {
        var sheetSize = sheetTexture.getSize();

        if (sheetTexture instanceof GLTexture) {
            return getGLSpriteSplitter(sheetSize, spriteSize, spacing, maxSprites);
        } else if (sheetTexture instanceof JTexture) {
            return getJSpriteSplitter(sheetSize, spriteSize, spacing, maxSprites);
        } else {
            // Shouldn't happen, but don't want to return null
            return getDefaultSplitter(sheetSize);
        }
    }

    /**
     * Create a tiled sprite splitter that reads sprites using GL coordinates
     * (bottom left as origin).
     *
     * @param sheetSize  the parent texture size
     * @param spriteSize the sub-sprite size
     * @param spacing    the spacing between sprites
     * @param maxSprites the maximum number of sprites
     * @return the sprite splitter
     */
    public static SpriteSplitter getGLSpriteSplitter(
            Vec2 sheetSize, Vec2 spriteSize, Vec2 spacing, int maxSprites
    ) {
        return new TiledSpriteSplitter(
                sheetSize, spriteSize, spacing, maxSprites,
                new Vec2(0, sheetSize.y - spriteSize.y), new Vec2(1, -1)
        );
    }

    /**
     * Create a tiled sprite splitter that reads sprites using AWT coordinates
     * (top left as origin).
     *
     * @param sheetSize  the parent texture size
     * @param spriteSize the sub-sprite size
     * @param spacing    the spacing between sprites
     * @param maxSprites the maximum number of sprites
     * @return the sprite splitter
     */
    public static SpriteSplitter getJSpriteSplitter(
            Vec2 sheetSize, Vec2 spriteSize, Vec2 spacing, int maxSprites
    ) {
        return new TiledSpriteSplitter(
                sheetSize, spriteSize, spacing, maxSprites,
                new Vec2(0, 0), new Vec2(1, 1)
        );
    }

    /**
     * Create a sprite splitter that returns just the parent sprite.
     *
     * @param sheetSize the parent texture size
     * @return the sprite splitter
     */
    private static SpriteSplitter getDefaultSplitter(Vec2 sheetSize) {
        return new TiledSpriteSplitter(
                sheetSize, sheetSize, new Vec2(0, 0), 1,
                new Vec2(0, 0), new Vec2(1, 1)
        );
    }

}
