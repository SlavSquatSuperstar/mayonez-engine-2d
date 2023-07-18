package mayonez.graphics.textures;

import mayonez.io.*;
import mayonez.math.*;

/**
 * An image file used by this program.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class Texture extends Asset permits GLTexture, JTexture {

    private int spriteSheetIndex;

    public Texture(String filename) {
        super(filename);
        spriteSheetIndex = -1;
    }

    /**
     * Creates a texture from an image file. Called during instantiation.
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

    @SuppressWarnings("unchecked")
    public final <T extends Texture> T setSpriteSheetIndex(int spriteSheetIndex) {
        this.spriteSheetIndex = spriteSheetIndex;
        return (T) this;
    }

    @Override
    public String toString() {
        var str =  super.toString();
        if (spriteSheetIndex >= 0) {
            str += " (Sprite %d)".formatted(spriteSheetIndex);
        }
        return str;
    }

}
