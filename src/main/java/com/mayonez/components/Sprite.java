package com.mayonez.components;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.mayonez.GameObject;
import com.util.Logger;

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
		int x = (int) parent.transform.position.x;
		int y = (int) parent.transform.position.y;
		g2.drawImage(image, x, y, width, height, null);

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
