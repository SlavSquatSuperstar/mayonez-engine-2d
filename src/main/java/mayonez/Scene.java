package mayonez;

import java.awt.Graphics;
import java.util.LinkedList;

import util.Logger;

public abstract class Scene {

//	public static final int UNBOUNDED = 0;
//	public static final int BOUNDED = 1;
//	public static final int WRAPPING = 2;

	private String name;
	private int width, height;

	// TODO camera
	private boolean running;
//	private int boundType = 1;
	private boolean bounded = true;

	protected LinkedList<GameObject> objects, toDestroy;
	protected LinkedList<Component> components;

	public Scene(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;

		objects = new LinkedList<GameObject>();
		components = new LinkedList<Component>();
		toDestroy = new LinkedList<GameObject>();

		init();
	}

	/**
	 * Add necessary objects
	 */
	protected void init() {
	}

	public void start() {
		running = true;
		Logger.log("%s: Started scene \"%s\"", getClass().getSimpleName(), name);

		for (GameObject o : objects)
			o.start();
	}

	public void update() {
		if (!running)
			return;

		for (GameObject o : objects) {
			o.update();
			// Flag objects for destruction
			if (o.isDestroyed())
				removeObject(o);
		}

		for (Component c : components)
			c.update();

		// Remove destroyed objects at the end of the frame
		for (GameObject o : toDestroy) {
			components.removeAll(o.getComponents());
			objects.remove(o);
			// set null for garbage collection?
		}

		toDestroy.clear();
	}

	public void render(Graphics g) {
		for (Component c : components)
			if (c instanceof Renderable)
				((Renderable) c).render(g);
	}

	// Object Methods

	public void addObject(GameObject object) {
		object.scene = this;
		objects.add(object);
		components.addAll(object.getComponents());
		if (running)
			object.start();
	}

	public void removeObject(GameObject object) {
		object.destroy();
		toDestroy.add(object);
	}

	// Getters and Setters

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isBounded() {
		return bounded;
	}

}
