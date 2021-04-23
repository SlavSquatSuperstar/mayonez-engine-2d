package mayonez;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.Logger;

public class Texture extends Component implements Renderable {

	private int width, height;
	private BufferedImage image;
	private Point origin, edge;

	public static final String TEXTURES_DIRECTORY = "assets/";

	public Texture(int width, int height, String path) {
		this.width = width;
		this.height = height;
		
		image = loadImage(path);
		origin = new Point();
		edge = (null == image) ? new Point() : new Point(image.getWidth(), image.getHeight());
	}

	@Override
	public void render(Graphics g) {
		int x = (int) parent.getPosition().x;
		int y = (int) parent.getPosition().y;
		// stretches the image asset to fit the object's dimensions (map image vertices
		// to screen)
		g.drawImage(image, x, y, x + width, y + height, origin.x, origin.y, edge.x, edge.y, null);
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
		return width;
	}

	public int getHeight() {
		return height;
	}

	// Static Methods

	// find a way to save images (to hash map?)
	public static BufferedImage loadImage(String path) {
		path = TEXTURES_DIRECTORY + path;
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			Logger.log("Texture: Failed to load image \"%s\"", path);
		}
		return null;
	}

}
