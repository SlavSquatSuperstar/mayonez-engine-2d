package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import io.github.slavsquatsuperstar.mathlib.MathUtil;
import io.github.slavsquatsuperstar.mathlib.Vector;
import io.github.slavsquatsuperstar.mayonez.Logger;
import io.github.slavsquatsuperstar.mayonez.engine.GameController;
import io.github.slavsquatsuperstar.mayonez.input.Input;
import io.github.slavsquatsuperstar.mayonez.objects.GameObject;

public class Player extends GameObject {

	private int length;
	private Color color;

	// Physics
	private Vector velocity;
	private double thrustForce = 8;
	private double breakForce = 2;
	private double topSpeed = 8;
	private double mass = 1;
	private double drag = 0.2; // [0, 1]

	private double boostMultiplier = 3;
	private boolean boosting, recovering;

	public Player(double x, double y, int length) {
		super(x, y);
		this.length = length;
		velocity = new Vector();
		drag = MathUtil.clamp(drag, 0, 1);
		color = Color.BLACK;
		Logger.log("Created Player");
	}

	public int getLength() {
		return length;
	}

	@Override
	public void update() {

		// Get Input
		int xInput = Input.getAxis("horizontal");
		int yInput = Input.getAxis("vertical");
		Vector appliedForce = new Vector(xInput, yInput).scale(thrustForce);
		// Boost (increase thrust and top speed)
		if (Input.keyDown(KeyEvent.VK_SHIFT)) {
			appliedForce.scale(boostMultiplier);
			boosting = true;
		} else {
			boosting = false;
			recovering = true;
		}
		applyForce(appliedForce);

		// Apply Drag Unless Stationary -> divide by 0
		if (velocity.magnitude() != 0) {
			Vector dragForce = velocity.scale(-drag);
			// Break (increase drag)
			if (Input.keyDown(KeyEvent.VK_SPACE))
				dragForce = dragForce.scale((drag + breakForce) / drag);
			applyForce(dragForce);

			// Just stop if moving really slow and not pressing move keys
			if (xInput == 0 && Math.abs(velocity.getX()) < drag)
				velocity.setX(0);
			if (yInput == 0 && Math.abs(velocity.getY()) < drag)
				velocity.setY(0);
		}

		// Limit Top Speed
		if (boosting) {
			if (speed() > topSpeed * boostMultiplier)
				velocity = velocity.scale(topSpeed / speed());
		} else if (recovering) {
			if (speed() > topSpeed * boostMultiplier)
				velocity = velocity.scale(topSpeed / speed());
			if (speed() < topSpeed)
				recovering = false;
		} else {
			if (speed() > topSpeed)
				velocity = velocity.scale(topSpeed / speed());
		}

		move(velocity);

		// Bounds Detection X
		if (getX() + length < 0)
			setX(GameController.width() - length);
		else if (getX() > GameController.width())
			setX(0);

		// Bounds Detection Y
		if (getY() + length < 0)
			setY(GameController.height() - length);
		else if (getY() > GameController.height())
			setY(-length);

	}

	// ForceMode.Force, should be used with keyboard axes
	private void applyForce(Vector force) {
		// dv = F*t/m
		velocity = velocity.add(force.scale(GameController.deltaTime()).scale(1 / mass));
	}

	// ForceMode.Acceleration, should be used with keyboard axes
	private void applyAcceleration(Vector acceleration) {
		// dv = a*t
		velocity = velocity.add(acceleration.scale(GameController.deltaTime()));
	}

	// ForceMode.Impulse, should be used with button presses
	private void applyImpulse(Vector impulse) {
		// dv = dp/m
		velocity = velocity.add(impulse.scale(1 / mass));
	}

	// ForceMode.VelocityChange, should be used with button presses
	private void applyVelocityChange(Vector velocityChange) {
		// dv
		velocity = velocity.add(velocityChange).scale(GameController.deltaTime());
	}

	private void applyDisplacement(Vector displacement) {
		velocity = displacement;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((int) getX(), (int) getY(), length, length);
	}

	public Vector velocity() {
		return velocity;
	}

	public double speed() {
		return velocity.magnitude();
	}

}
