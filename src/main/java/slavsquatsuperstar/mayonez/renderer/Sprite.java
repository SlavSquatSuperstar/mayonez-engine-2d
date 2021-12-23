package slavsquatsuperstar.mayonez.renderer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.fileio.Assets;
import slavsquatsuperstar.mayonez.fileio.Texture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A component that draws an image at a {@link GameObject}'s position.
 *
 * @author SlavSquatSuperstar
 */
public class Sprite extends Component {

    private BufferedImage image;

    public Sprite(String filename) {
        try {
            image = Assets.getAsset(filename, Texture.class).getImage();
        } catch (NullPointerException e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            Logger.log("Sprite: Error loading image \"%s\"", filename);
            Game.stop(-1);
        }
    }

    public Sprite(BufferedImage image) {
        this.image = image;
    }

    @Override
    public final void update(float dt) {
    } // Sprites shouldn't update any game logic

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
//        Logger.log("%.2f, %.2f", g2Xf.getTranslateX(), g2Xf.getTranslateY());

        g2.drawImage(image, g2Xf, null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public Sprite copy() {
        return new Sprite(image);
    }

}
