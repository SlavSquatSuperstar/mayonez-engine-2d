package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.util.Vector2;

/**
 * Stores the position and scale of a GameObject and provides additional
 * methods.
 * 
 * @author SlavSquatSuperstar
 *
 */
public class Transform {

	public Vector2 position, scale;
	public float rotation;

	public Transform(Vector2 position) {
		this.position = position;
		scale = Vector2.ONE;
		rotation = 0f;
	}

	public void move(Vector2 displacement) {
		position = position.add(displacement);
	}

	@Override
	public String toString() {
		return String.format("Position: %s, Scale: %s", position, scale);
	}

}
