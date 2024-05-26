package mayonez.graphics.textures;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.Color;
import mayonez.math.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;

/**
 * An image file used by the AWT engine. This class should not be directly
 * instantiated. Instead, call {@link mayonez.graphics.textures.Textures#getTexture}.
 * See {@link mayonez.graphics.textures.Texture} for more information.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public sealed class JTexture extends Texture permits JSpriteSheetTexture {

    private BufferedImage image;
    private final Vec2 imageSize;

    /**
     * Create a brand-new JTexture with the given filename.
     *
     * @param filename the file location
     */
    public JTexture(String filename) {
        super(filename);
        imageSize = new Vec2();
        readImage();
    }

    /**
     * Create a JTexture from a portion of another texture.
     *
     * @param filename the file location
     * @param image    the sub-image
     */
    protected JTexture(String filename, BufferedImage image) {
        super(filename);
        this.image = image;
        imageSize = new Vec2(image.getWidth(), image.getHeight());
    }

    // Image Methods

    @Override
    protected void readImage() {
        try (var in = openInputStream()) {
            image = ImageIO.read(new ByteArrayInputStream(in.readAllBytes()));
            imageSize.set(image.getWidth(), image.getHeight());
            Logger.debug("Loaded image %s", getFilename());
        } catch (Exception e) {
            Logger.error("Could not read image file %s", getFilename());
        }
    }

    /**
     * Draws a texture with the given position, rotation and scale qualities.
     *
     * @param g2       the graphics object of the window
     * @param parentXf the transform of the parent object
     * @param spriteXf any additional transformations on the image
     * @param color    any recoloring of the image
     * @param scale    the scale of the scene
     */
    public void draw(Graphics2D g2, Transform parentXf, Transform spriteXf, Color color, float scale) {
        if (image == null) return;

        // Draw sprite at parent center with parent rotation and scale
        var texXf = parentXf.combine(spriteXf);
        var g2Xf = transformScreen(texXf, scale);

        // Recolor the image
        BufferedImage recoloredImage = getRecoloredImage(color);
        g2.drawImage(recoloredImage, g2Xf, null); // Draw buffered image
    }

    private AffineTransform transformScreen(Transform texXf, float scale) {
        // Measurements are in screen coordinates (pixels)
        var parentCenter = texXf.getPosition().mul(scale);
        var parentSize = texXf.getScale().mul(scale);
        var parentHalfSize = parentSize.mul(0.5f);

        var g2Xf = AffineTransform.getTranslateInstance(
                parentCenter.x - parentHalfSize.x,
                parentCenter.y - parentHalfSize.y); // Move to object min
        g2Xf.rotate(FloatMath.toRadians(texXf.getRotation()), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / imageSize.x, -parentSize.y / imageSize.y); // Flip image vertically like GL
        g2Xf.translate(0.0, -imageSize.y); // Move to object center
        return g2Xf;
    }

    // Source: https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/image/RescaleOp.html
    private BufferedImage getRecoloredImage(Color color) {
        if (color == null) return image;

        var recolor = new RescaleOp(
                new float[]{color.getRedNorm(), color.getGreenNorm(), color.getBlueNorm(), color.getAlphaNorm()},
                new float[4], null
        );
        return recolor.filter(image, null);
    }

    // Image Getters

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return (int) imageSize.x;
    }

    @Override
    public int getHeight() {
        return (int) imageSize.y;
    }

}
