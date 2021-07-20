package slavsquatsuperstar.mayonez.components;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.assets.Assets;

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
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        Vector2 imageCenter = new Vector2(image.getWidth() / 2f, image.getHeight() / 2f);
        transform.translate(parent.getX() * Preferences.TILE_SIZE - imageCenter.x,
                parent.getY() * Preferences.TILE_SIZE - imageCenter.y); // Draw at parent's center
        transform.scale(parent.transform.scale.x, parent.transform.scale.y);
        transform.rotate(Math.toRadians(parent.transform.rotation), imageCenter.x,
                imageCenter.y);
        Logger.log("Mario, Screen: (%.4f, %.4f)", transform.getTranslateX() + imageCenter.x, transform.getTranslateY() + imageCenter.y);
        g2.drawImage(image, transform, null);
    }

    public BufferedImage getImage() {
        return image;
    }

}
