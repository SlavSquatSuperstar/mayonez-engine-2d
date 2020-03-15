package mayonez.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import mayonez.level.Level;

public class Renderer extends Canvas {

	private static final long serialVersionUID = 2L;

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

	}

	private void init() {

		if (!isVisible())
			return;

		createBufferStrategy(3);
		bs = getBufferStrategy();
		g = bs.getDrawGraphics();

	}

	public void render(Level l) {

		if (null == bs || null == g) {
			init();
			return;
		}

		g.clearRect(0, 0, width, height);

		l.render(g);
		
		bs.show();
	}

}
