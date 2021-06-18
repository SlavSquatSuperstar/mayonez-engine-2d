package com.slavsquatsuperstar.mayonez.components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.slavsquatsuperstar.mayonez.Assets;
import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Logger;
import com.slavsquatsuperstar.mayonez.Preferences;

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
	public void render(Graphics2D g2) {
		AffineTransform transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(parent.transform.position.x, parent.transform.position.y);
		transform.scale(parent.transform.scale.x, parent.transform.scale.y);
		transform.rotate(Math.toRadians(parent.transform.rotation), Preferences.PLAYER_WIDTH / 2,
				Preferences.PLAYER_HEIGHT / 2);

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
