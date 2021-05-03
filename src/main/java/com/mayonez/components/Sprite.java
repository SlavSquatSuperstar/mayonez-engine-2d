package com.mayonez.components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mayonez.GameObject;
import com.util.Logger;
import com.util.Preferences;

/**
 * An image used to display a {@link GameObject} or background.
 * 
 * @author SlavSquatSuperstar
 */
public class Sprite extends Component {

	private BufferedImage image;
	public int width, height;
//	private Point origin, edge; // top left and bottom right

	public Sprite(String filename) {
		try {
			File file = new File(filename);
			this.image = ImageIO.read(file);
			this.width = image.getWidth();
			this.height = image.getHeight();

//			origin = new Point();
//			edge = (null == image) ? new Point() : new Point(image.getWidth(), image.getHeight());
		} catch (Exception e) {
			Logger.log("Sprite: Error loading image %s", filename);
			System.exit(-1);
		}
	}

	public Sprite(BufferedImage image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	@Override
	public void render(Graphics2D g2) {
		AffineTransform transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(parent.transform.position.x, parent.transform.position.y);
		transform.scale(parent.transform.scale.x, parent.transform.scale.y);
		transform.rotate(Math.toRadians(parent.transform.rotation), Preferences.PLAYER_WIDTH / 2, Preferences.PLAYER_HEIGHT / 2);
		
		g2.drawImage(image, transform, null);
		
//		int x = (int) parent.transform.position.x;
//		int y = (int) parent.transform.position.y;
//		g2.drawImage(image, x, y, width, height, null);

		// stretches the image asset to fit the object's dimensions (map image vertices
		// to screen)
//		g2.drawImage(image, x, y, x + width, y + height, origin.x, origin.y, edge.x, edge.y, null);
	}

	// Image Manipulation Methods

//	public void flipHorizontal() {
//		int temp = origin.x;
//		origin.x = edge.x;
//		edge.x = temp;
//	}
//
//	public void flipVertical() {
//		int temp = origin.y;
//		origin.y = edge.y;
//		edge.y = temp;
//	}

	// Getters and Setters

	public BufferedImage getImage() {
		return image;
	}

}
