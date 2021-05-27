package com.slavsquatsuperstar.mayonez.components;

import java.awt.Rectangle;

public class BoxCollider extends Component {

	private float x, y;
	private int width, height;

	public BoxCollider(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void update(float dt) {
		x = parent.transform.position.x;
		y = parent.transform.position.y;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

}
