package mayonez.io.image;

import mayonez.Logger;
import mayonez.Transform;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;
import mayonez.math.FloatMath;
import mayonez.math.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        try (var in = inputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(in).readAllBytes()));
            Logger.debug("Loaded image \"%s\"", getFilename());
        } catch (IOException e) {
            Logger.error("Could not read image \"%s\"", getFilename());
        } catch (NullPointerException e) {
            Logger.error("Image file \"%s\" not found", getFilename());
        }
    }

    /**
     * Draws a texture with the given position, rotation and scale qualities.
     *
     * @param g2       the graphics object of the window
     * @param parentXf the transform of the parent object
     * @param spriteXf any additional transformations of the sprite
     * @param scale    the scale of the scene
     */
    public void draw(Graphics2D g2, Transform parentXf, Transform spriteXf, float scale) {
        // Measurements are in screen coordinates (pixels)
        var texXf = parentXf.combine(spriteXf);
        var parentCenter = texXf.getPosition().mul(scale);
        var parentSize = texXf.getScale().mul(scale);
        var parentHalfSize = parentSize.mul(0.5f);
        var imageSize = new Vec2(image.getWidth(), image.getHeight());

        // Draw sprite at parent center with parent rotation and scale
        var g2Xf = AffineTransform.getTranslateInstance(
                parentCenter.x - parentHalfSize.x, parentCenter.y - parentHalfSize.y); // Parent min
        g2Xf.rotate(FloatMath.toRadians(texXf.getRotation()), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / imageSize.x, -parentSize.y / imageSize.y); // Flip image vertically like GL

        g2Xf.translate(0.0, -imageSize.y); // Move to correct position
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
