package mayonez.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.Logger;

public class Texture {

	private BufferedImage image;
	private Point origin, edge; // top left and bottom right

	public static final String TEXTURES_DIRECTORY = "assets/";

	public Texture(String path) {
		image = loadImage(path);
		origin = new Point();
		edge = (null == image) ? new Point() : new Point(image.getWidth(), image.getHeight());
	}

	// Image Methods
	
	public void drawImage(Graphics g, int x, int y, int width, int height) {
		if (null == image)
			return;

		// stretches the image asset to fit the object's dimensions (map image vertices
		// to screen)
		g.drawImage(image, x, y, x + width, y + height, origin.x, origin.y, edge.x, edge.y, null);
	}

	public BufferedImage loadImage(String path) {
		path = TEXTURES_DIRECTORY + path;
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
			Logger.log("Texture: Loaded image \"%s\"", path);
		} catch (IOException e) {
			Logger.log("Texture: Failed to load image \"%s\"", path);
		}
		return image;
	}

	

	// Image Manipulation Methods

	public void flipHorizontal() {
		int temp = origin.x;
		origin.x = edge.x;
		edge.x = temp;
	}

	public void flipVertical() {
		int temp = origin.y;
		origin.y = edge.y;
		edge.y = temp;
	}

	// Getters and Setters

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public BufferedImage getImage() {
		return image;
	}

}
