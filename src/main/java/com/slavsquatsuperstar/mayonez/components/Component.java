package com.slavsquatsuperstar.mayonez.components;

import java.awt.Graphics2D;

import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Scene;

/**
 * A data structure representing traits and behaviors of a {@link GameObject}.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Component {

	/**
	 * The {@link GameObject} this component belongs to.
	 */
	public GameObject parent;

	/**
	 * Initialize any fields needed for subclasses or scripts.
	 */
	public void start() {}

	/**
	 * Refresh this component in the world.
	 */
	public void update(float dt) {}

	/**
	 * Draw this component on the screen.
	 */
	public void render(Graphics2D g2) {}

	/**
	 * @return The {@link Scene} the parent object belongs to.
	 */
	public Scene scene() {
		return parent.getScene();
	}

	public boolean isInScene(Scene scene) {
		// This could cause a NPE
		return scene.equals(scene());
	}

}
