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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * An image used to display a {@link GameObject} or background.
 *
 * @author SlavSquatSuperstar
 */
public class Sprite extends Component {

    private BufferedImage image;

    public Sprite(String filename) {
        try {
            Texture texture = Assets.getAsset(filename, Texture.class);
            byte[] bytes = texture.getImageData();
//            byte[] bytes = Assets.readContents(filename);
            this.image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            Logger.log(ExceptionUtils.getStackTrace(e));
            Logger.log("Sprite: Error loading image \"%s\"", filename);
            Game.instance().stop(-1);
        }
    }

    public Sprite(BufferedImage image) {
        this.image = image; // TODO copy rather than reference?
    }

    @Override
    public final void update(float dt) {
    } // Sprites shouldn't update any game logic

    @Override
    public void render(Graphics2D g2) {
        // Measurements are in screen coordinates (pixels)
        Vec2 parentCenter = transform.position.mul(getScene().getCellSize()); // no camera offset
        Vec2 imageHalfSize = new Vec2(image.getWidth(), image.getHeight()).div(2);

        // Use the parent's transform to draw the sprite
        AffineTransform g2Transform = new AffineTransform();
        g2Transform.setToIdentity();
        g2Transform.translate(parentCenter.x - imageHalfSize.x,
                parentCenter.y - imageHalfSize.y); // Line up image center with parent center
        g2Transform.scale(transform.scale.x, transform.scale.y);
        g2Transform.rotate(MathUtils.toRadians(transform.rotation), imageHalfSize.x, imageHalfSize.y);

        g2.drawImage(image, g2Transform, null);
//        DebugDraw.drawPoint(parentCenter, Colors.BLUE);
    }

    public BufferedImage getImage() {
        return image;
    }

}
