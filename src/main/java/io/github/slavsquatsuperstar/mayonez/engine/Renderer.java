package io.github.slavsquatsuperstar.mayonez.engine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import io.github.slavsquatsuperstar.mayonez.levels.Level;

public class Renderer extends Canvas {

	// Renderer elements
	private BufferStrategy bs;
	private Graphics g;

	// Level
	private Level level; // find a better way

	// Size parameters
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

	public void render() {
		if (null == bs || null == g) {
			init();
			return;
		}
		if (null == level)
			return;

		g.clearRect(0, 0, width, height);
		level.render(g);
		bs.show();
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
