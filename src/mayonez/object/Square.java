package mayonez.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import lib.math.MathUtil;
import lib.math.Vector;
import mayonez.Launcher;

public class Square extends Object implements Collidable, Mobile {

	private Color color;
	private Vector velocity;
	private int side;

	private double friction = 0.1;
	private double density = 0.2;
	// move to a Physics and Material

	public Square() {
		super(Math.random() * 1080, Math.random() * 720);
		color = new Color(MathUtil.random(16777215));
		side = MathUtil.random(32, 64);
		velocity = new Vector();
	}

	public double speed() {
		return velocity.magnitude();
	}

	public double mass() {
		return density * (side * side);
	}

	// Game methods

	@Override
	public void update() {

		if (x > Launcher.WIDTH)
			x = 0;
		else if (x + side < 0)
			x = Launcher.WIDTH;
		if (y > Launcher.HEIGHT)
			y = -side;
		else if (y + side < 0)
			y = Launcher.HEIGHT - side;

		x += velocity.getX();
		y += velocity.getY();

	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((int) x, (int) y, side, side);
	}

	// Interface methods

	@Override
	public void applyForce(Vector f) {
		Vector forceFriction = velocity.scale(-friction);
		Vector forceNet = f.add(forceFriction);
		Vector acceleration = forceNet.scale(1 / mass());
		velocity = velocity.add(acceleration);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, side, side);
	}

}
