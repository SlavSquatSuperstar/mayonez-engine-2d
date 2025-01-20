package mayonez.graphics.textures;

import mayonez.assets.*;
import mayonez.assets.image.*;
import mayonez.math.*;

/**
 * An image file used by this program. To instantiate a texture, call
 * {@link mayonez.graphics.textures.Textures#getTexture}. See
 * {@link mayonez.graphics.sprites.Sprite} for more information.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class Texture extends Asset permits GLTexture, JTexture {

    public Texture(String filename) {
        super(filename);
    }

    /**
     * Create a texture from an image file. Called during instantiation.
     *
     * @return the image file data
     */
    protected abstract ImageData readImage();

    /**
     * Get the underlying image data associated with this texture.
     *
     * @return the image data
     */
    public abstract ImageData getImageData();

    /**
     * The parent texture of this texture, if any.
     *
     * @return the parent texture, by default none
     */
    public Texture getParentTexture() {
        return null;
    }

    /**
     * The width of the texture in pixels.
     *
     * @return the texture width
     */
    public abstract int getWidth();

    /**
     * The height of the texture in pixels.
     *
     * @return the texture height
     */
    public abstract int getHeight();

    /**
     * The dimensions of the texture in pixels.
     *
     * @return the texture size
     */
    public Vec2 getSize() {
        return new Vec2(getWidth(), getHeight());
    }

}
