package com.slavsquatsuperstar.mayonez;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.slavsquatsuperstar.mayonez.components.Component;
import com.slavsquatsuperstar.util.Logger;
import com.slavsquatsuperstar.util.Vector2;

/**
 * A collection of {@link Component}s representing an in-game object.
 * 
 * @author SlavSquatSuperstar
 *
 */
public class GameObject {

	private String name;
	public Transform transform;
	private boolean destroyed;

	private ArrayList<Component> components;
	protected Scene scene;

	public GameObject(String name, Vector2 position) {
		this.name = name;
		this.transform = new Transform(position);
		components = new ArrayList<>();
		init();
	}

	// Game Methods

	/**
	 * Add necessary components.
	 */
	protected void init() {
	}

	void start() {
		for (Component c : components) {
			c.scene = scene;
			c.start();
		}
	}

	void update(float dt) {
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

	public void addComponent(Component comp) {
//		if (null != getComponent(comp.getClass()))
//			;
		// maybe make annotation (multiple scripts should suprress warning)
//			Logger.log("GameObject: Adding multiple components of the same type is not recommended");

		comp.parent = this;
		comp.scene = scene;
		components.add(comp);
	}

	public <T extends Component> void removeComponent(Class<T> cls) {
		for (int i = 0; i < components.size(); i++) { // Use indexing loop to avoid concurrent errors
			Component c = components.get(i);
//			if (cls.isAssignableFrom(c.getClass())) {
			if (cls.isInstance(c)) {
				components.remove(i);
				return;
			}
		}
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	// Getters and Setters
	
	public float getX() {
		return transform.position.x;
	}
	
	public void setX(float x) {
		transform.position.x = x;
	}
	
	public float getY() {
		return transform.position.y;
	}
	
	public void setY(float y) {
		transform.position.y = y;
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

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
