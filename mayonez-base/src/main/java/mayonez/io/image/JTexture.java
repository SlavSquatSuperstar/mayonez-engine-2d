package mayonez.io.image;

import mayonez.math.FloatMath;
import mayonez.math.Vec2;
import mayonez.Logger;
import mayonez.Transform;
import mayonez.annotations.UsesEngine;
import mayonez.annotations.EngineType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * An image file used by the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JTexture extends Texture {

    private BufferedImage image;

    /**
     * Create a brand-new JTexture with the given filename.
     *
     * @param filename the file location
     */
    public JTexture(String filename) {
        super(filename);
        readImage();
    }

    /**
     * Create a JTexture from a portion of another texture.
     *
     * @param filename the file location
     * @param image    the sub-image
     */
    public JTexture(String filename, BufferedImage image) {
        super(filename);
        this.image = image;
    }

    // Image Methods

    @Override
    protected void readImage() {
        try (InputStream in = inputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(in).readAllBytes()));
            Logger.debug("I/O: Loaded image \"%s\"", getFilename());
        } catch (IOException e) {
            Logger.error("I/O: Could not read image \"%s\"", getFilename());
        } catch (NullPointerException e) {
            Logger.error("I/O: Image file \"%s\" not found", getFilename());
        }
    }

    /**
     * Draws a texture with the given position, rotation and scale qualities.
     *
     * @param g2       the graphics object of the window
     * @param parentXf the transform of the parent object
     * @param spriteXf any additional transformations of the sprite
     * @param cellSize the scale of the scene
     */
    public void draw(Graphics2D g2, Transform parentXf, Transform spriteXf, float cellSize) {
        // Measurements are in screen coordinates (pixels)
        Transform newXf = parentXf.combine(spriteXf);

        Vec2 parentCenter = (newXf.position.mul(cellSize));
        Vec2 parentSize = (newXf.scale.mul(cellSize));
        Vec2 parentHalfSize = parentSize.mul(0.5f);
        Vec2 imageSize = new Vec2(image.getWidth(), image.getHeight());

        // Draw sprite at parent center with parent rotation and scale
        AffineTransform g2Xf = new AffineTransform(); // Identity
        g2Xf.translate(parentCenter.x - parentHalfSize.x, parentCenter.y - parentHalfSize.y);
        g2Xf.rotate(FloatMath.toRadians(newXf.rotation), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / imageSize.x, parentSize.y / imageSize.y);

        g2Xf.scale(1, -1); // Flip image vertically like GL
        g2Xf.translate(0, -imageSize.y); // Move to correct position

        g2.drawImage(image, g2Xf, null);
    }

    // Image Getters

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
