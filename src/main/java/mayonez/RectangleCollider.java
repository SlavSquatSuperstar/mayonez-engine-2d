package mayonez;

import java.awt.Rectangle;

import util.Vector2;

public class RectangleCollider extends Component {

	private double x, y;
	private int width, height;
	private double bounceModifier = 0.4;

	public RectangleCollider(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void update() {
		x = parent.getPosition().x;
		y = parent.getPosition().y;

		// Bounds detection
		// TODO: When hitting walls, set velocity to 0
		// TODO: Make collision detector, move to physics
		if (scene.isBounded()) {

			RigidBody rb = parent.getComponent(RigidBody.class);
			Vector2 velocity = (null == rb) ? null : rb.velocity();

			if (x < 0) {
				x = 0;
				if (null != velocity)
					velocity.x = -bounceModifier * velocity.x;
			} else if (x > scene.getWidth() - width) {
				x = scene.getWidth() - width;
				if (null != velocity)
					velocity.x = -bounceModifier * velocity.x;
			}

			if (y < 0) {
				y = 0;
				if (null != velocity)
					velocity.y = -bounceModifier * velocity.y;
			} else if (y > scene.getHeight() - height) {
				y = scene.getHeight() - height;
				if (null != velocity)
					velocity.y = -bounceModifier * velocity.y;
			}

			parent.setPosition(new Vector2(x, y));
		}

	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}

}
