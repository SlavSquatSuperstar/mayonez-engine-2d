package slavsquatsuperstar.mayonez.renderer;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.assets.Assets;
import slavsquatsuperstar.mayonez.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * An image used to display a {@link GameObject} or background.
 *
 * @author SlavSquatSuperstar
 */
public class Sprite extends Component {

    private static final String TEXTURES_DIRECTORY = "assets/";
    private BufferedImage image;

    public Sprite(String filename) {
        filename = TEXTURES_DIRECTORY + filename;
        try {
            this.image = ImageIO.read(Assets.getAsset(filename, true).path);
        } catch (Exception e) {
            Logger.log("Sprite: Error loading image \"%s\"", filename);
            Game.instance().stop(-1);
        }
    }

    public Sprite(BufferedImage image) {
        this.image = image; // TODO copy rather than reference?
    }

    @Override
    public final void update(float dt) {} // Sprites shouldn't update any game logic

    @Override
    public void render(Graphics2D g2) {
        // Measurements are in screen coordinates (pixels)
        Vector2 parentCenter = parent.transform.position.mul(getScene().getCellSize()); // no camera offset
        Vector2 imageHalfSize = new Vector2(image.getWidth(), image.getHeight()).div(2);

        // Use the parent's transform to draw the sprite
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        transform.translate(parentCenter.x - imageHalfSize.x,
                parentCenter.y - imageHalfSize.y); // Line up image center with parent center
        transform.scale(parent.transform.scale.x, parent.transform.scale.y);
        transform.rotate(Math.toRadians(parent.transform.rotation), imageHalfSize.x, imageHalfSize.y);

        g2.drawImage(image, transform, null);
        DebugDraw.drawPoint(parentCenter, Colors.BLUE);
    }

    public BufferedImage getImage() {
        return image;
    }

}
