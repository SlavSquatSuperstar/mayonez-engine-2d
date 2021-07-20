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
        // Graphics screen coordinates with camera offset
        double g2X = g2.getTransform().getTranslateX();
        double g2Y = g2.getTransform().getTranslateY();

        // Measurements are in screen coordinates (pixels)
        Vector2 cameraOffset = new Vector2((float) g2X, (float) g2Y);
        Vector2 parentCenter = parent.transform.position.mul(Preferences.TILE_SIZE); // no offset
        Vector2 imageHalfSize = new Vector2(image.getWidth(), image.getHeight()).div(2);
        Logger.log("Sprite, Screen: (%.4f, %.4f)", parentCenter.x + g2X, parentCenter.y + g2Y);
//        DebugDraw.drawPoint(parentCenter.add(cameraOffset).add(imageHalfSize), Colors.BLUE);

        // Use the parent's transform to draw the sprite
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        transform.translate(parentCenter.x - image.getWidth(),
                parentCenter.y - image.getHeight()); // Draw at parent's center
        transform.scale(parent.transform.scale.x, parent.transform.scale.y);
        transform.rotate(Math.toRadians(parent.transform.rotation), imageHalfSize.x, imageHalfSize.y);

        g2.drawImage(image, transform, null);
    }

    public BufferedImage getImage() {
        return image;
    }

}
