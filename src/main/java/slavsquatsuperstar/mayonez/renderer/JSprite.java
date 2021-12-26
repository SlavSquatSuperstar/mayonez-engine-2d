package slavsquatsuperstar.mayonez.renderer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.fileio.JTexture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A component that draws an image at a {@link GameObject}'s position.
 *
 * @author SlavSquatSuperstar
 */
public class JSprite extends Sprite {

    private BufferedImage image;

    public JSprite(String filename) {
        try {
            image = Assets.getAsset(filename, JTexture.class).getImage();
        } catch (NullPointerException e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            Logger.log("Sprite: Error loading image \"%s\"", filename);
            Mayonez.stop(-1);
        }
    }

    public JSprite(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void render(Graphics2D g2) {
        // Measurements are in screen coordinates (pixels)
        Vec2 parentCenter = transform.position.mul(getScene().getCellSize());
        Vec2 parentSize = transform.scale.mul(getScene().getCellSize());
        Vec2 parentHalfSize = parentSize.mul(0.5f);
        Vec2 imageSize = new Vec2(image.getWidth(), image.getHeight());

        // Draw sprite at parent center with parent rotation and scale
        AffineTransform g2Xf = new AffineTransform(); // Identity
        g2Xf.translate(parentCenter.x - parentHalfSize.x, parentCenter.y - parentHalfSize.y);
        g2Xf.rotate(MathUtils.toRadians(transform.rotation), parentHalfSize.x, parentHalfSize.y);
        g2Xf.scale(parentSize.x / imageSize.x, parentSize.y / imageSize.y);

        // Flip image vertically like GL
        g2Xf.scale(1, -1);
        g2Xf.translate(0, -imageSize.y); // Move to correct position

        g2.drawImage(image, g2Xf, null);
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * Returns a new sprite with the image as this instance's but not attached to any {@link GameObject}.
     *
     * @return a copy of this image
     */
    @Override
    public JSprite copy() {
        return new JSprite(image);
    }

}
