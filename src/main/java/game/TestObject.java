package game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import mayonez.GameObject;
import mayonez.KeyInput;
import mayonez.RectangleCollider;
import mayonez.RectangleMesh;
import mayonez.RigidBody;
import util.Vector;

public class TestObject extends GameObject {

	private RigidBody rb;

	private double thrustForce = 12;
	private double breakForce = 2;
	private double topSpeed = 6;
	private double mass = 3;
	private double drag = 0.2; // [0, 1]
	private int length = 32;

	private double boostMultiplier = 3;
	private MoveState state = MoveState.MOVING;

	public TestObject(String name) {
		super(name, 100, 100);
		addComponent(new RectangleMesh(this, length, length, Color.BLUE));
		addComponent(new RectangleCollider(this, length, length));
		addComponent(rb = new RigidBody(this, mass));
	}

	@Override
	public void update() {

		Vector velocity = rb.velocity();

		int xInput = KeyInput.getAxis("horizontal");
		int yInput = KeyInput.getAxis("vertical");
		// Don't want to move faster diagonally
		Vector appliedForce = new Vector(xInput, yInput).unitVector().scale(thrustForce);

		// Hold space to increase acceleration and top speed
		if (KeyInput.keyDown(KeyEvent.VK_SHIFT)) {
			appliedForce.scale(boostMultiplier);
			state = MoveState.BOOSTING;
		} else {
			if (state == MoveState.BOOSTING) {
				state = MoveState.RECOVERING;
			}
		}
		rb.addForce(appliedForce);

		// Apply Drag Unless Stationary (prevent divide by 0)
		if (velocity.magnitude() != 0) {
			Vector dragForce = velocity.scale(-drag);
			// Break (increase drag)
			if (KeyInput.keyDown(KeyEvent.VK_SPACE))
				dragForce = dragForce.scale((drag + breakForce) / drag);
			rb.addForce(dragForce);

			// Just stop if moving really slow and not pressing move keys
			if (xInput == 0 && Math.abs(velocity.x) < drag)
				velocity.x = 0;
			if (yInput == 0 && Math.abs(velocity.y) < drag)
				velocity.y = 0;

			// TODO: When recovering, prevent acceleration in the direction of motion
		}

		// Limit Top Speed
		switch (state) {
		case BOOSTING:
			if (rb.speed() > topSpeed * boostMultiplier)
				velocity = velocity.scale(topSpeed / rb.speed());
		case RECOVERING:
			if (rb.speed() > topSpeed * boostMultiplier)
				velocity = velocity.scale(topSpeed / rb.speed());
			else if (rb.speed() <= topSpeed)
				state = MoveState.RECOVERING;
		case MOVING:
			if (rb.speed() > topSpeed)
				velocity = velocity.scale(topSpeed / rb.speed());
		}

		move(velocity);
		
	}

	private enum MoveState {
		MOVING, BOOSTING, RECOVERING;
	}

}
