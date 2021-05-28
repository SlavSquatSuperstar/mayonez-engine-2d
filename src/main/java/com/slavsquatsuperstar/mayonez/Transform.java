package com.slavsquatsuperstar.mayonez;

/**
 * Stores the position and scale of a GameObject and provides additional
 * methods.
 * 
 * @author SlavSquatSuperstar
 *
 */
public class Transform {

	public Vector2 position;
	public Vector2 scale = new Vector2(1, 1);
	public float rotation;

	public Transform(Vector2 position) {
		this.position = position;
	}

	public void move(Vector2 displacement) {
		position = position.add(displacement);
	}

	@Override
	public String toString() {
		return String.format("Position: %s, Scale: %s", position, scale);
	}

}
