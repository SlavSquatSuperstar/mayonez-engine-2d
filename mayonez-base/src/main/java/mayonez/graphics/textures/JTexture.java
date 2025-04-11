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
public final class JTexture extends Texture {

    // Image Data Fields
    private final AWTImageData imageData;
    private final int width, height;
    private final JTexture parentTexture;

    /**
     * Create a brand-new JTexture with the given filename.
     *
     * @param filename the file location
     */
    @SuppressWarnings("unused") // Needed for Assets.getJTexture()
    public JTexture(String filename) {
        super(filename);
        imageData = readImage();
        if (imageData != null) {
            width = imageData.getWidth();
            height = imageData.getHeight();
        } else {
            width = 0;
            height = 0;
        }
        parentTexture = null;
    }

    /**
     * Create a JTexture from a portion of another texture.
     *
     * @param parentTexture the parent texture
     * @param region        the sub-image region
     * @param description   the description of the sub-image
     */
    private JTexture(JTexture parentTexture, ImageRegion region, String description) {
        super("%s (%s)".formatted(parentTexture.getFilename(), description));
        this.imageData = parentTexture.getImageData().getSubImageData(region);
        // Get new image size in px
        width = region.getWidth();
        height = region.getHeight();
        this.parentTexture = parentTexture;
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
     */
    public void draw(Graphics2D g2, Transform parentXf, Transform spriteXf, Color color) {
        if (imageData == null) return;

        // Draw sprite at parent center with parent rotation and scale
        var texXf = parentXf.combine(spriteXf);
        var g2Xf = getImageTransform(texXf);

        // Recolor the image (without modifying the original)
        var recoloredImage = getRecoloredImage(color);
        g2.drawImage(recoloredImage, g2Xf, null); // Draw buffered image
    }

    private AffineTransform getImageTransform(Transform texXf) {
        // Measurements are in screen coordinates (pixels)
        var parentCenter = texXf.getPosition();
        var parentSize = texXf.getScale();
        var parentHalfSize = parentSize.mul(0.5f);

        var g2Xf = AffineTransform.getTranslateInstance(
                parentCenter.x - parentHalfSize.x,
                parentCenter.y - parentHalfSize.y); // Move to object min
        g2Xf.rotate(MathUtils.toRadians(texXf.getRotation()), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / width, -parentSize.y / height); // Flip image vertically like GL
        g2Xf.translate(0.0, -height); // Move to object center
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
    public JTexture getSubTexture(ImageRegion region, String description) {
        return new JTexture(this, region, description);
    }

    @Override
    public AWTImageData getImageData() {
        return imageData;
    }

    @Override
    public Texture getParentTexture() {
        return super.getParentTexture();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
