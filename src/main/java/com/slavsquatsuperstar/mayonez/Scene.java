package com.slavsquatsuperstar.mayonez;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * A collection of {@link GameObject}s that represents an in-game world.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Scene {

	protected String name;
	protected int width, height;

	private boolean started;
	protected boolean bounded;
	protected Color background;

	// Rendering Fields
	private Renderer renderer;
	private Camera camera;
	protected ArrayList<GameObject> objects, toRemove;

	public Scene(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;

		camera = new Camera(new Vector2(), width, height);
		renderer = new Renderer(camera);
		objects = new ArrayList<>();
		toRemove = new ArrayList<>();
	}

	// Game Methods

	/**
	 * Add necessary objects.
	 */
	protected void init() {
	}

	void start() {
		if (started)
			return;

		addObject(camera);
		init();
		objects.forEach(o -> o.start());
		started = true;
	}

	void update(float dt) {
		if (!started)
			return;

		// Update Objects and Camera
		objects.forEach(o -> {
			o.update(dt);
			// Flag objects for destruction
			if (o.isDestroyed())
				removeObject(o);
		});

		// Remove destroyed objects at the end of the frame
		toRemove.forEach(o -> {
			objects.remove(o);
			renderer.remove(o);
		});
		toRemove.clear();
	}

	void render(Graphics2D g2) {
		if (!started)
			return;

		g2.setColor(background);
		g2.fillRect(0, 0, width, height);

		renderer.render(g2);

		// g.drawImage(background, 0, 0, height, width, camera.getXOffset(),
		// camera.getYOffset(), background.getWidth(), background.getHeight(), null);
	}

	// Object Methods

	public void addObject(GameObject obj) {
		obj.setScene(this);
		objects.add(obj);
		renderer.add(obj);
		Logger.log("Scene: Added GameObject \"%s\"", obj.getName());

		if (started)
			obj.start();
	}

	public void removeObject(GameObject obj) {
		obj.destroy();
		toRemove.add(obj);
	}

	public ArrayList<GameObject> getObjects() {
		return objects;
	}

	public <T extends GameObject> ArrayList<T> getObjects(Class<T> cls) {
		ArrayList<T> found = new ArrayList<>();
		objects.forEach(o -> {
			if (cls == null || cls.isInstance(o))
				found.add(cls.cast(o));
		});
		return found;
	}

	// TODO use hash map or bin search?
	public GameObject getObject(String name) {
		for (GameObject o : objects)
			if (o.getName().equalsIgnoreCase(name))
				return o;
		return null;
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

	public Camera camera() {
		return camera;
	}

}
