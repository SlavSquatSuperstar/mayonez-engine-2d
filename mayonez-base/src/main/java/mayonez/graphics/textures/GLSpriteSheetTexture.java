package mayonez.graphics.textures;

import mayonez.graphics.*;
import mayonez.math.*;
import org.jetbrains.annotations.NotNull;

/**
 * A GL texture created from a sprite sheet that represents a portion of another
 * texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSpriteSheetTexture extends GLTexture {

    private final GLTexture parentTexture;
    private final int spriteSheetIndex;
    private final int width, height;

    /**
     * Create a sprite sheet texture from a portion of another texture.
     *
     * @param parentTexture    another texture
     * @param spriteSheetIndex the index of this texture on the atlas
     * @param texCoords        the image coordinates, between 0-1
     */
    public GLSpriteSheetTexture(GLTexture parentTexture, int spriteSheetIndex, Vec2[] texCoords) {
        super(parentTexture.getFilename(), texCoords);
        this.parentTexture = parentTexture;
        this.spriteSheetIndex = spriteSheetIndex;

        var size = texCoords[2].sub(texCoords[0]); // relative size
        this.width = (int) (parentTexture.getWidth() * size.x); // get new image size
        this.height = (int) (parentTexture.getHeight() * size.y);
    }

    // Asset Methods

    @Override
    public void free() { // Don't do anything since we may need the parent texture
    }

    // Image Getters

    public GLTexture getParentTexture() {
        return parentTexture;
    }

    public int getParentWidth() {
        return parentTexture.getWidth();
    }

    public int getParentHeight() {
        return parentTexture.getHeight();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getTexID() {
        return parentTexture.getTexID();
    }

    @NotNull
    @Override
    public String toString() {
        return "%s (Sprite %d)".formatted(super.toString(), spriteSheetIndex);
    }

}
