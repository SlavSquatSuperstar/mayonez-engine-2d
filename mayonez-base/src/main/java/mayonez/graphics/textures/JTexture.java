package mayonez.graphics.textures;

import mayonez.*;
import mayonez.assets.image.*;
import mayonez.graphics.*;
import mayonez.graphics.Color;
import mayonez.math.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * An image file used by the AWT engine. This class should not be directly
 * instantiated. Instead, call {@link mayonez.graphics.textures.Textures#getTexture}.
 * See {@link mayonez.graphics.textures.Texture} for more information.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public sealed class JTexture extends Texture permits JSpriteSheetTexture {

    private final AWTImageData imageData;
    private final Vec2 imageSize;

    /**
     * Create a brand-new JTexture with the given filename.
     *
     * @param filename the file location
     */
    public JTexture(String filename) { // Needed for Assets.getJTexture()
        super(filename);
        imageData = readImage();
        if (imageData == null) {
            imageSize = new Vec2();
        } else {
            imageSize = new Vec2(imageData.getWidth(), imageData.getHeight());
        }
    }

    /**
     * Create a JTexture from a portion of another texture.
     *
     * @param filename  the file location
     * @param imageData the sub-image
     */
    protected JTexture(String filename, AWTImageData imageData) {
        super(filename);
        this.imageData = imageData;
        imageSize = new Vec2(imageData.getWidth(), imageData.getHeight());
    }

    // Image Methods

    @Override
    protected AWTImageData readImage() {
        try {
            var imageData = new AWTImageData(getFilename());
            Logger.debug("Loaded image %s", getFilename());
            return imageData;
        } catch (Exception e) {
            Logger.error("Could not read image file %s", getFilename());
            Logger.printStackTrace(e);
            return null;
        }
    }

    // Draw Methods

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
        if (imageData == null) return;

        // Draw sprite at parent center with parent rotation and scale
        var texXf = parentXf.combine(spriteXf);
        var g2Xf = getImageTransform(texXf, scale);

        // Recolor the image (without modifying the original)
        var recoloredImage = getRecoloredImage(color);
        g2.drawImage(recoloredImage, g2Xf, null); // Draw buffered image
    }

    private AffineTransform getImageTransform(Transform texXf, float scale) {
        // Measurements are in screen coordinates (pixels)
        var parentCenter = texXf.getPosition().mul(scale);
        var parentSize = texXf.getScale().mul(scale);
        var parentHalfSize = parentSize.mul(0.5f);

        var g2Xf = AffineTransform.getTranslateInstance(
                parentCenter.x - parentHalfSize.x,
                parentCenter.y - parentHalfSize.y); // Move to object min
        g2Xf.rotate(MathUtils.toRadians(texXf.getRotation()), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / imageSize.x, -parentSize.y / imageSize.y); // Flip image vertically like GL
        g2Xf.translate(0.0, -imageSize.y); // Move to object center
        return g2Xf;
    }

    // Source: https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/image/RescaleOp.html
    private BufferedImage getRecoloredImage(Color color) {
        if (color == null) return imageData.getImage();

        var recolor = new RescaleOp(
                new float[]{color.getFRed(), color.getFGreen(), color.getFBlue(), color.getFAlpha()},
                new float[4], null
        );
        return recolor.filter(imageData.getImage(), null);
    }

    // Image Getters

    @Override
    public AWTImageData getImageData() {
        return imageData;
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
