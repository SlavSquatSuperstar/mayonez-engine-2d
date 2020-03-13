package mayonez.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Renderer extends Canvas {

	private static final long serialVersionUID = 2L;

	private BufferedImage img;
	private int[] pixels;

	// Renderer elements
	private BufferStrategy bs;
	private Graphics g;

	// parameters
	private int width, height;

	public Renderer(int width, int height) {

		this.width = width;
		this.height = height;

		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setFocusable(false);

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	}

	private void init() {

		if (!isVisible())
			return;

		createBufferStrategy(3);
		bs = getBufferStrategy();
		g = bs.getDrawGraphics();

	}

	public void render() {

		if (null == bs || null == g) {
			init();
			return;
		}
		
		g.clearRect(0, 0, width, height);
		g.drawImage(img, 0, 0, null);

		bs.show();
		
		for (int i = 0; i < pixels.length; i++)
			pixels[i] += i;
	}

}
