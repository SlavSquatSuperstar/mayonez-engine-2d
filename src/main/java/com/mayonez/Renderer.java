package com.mayonez;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Renderer {

	private ArrayList<GameObject> objects;
	private Camera camera;

	public Renderer(Camera camera) {
		this.camera = camera;
		objects = new ArrayList<>();
	}

	/**
	 * Submit a {@link GameObject} for rendering.
	 */
	public void add(GameObject obj) {
		objects.add(obj);
	}

	public void remove(GameObject obj) {
		objects.remove(obj);
	}

	public void render(Graphics2D g2) {
		// Save the unmodified transform
		AffineTransform transform = g2.getTransform();

		// Move the screen and render everything at the offset position
		g2.translate(-camera.position.x, -camera.position.y);
		for (GameObject o : objects)
			o.render(g2);

		// Reset the screen's transform
		g2.setTransform(transform);
	}

}
