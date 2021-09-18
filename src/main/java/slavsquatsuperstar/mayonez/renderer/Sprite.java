package slavsquatsuperstar.mayonez.renderer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import slavsquatsuperstar.fileio.Assets;
import slavsquatsuperstar.fileio.Texture;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Logger;

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
            Game.instance().stop(-1);
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
        Vec2 parentHalfSize = parentSize.div(2);

        // Use the parent's transform to draw the sprite
        AffineTransform g2Transform = new AffineTransform();
        g2Transform.setToIdentity();
        g2Transform.translate(parentCenter.x - parentHalfSize.x,
                parentCenter.y - parentHalfSize.y); // Line up image center with parent center
        g2Transform.rotate(MathUtils.toRadians(transform.rotation), parentHalfSize.x, parentHalfSize.y);
        g2Transform.scale(parentSize.x / image.getWidth(), parentSize.y / image.getHeight());

        g2.drawImage(image, g2Transform, null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public Sprite copy() {
        return new Sprite(image);
    }

}
