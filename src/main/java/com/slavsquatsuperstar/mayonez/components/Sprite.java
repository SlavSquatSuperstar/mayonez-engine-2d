package com.slavsquatsuperstar.mayonez.components;

import com.slavsquatsuperstar.mayonez.*;

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

    private BufferedImage image;

    public Sprite(String filename) {
        try {
            this.image = ImageIO.read(Assets.getFile(filename));
        } catch (Exception e) {
            Logger.log("Sprite: Error loading image %s", filename);
            System.exit(-1);
        }
    }

    public Sprite(BufferedImage image) {
        this.image = image; // TODO copy rather than reference?
    }

    @Override
    public void start() {
        // Move the parent so the top-left edge of this image is at the parent's previous positoin
        parent.transform.move(new Vector2(image().getWidth() / 2f, image().getHeight() / 2f));
    }

    @Override
    public void render(Graphics2D g2) {
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        transform.translate(parent.getX() - image.getWidth() / 2f, parent.getY() - image.getHeight() / 2f); // Draw at parent's center
//            transform.translate(parent.getX(), parent.getY());
        transform.scale(parent.transform.scale.x, parent.transform.scale.y);
        transform.rotate(Math.toRadians(parent.transform.rotation), image.getWidth() / 2f,
                image.getHeight() / 2f);
        g2.drawImage(image, transform, null);
    }

    // Getters and Setters

    public BufferedImage image() {
        return image;
    }

}
