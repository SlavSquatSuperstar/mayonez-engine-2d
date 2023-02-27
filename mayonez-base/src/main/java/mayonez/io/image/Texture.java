package mayonez.io.image;

import mayonez.io.Asset;
import mayonez.math.Vec2;

/**
 * An image file used by this program.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class Texture extends Asset permits JTexture, GLTexture {

    public Texture(String filename) {
        super(filename);
    }

    /**
     * Creates a texture from an image file. Called during creation.
     */
    protected abstract void readImage();

    /**
     * The width of the image in pixels.
     *
     * @return the image width
     */
    public abstract int getWidth();

    /**
     * The height of the image in pixels.
     *
     * @return the image height
     */
    public abstract int getHeight();

    /**
     * The dimensions of the image in pixels.
     *
     * @return the image size
     */
    public Vec2 getSize() {
        return new Vec2(getWidth(), getHeight());
    }

}
