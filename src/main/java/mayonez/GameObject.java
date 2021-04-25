package mayonez;

import java.util.LinkedList;

import util.Logger;
import util.Vector2;

public class GameObject {

	private String name;
	protected Vector2 position, dimensions;
	// TODO add width and height?
	private boolean destroyed;

	private LinkedList<Component> components;
	protected Scene scene;

	public GameObject(String name, int x, int y, int width, int height) {
		this.name = name;
		position = new Vector2(x, y);
		dimensions = new Vector2(width, height);
		components = new LinkedList<Component>();
	}

	// Game Methods
	protected void init() {
	}

	void start() {
		for (Component c : components) {
			c.scene = scene;
			c.start();
		}
	}

	void update() {
		for (Component c : components)
			c.update();
	}

	// Component Methods

	public <T extends Component> T getComponent(Class<T> componentClass) {
		try {
			for (Component c : components)
				if (componentClass.isAssignableFrom(c.getClass()))
					return componentClass.cast(c);
		} catch (ClassCastException e) { // This shouldn't happen!
			Logger.log("Object: Error accessing %s component", componentClass.getName());
			Logger.log(e.getStackTrace());
		}

		return null;
	}

	public void addComponent(Component c) {
		if (null != getComponent(c.getClass()))
			// maybe make annotation (multiple scripts should suprress warning)
			Logger.log("GameObject: Adding multiple components of the same type is not recommended");

		c.parent = this;
		c.scene = scene;
		components.add(c);
	}

	public <T extends Component> void removeComponent(Class<T> componentClass) {
		for (int i = 0; i < components.size(); i++) { // Use indexing loop to avoid concurrent errors
			Component c = components.get(i);
			if (componentClass.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return;
			}
		}
	}

	public LinkedList<Component> getComponents() {
		return components; // clone?
	}

	// Getters and Setters

	public int getWidth() {
		return (int) dimensions.x;
	}

	public int getHeight() {
		return (int) dimensions.y;
	}

	public String getName() {
		return name;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void destroy() {
		destroyed = true;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void move(Vector2 displacement) {
		position = position.add(displacement);
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
