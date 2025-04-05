package mayonez.graphics.sprites;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Creates multiple {@link GLSprite}s from a larger image.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLSpriteSheet extends SpriteSheet {

    /**
     * Creates a sprite sheet from the given texture.
     *
     * @param sheetTexture the parent texture
     * @param spriteSize   the dimensions of each sprite, in pixels
     * @param numSprites   how many sprites to create
     * @param spacing      the padding between sprites, in pixels
     */
    GLSpriteSheet(GLTexture sheetTexture, Vec2 spriteSize, int numSprites, int spacing) {
        super(sheetTexture, SpriteSplitters.getGLSpriteSplitter(
                sheetTexture.getSize(), spriteSize,
                new Vec2(spacing), numSprites
        ));
    }

    @Override
    public String toString() {
        return "GLSpriteSheet (%s)".formatted(getSheetTexture());
    }

}
