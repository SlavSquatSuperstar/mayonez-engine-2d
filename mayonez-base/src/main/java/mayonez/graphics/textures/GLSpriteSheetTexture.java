package mayonez.graphics.textures;

import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

/**
 * A GL texture created from a sprite sheet that represents a portion of another
 * texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public final class GLSpriteSheetTexture extends GLTexture {

    private final GLTexture parentTexture;
    private final int width, height;

    /**
     * Create a sprite sheet texture from a portion of another texture.
     *
     * @param parentTexture    another texture
     * @param spriteSheetIndex the index of this texture on the atlas
     * @param spritePos        the bottom left corner of the sprite
     * @param spriteSize       the dimensions of the sprite
     */
    public GLSpriteSheetTexture(GLTexture parentTexture, int spriteSheetIndex, Vec2 spritePos, Vec2 spriteSize) {
        super(getSubSpriteFilename(parentTexture.getFilename(), spriteSheetIndex),
                parentTexture, getSubImageCoords(parentTexture, spritePos, spriteSize));
        this.parentTexture = parentTexture;
        this.width = (int) spriteSize.x; // get new image size in px
        this.height = (int) spriteSize.y; // get new image size in px
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

    // Helper Methods

    private static String getSubSpriteFilename(String parentFilename, int spriteSheetIndex) {
        return "%s (Sprite %d)".formatted(parentFilename, spriteSheetIndex);
    }

    private static Vec2[] getSubImageCoords(GLTexture sheetTexture, Vec2 spriteBottomLeft, Vec2 spriteSize) {
        // Normalize image coordinates to between 0-1
        var texSize = sheetTexture.getSize();
        var subImgMin = spriteBottomLeft.div(texSize);
        var subImgMax = spriteBottomLeft.add(spriteSize).div(texSize);
        var subImgSize = subImgMax.sub(subImgMin);
        return Rectangle.rectangleVertices(subImgMin.midpoint(subImgMax), subImgSize, 0);
    }

}
