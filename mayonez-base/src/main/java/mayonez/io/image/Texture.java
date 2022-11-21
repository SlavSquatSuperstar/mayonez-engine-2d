package mayonez.io.image;

import mayonez.io.Asset;

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

    public abstract int getWidth();

    public abstract int getHeight();

}
