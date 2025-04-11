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

    GLSpriteSheetTexture(GLTexture parentTexture, ImageRegion region, String description) {
        super("%s (%s)".formatted(parentTexture.getFilename(), description),
                parentTexture, region);
        this.parentTexture = parentTexture;
    }

    // Asset Methods

    @Override
    public void free() { // Don't do anything since we may need the parent texture
    }

    // Image Getters

    public GLTexture getParentTexture() {
        return parentTexture;
    }

}
