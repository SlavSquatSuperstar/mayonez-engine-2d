package com.mayonez;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Scene {

	protected String name;
	protected int width, height;

	// TODO camera
	private boolean running;
	private boolean bounded = true;
	protected Color background;
	// private BoundType = BOUNDED;

//	private Camera camera;
//	private BufferedImage background;

	protected ArrayList<GameObject> objects, toRemove;

	public Scene(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;

		objects = new ArrayList<GameObject>();
		toRemove = new ArrayList<GameObject>();

//		camera = new Camera(this);
//		background = Texture.loadImage("background.png");
	}

	// User defined start behavior
	protected abstract void init();

	void start() {
		if (running)
			return;

		init();
		for (GameObject o : objects)
			o.start();
		running = true;
	}

	void update(double dt) {
		if (!running)
			return;

		// Update Objects and Camera
		for (GameObject o : objects) {
			o.update(dt);
			// Flag objects for destruction
			if (o.isDestroyed())
				removeObject(o);
		}
//		camera.update();

		// Remove destroyed objects at the end of the frame
		for (GameObject o : toRemove)
			objects.remove(o);
		toRemove.clear();
	}

	void render(Graphics2D g2) {
		if (!running)
			return;

		g2.setColor(background);
		g2.fillRect(0, 0, width, height);

		for (GameObject o : objects) {
			o.render(g2);
		}

		// g.drawImage(background, 0, 0, height, width, camera.getXOffset(),
		// camera.getYOffset(), background.getWidth(), background.getHeight(), null);

		// g.translate(camera.getXOffset(), camera.getYOffset());
		// g.translate(-camera.getXOffset(), -camera.getYOffset());
	}

	// Object Methods

	public void addObject(GameObject object) {
		object.setScene(this);
		object.init();
		objects.add(object);

		if (running)
			object.start();
	}

	public void removeObject(GameObject object) {
		object.destroy();
		toRemove.add(object);
	}

	// Getters and Setters

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public boolean isBounded() {
		return bounded;
	}

//	public Camera getCamera() {
//		return camera;
//	}

}
