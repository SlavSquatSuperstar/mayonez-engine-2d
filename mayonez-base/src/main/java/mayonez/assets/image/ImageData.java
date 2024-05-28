package mayonez.assets.image;

import mayonez.graphics.*;
import mayonez.math.*;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Stores the buffered image contents of an image file or resource using {@link java.awt.image}.
 *
 * @author SlavSquatSuperstar
 */
public class ImageData extends BaseImageData {

    private final BufferedImage image;

    public ImageData(String filename) throws IOException {
        super(filename);
        try (var stream = openInputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(stream.readAllBytes()));
        } catch (IOException e) {
            throw new IOException("Error reading buffered image");
        }
    }

    public ImageData(String filename, BufferedImage image) {
        super(filename);
        this.image = image;
    }

    // Image Getters

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getChannels() {
        return image.getColorModel().getNumComponents();
    }

    @Override
    public boolean hasAlpha() {
        return image.getColorModel().hasAlpha();
        // Can also check image.getAlphaRaster() != null
        // Or image.getColorModel().getTransparency() == Transparency.TRANSLUCENT
    }

    // BufferedImage Methods

    public BufferedImage getImage() {
        return image;
    }

    // TODO make return buffer
    public BufferedImage getSubImage(Vec2 topLeft, Vec2 size) {
        return image.getSubimage((int) topLeft.x, (int) topLeft.y, (int) size.x, (int) size.y);
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

    @Override
    public Color getPixelColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    @Override
    public void setPixelColor(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGBAValue());
        // AWT already restricts setting alpha for non-transparent images
    }

    public int[] getPixels() {
        int[] pixels = new int[getWidth() * getHeight()];
        image.getRGB(0, 0, getWidth(), getHeight(), pixels, 0, getWidth());
        return pixels;
    }

    public void setPixels(int[] pixels) {
        image.setRGB(0, 0, getWidth(), getHeight(), pixels, 0, getWidth());
    }

}
