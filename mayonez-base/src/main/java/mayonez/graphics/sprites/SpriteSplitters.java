package mayonez.graphics.sprites;

import mayonez.math.*;

/**
 * Constructs {@link SpriteSplitters} for different purposes.
 *
 * @author SlavSquatSuperstar
 */
public final class SpriteSplitters {

    private SpriteSplitters() {
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

}
