package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;

// Axis Aligned Bounding Box
public class AlignedBoxCollider2D extends Collider2D {

	private Vector2 size;

	public AlignedBoxCollider2D(Vector2 size) {
		this.size = size;
	}

//	public AlignedBox2D(Vector2 min, Vector2 max) { // top left, bottom right
//		size = max.subtract(min);
//	}

	public Vector2 getMin() {
		return rb.getPosition();//.subtract(size.divide(2f)); // Assume getPosition() is center
	}

	public Vector2 getMax() {
		return rb.getPosition().add(size);//.divide(2f));
	}
	
	public Vector2 getSize() {
		return size;
	}

	@Override
	public boolean contains(Vector2 point) {
		return point.x >= getMin().x && point.x <= getMax().x
				&& point.y >= getMin().y && point.y <= getMax().y;
	}

}
