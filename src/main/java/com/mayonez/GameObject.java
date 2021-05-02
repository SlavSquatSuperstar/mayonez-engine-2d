package com.mayonez;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.mayonez.components.Component;
import com.util.Logger;

public class GameObject {

	private String name;
	public Transform transform;
	private boolean destroyed;

	private ArrayList<Component> components;
	protected Scene scene;

	public GameObject(String name, Transform transform) {
		this.name = name;
		this.transform = transform;
		components = new ArrayList<Component>();
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

	void update(double dt) {
		for (Component c : components)
			c.update(dt);
	}

	void render(Graphics2D g2) {
		for (Component c : components)
			c.render(g2);
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

	public ArrayList<Component> getComponents() {
		return components; // clone?
	}

	// Getters and Setters

	public String getName() {
		return name;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void destroy() {
		destroyed = true;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
