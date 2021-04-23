package mayonez;

import java.util.LinkedList;

import util.Logger;
import util.Vector;

public class GameObject {

	private String name;
	private Vector position;
	private boolean destroyed;

	private LinkedList<Component> components;
	protected Scene scene;

	public GameObject(String name, int x, int y) {
		this.name = name;
		position = new Vector(x, y);
		components = new LinkedList<Component>();
	}

	// Game Methods

	public void start() {
		Logger.log("%s: Created object \"%s\"", getClass().getSimpleName(), name);
		for (Component c : components)
			c.start();
	}

	public void update() {
	}

	// Getters and Setters

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public void move(Vector displacement) {
		position = position.add(displacement);
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
		components.add(c);
		c.parent = this;
		c.scene = scene;
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

}
