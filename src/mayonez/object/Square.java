package mayonez.object;

import java.awt.Color;
import java.awt.Graphics;

import lib.math.MathUtil;
import lib.math.Vector;
import mayonez.Launcher;

public class Square extends Object {

	private Color color;
	private Vector velocity, forceApplied;
	private int side;

	private double friction = 0.25;
	private double density = 0.5;

	public Square() {
		super(Math.random() * 1080, Math.random() * 720);
		color = new Color(MathUtil.random(16777215));
		side = MathUtil.random(48, 96);

		velocity = new Vector(MathUtil.random(-1.5, 1.5), MathUtil.random(-1.5, 1.5));
		forceApplied = new Vector(MathUtil.randomSign() * MathUtil.random(20.0, 30.0),
				MathUtil.randomSign() * MathUtil.random(20.0, 30.0));
	}

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

		Vector forceFriction = velocity.scale(-friction);
		Vector forceNet = forceApplied.add(forceFriction);
		Vector acceleration = forceNet.scale(1 / mass());

		velocity = velocity.add(acceleration);

		x += velocity.getX();
		y += velocity.getY();

	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((int) x, (int) y, side, side);
	}

	public double speed() {
		return velocity.magnitude();
	}

	public double mass() {
		return density * (side * side);
	}

}
