package mayonez.io.image;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.math.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

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
        try (var in = openInputStream()) {
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
        var imageSize = new Vec2(image.getWidth(), image.getHeight());

        // Draw sprite at parent center with parent rotation and scale
        AffineTransform g2Xf = rotateAndScaleTexture(texXf, imageSize, scale);
        transformAndDrawTexture(g2, imageSize, g2Xf);
    }

    private static AffineTransform rotateAndScaleTexture(Transform texXf, Vec2 imageSize, float scale) {
        var parentCenter = texXf.getPosition().mul(scale);
        var parentSize = texXf.getScale().mul(scale);
        var parentHalfSize = parentSize.mul(0.5f);

        var g2Xf = AffineTransform.getTranslateInstance(
                parentCenter.x - parentHalfSize.x, parentCenter.y - parentHalfSize.y); // Parent min
        g2Xf.rotate(FloatMath.toRadians(texXf.getRotation()), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / imageSize.x, -parentSize.y / imageSize.y); // Flip image vertically like GL
        return g2Xf;
    }

    private void transformAndDrawTexture(Graphics2D g2, Vec2 imageSize, AffineTransform g2Xf) {
        g2Xf.translate(0.0, -imageSize.y);
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
