package mayonez.assets.image;

import mayonez.assets.*;
import mayonez.graphics.*;
import mayonez.math.*;

/**
 * Stores the rasterized image contents of an image file used by the program.
 *
 * @author SlavSquatSuperstar
 */
// TODO convert AWT and STB
public abstract class ImageData extends Asset {

    protected static final int RGB_CHANNELS = 3;
    protected static final int RGBA_CHANNELS = 4;

    public ImageData(String filename) {
        super(filename);
    }

    // Image Getters

    /**
     * Get the width of the image in pixels.
     *
     * @return the width
     */
    public abstract int getWidth();

    /**
     * Get the height of the image in pixels.
     *
     * @return the height
     */
    public abstract int getHeight();

    /**
     * Get the number of channels (color components) in pixels, 3 for RGB and
     * 4 for RGBA.
     *
     * @return the channel count
     */
    public abstract int getChannels();

    /**
     * Get whether the image supports alpha (transparency).
     *
     * @return if the image has alpha
     */
    public abstract boolean hasAlpha();

    // Pixel Methods
    // TODO bounds checking

    /**
     * Get the pixel's RBG color on this sprite's stored texture at the
     * specific coordinates.  If the image format supports transparency,
     * then the alpha value will also be returned.
     *
     * @param x the pixel x's coordinate, from the top left, in pixels
     * @param y the pixel's y coordinate, from the top left, in pixels
     * @return the pixel color
     */
    public abstract Color getPixelColor(int x, int y);

    /**
     * Set the pixel's RBG color on this sprite's stored texture at the
     * specific coordinates. If the image format supports transparency,
     * then the alpha value will also be set.
     *
     * @param x     the pixel x's coordinate, from the top left, in pixels
     * @param y     the pixel's y coordinate, from the top left, in pixels
     * @param color the pixel color to set
     */
    public abstract void setPixelColor(int x, int y, Color color);

    // Sub-Image Methods

    /**
     * Create an image resource from a sub-region of this image.
     *
     * @param topLeft the sub-image top left corner, in pixels
     * @param size    the sub-image dimensions, in pixels
     * @return the sub-image
     */
    public abstract ImageData getSubImageData(Vec2 topLeft, Vec2 size);

    // TODO Copy Methods

}
