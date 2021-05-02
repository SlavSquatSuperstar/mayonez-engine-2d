package com.mayonez.components;

import java.awt.Rectangle;

import com.util.Vector2;

public class RectangleCollider extends Component {

	private double x, y;
	private int width, height;
	private double bounceModifier = 0;

	public RectangleCollider(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void update(double dt) {
		x = parent.transform.position.x;
		y = parent.transform.position.y;

		// Bounds detection
		// TODO: When hitting walls, set velocity to 0
		// TODO: Make collision detector, move to physics
		if (scene.isBounded()) {
			RigidBody rb = parent.getComponent(RigidBody.class);
			Vector2 velocity = (null == rb) ? Vector2.ZERO : rb.velocity();

			if (x < 0) {
				x = 0;
				velocity.x = -bounceModifier * velocity.x;
			} else if (x > scene.getWidth() - width) {
				x = scene.getWidth() - width;
				velocity.x = -bounceModifier * velocity.x;
			}

			if (y < 0) {
				y = 0;
				velocity.y = -bounceModifier * velocity.y;
			} else if (y > scene.getHeight() - height) {
				y = scene.getHeight() - height;
				velocity.y = -bounceModifier * velocity.y;
			}

			parent.transform.position = new Vector2(x, y);
		}

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

}
