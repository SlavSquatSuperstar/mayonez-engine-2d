package slavsquatstudio.mayonez.engine.object;

import java.awt.Graphics;

public abstract class GameObject {

	protected double x, y;

	public GameObject(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public abstract void update();

	public abstract void render(Graphics g);

}
