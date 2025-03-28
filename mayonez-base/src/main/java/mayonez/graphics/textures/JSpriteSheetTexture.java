package mayonez.graphics.textures;

import mayonez.assets.image.*;
import mayonez.graphics.*;

/**
 * A GL texture created from a sprite sheet that represents a portion of another
 * texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JSpriteSheetTexture extends JTexture {

    private final JTexture parentTexture;
    private final int width, height;

    JSpriteSheetTexture(JTexture parentTexture, ImageRegion region, String description) {
        super("%s (%s)".formatted(parentTexture.getFilename(), description),
                parentTexture.getImageData().getSubImageData(region));
        this.parentTexture = parentTexture;
        this.width = region.getWidth(); // Get new image size in px
        this.height = region.getHeight(); // Get new image size in px
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

}
