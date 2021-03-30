package io.github.slavsquatsuperstar.mayonez.objects;

import java.awt.Graphics;

import io.github.slavsquatsuperstar.mathlib.Vector;
import io.github.slavsquatsuperstar.mayonez.Logger;

public abstract class GameObject {

	// name?
	public Vector position;
	private boolean destroyed;
	// detect(collider)
	// texture
	// get component(Class)

	public GameObject() {
		this(0, 0);
	}

	public GameObject(double x, double y) {
		position = new Vector(x, y);
	}

	// Engine methods
	public void update() {
	}

	public void render(Graphics g) {
	}

	// Position methods
	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	public void move(Vector displacement) {
		position = position.add(displacement);
	}
	
	public void setX(double x) {
		position.setX(x);
	}
	
	public void setY(double y) {
		position.setY(y);
	}

	public void setPos(Vector position) {
		this.position = position;
	}

	// Destroy methods
	public boolean isDestroyed() {
		return destroyed;
	}

	public void destroy() {
		destroyed = true;
		Logger.log("Destroyed Object");
	}

}
