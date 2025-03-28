package mayonez.graphics.textures;

import mayonez.assets.image.*;
import mayonez.graphics.*;

/**
 * A GL texture created from a sprite sheet that represents a portion of another
 * texture.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
final class GLSpriteSheetTexture extends GLTexture {

    private final GLTexture parentTexture;
    private final int width, height;

    GLSpriteSheetTexture(GLTexture parentTexture, ImageRegion region, String description) {
        super("%s (%s)".formatted(parentTexture.getFilename(), description),
                parentTexture, region);
        this.parentTexture = parentTexture;
        this.width = region.getWidth(); // Get new image size in px
        this.height = region.getHeight(); // Get new image size in px
    }

    // Asset Methods

    @Override
    public void free() { // Don't do anything since we may need the parent texture
    }

    // Image Getters

    public GLTexture getParentTexture() {
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
