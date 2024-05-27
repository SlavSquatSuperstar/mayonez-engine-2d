package mayonez.assets.image;

import mayonez.assets.*;
import mayonez.graphics.*;
import mayonez.math.*;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Stores the rasterized contents of an image file or resource.
 *
 * @author SlavSquatSuperstar
 */
public class ImageData extends Asset {

    private final BufferedImage image;
    private final boolean alpha;

    public ImageData(String filename) throws IOException {
        super(filename);
        try (var stream = openInputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(stream.readAllBytes()));
            alpha = image.getColorModel().hasAlpha();
            // Can also check image.getAlphaRaster() != null
            // Or image.getColorModel().getTransparency() == Transparency.TRANSLUCENT
        } catch (IOException e) {
            throw new IOException("Error reading buffered image");
        }
    }

    public ImageData(String filename, BufferedImage image) {
        super(filename);
        this.image = image;
        alpha = image.getColorModel().hasAlpha();
    }

    // BufferedImage Methods

    // TODO make this private?
    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getSubImage(Vec2 topLeft, Vec2 size) {
        return image.getSubimage((int) topLeft.x, (int) topLeft.y, (int) size.x, (int) size.y);
    }

    // Image Getters

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public boolean hasAlpha() {
        return alpha;
    }

    // Pixel Methods

    public void recolor(Color color) {
        for (var y = 0; y < getHeight(); y++) {
            for (var x = 0; x < getWidth(); x++) {
                var pixelColor = getPixelColor(x, y);
                var combinedColor = pixelColor.combine(color);
                setPixelColor(x, y, combinedColor);
            }
        }
    }

    /**
     * Get the pixel's RBGA color on this sprite's stored texture at the
     * specific coordinates.
     */
    public Color getPixelColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    /**
     * Set the pixel's RBGA color on this sprite's stored texture at the
     * specific coordinates.
     */
    public void setPixelColor(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGBAValue());
        // TODO restrict opaque images from setting alpha?
    }

    public int[] getPixels() {
        int[] pixels = new int[getWidth() * getHeight()];
        image.getRGB(0, 0, getWidth(), getHeight(), pixels, 0, getWidth());
        return pixels;
    }

    public void setPixels(int[] pixels) {
        image.setRGB(0, 0, getWidth(), getHeight(), pixels, 0, getWidth());
    }

    // TODO copy methods

}
