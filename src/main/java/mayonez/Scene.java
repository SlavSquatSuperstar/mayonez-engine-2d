package mayonez;

import java.awt.Graphics;
import java.util.LinkedList;

import mayonez.graphics.Renderable;
import mayonez.graphics.Renderer;
import util.Logger;

public abstract class Scene {

	private String name;
	private int width, height;

	// TODO camera
	private boolean running;
	private boolean bounded = true;
	// private BoundType = BOUNDED;

//	private Camera camera;
//	private BufferedImage background;
	private Renderer renderer;

	protected LinkedList<GameObject> objects, toRemove;

	public Scene(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;

		objects = new LinkedList<GameObject>();
		toRemove = new LinkedList<GameObject>();

		renderer = new Renderer();

//		camera = new Camera(this);
//		background = Texture.loadImage("background.png");
		// load this after
	}

	// User defined start behavior
	protected abstract void init();

	void start() {
		if (running)
			return;

		running = true;
		init();
		for (GameObject o : objects)
			o.start();
		Logger.log("Scene: Started scene \"%s\"", name);
	}

	void update() {
		if (!running)
			return;

		// Update Objects and Camera
		for (GameObject o : objects) {
			o.update();
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

	void render(Graphics g) {
		if (!running)
			return;

		renderer.render(g);

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

		// Add any renderables into the renderer
		for (Component c : object.getComponents())
			if (c instanceof Renderable)
				renderer.addObject((Renderable) c);

		if (running)
			object.start();
	}

	public void removeObject(GameObject object) {
		object.destroy();
		for (Component c : object.getComponents())
			if (c instanceof Renderable)
				renderer.removeObject((Renderable) c);
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
