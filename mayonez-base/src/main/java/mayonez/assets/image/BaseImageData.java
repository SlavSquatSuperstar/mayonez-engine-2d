package mayonez.assets.image;

import mayonez.assets.*;
import mayonez.graphics.*;

/**
 * Stores the rasterized image contents of an image file used by the program.
 *
 * @author SlavSquatSuperstar
 */
public abstract class BaseImageData extends Asset {

    protected static final int RGB_CHANNELS = 3;
    protected static final int RGBA_CHANNELS = 4;

    public BaseImageData(String filename) {
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
     * @param x the pixel x's coordinate, from the top left, in pixels
     * @param y the pixel's y coordinate, from the top left, in pixels
     * @param color the pixel color to set
     */
    public abstract void setPixelColor(int x, int y, Color color);

    // TODO Copy Methods

}
