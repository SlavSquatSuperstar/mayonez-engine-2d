package com.slavsquatsuperstar.mayonez.components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Logger;
import com.slavsquatsuperstar.util.Constants;

/**
 * An image used to display a {@link GameObject} or background.
 * 
 * @author SlavSquatSuperstar
 */
public class Sprite extends Component {

	private BufferedImage image;

	public Sprite(String filename) {
		try {
			File file = new File(filename);
			this.image = ImageIO.read(file);
		} catch (Exception e) {
			Logger.log("Sprite: Error loading image %s", filename);
			System.exit(-1);
		}
	}

	public Sprite(BufferedImage image) {
		this.image = image; // TODO copy rather than reference?
	}

	@Override
	public void render(Graphics2D g2) {
		AffineTransform transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(parent.transform.position.x, parent.transform.position.y);
		transform.scale(parent.transform.scale.x, parent.transform.scale.y);
		transform.rotate(Math.toRadians(parent.transform.rotation), Constants.PLAYER_WIDTH / 2,
				Constants.PLAYER_HEIGHT / 2);

		g2.drawImage(image, transform, null);
	}

	// Getters and Setters

	public BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

}
