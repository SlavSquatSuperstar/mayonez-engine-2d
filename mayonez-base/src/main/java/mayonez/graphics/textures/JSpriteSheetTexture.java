package mayonez.graphics.textures;

import mayonez.assets.image.*;
import mayonez.graphics.*;
import mayonez.math.*;

/**
 * A GL texture created from a sprite sheet that represents a portion of another
 * texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSpriteSheetTexture extends JTexture {

    private final JTexture parentTexture;
    private final int width, height;
    // TODO uvCoords

    public JSpriteSheetTexture(JTexture parentTexture, int spriteSheetIndex, Vec2 spritePos, Vec2 spriteSize) {
        this(getSubSpriteFilename(parentTexture.getFilename(), spriteSheetIndex),
                parentTexture, spritePos, spriteSize);
    }

    private JSpriteSheetTexture(String filename, JTexture parentTexture, Vec2 spritePos, Vec2 spriteSize) {
        super(filename, getSubImageData(filename, parentTexture.getImageData(), spritePos, spriteSize));
        this.parentTexture = parentTexture;
        this.width = (int) spriteSize.x; // get new image size in px
        this.height = (int) spriteSize.y; // get new image size in px
    }

    // Asset Methods

    @Override
    public void free() { // Don't do anything since we may need the parent texture
    }

    // Image Getters

    public JTexture getParentTexture() {
        return parentTexture;
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

    private static AWTImageData getSubImageData(String filename, AWTImageData imageData, Vec2 spritePos, Vec2 spriteSize) {
        return new AWTImageData(filename, imageData.getSubImage(spritePos, spriteSize));
    }

}
