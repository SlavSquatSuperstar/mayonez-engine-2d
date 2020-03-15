package mayonez.object;

import java.awt.Graphics;

public abstract class Object {

	protected double x, y;

	public Object(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public abstract void update();

	public abstract void render(Graphics g);

}
