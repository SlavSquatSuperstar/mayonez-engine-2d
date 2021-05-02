package com.game;

import java.awt.event.KeyEvent;

import com.mayonez.Game;
import com.mayonez.components.Component;
import com.mayonez.components.RigidBody;
import com.util.Vector2;

public class PlayerController extends Component {

	private RigidBody rb;

	private double thrustForce = 10;
	private double breakForce = 2;
	private double topSpeed = 6;
	private double mass = 2;
	private double drag = 0.4; // [0, 1]

	private double boostMultiplier = 3;
	private MoveState state = MoveState.MOVING;

	@Override
	public void start() {
		rb = parent.getComponent(RigidBody.class);
		rb.mass = mass;
	}

	@Override
	public void update(double dt) {
		Vector2 velocity = rb.velocity();

		int xInput = Game.keyboard().getAxis("horizontal");
		int yInput = Game.keyboard().getAxis("vertical");
		// Don't want to move faster diagonally
		Vector2 appliedForce = new Vector2(xInput, yInput).unitVector().scale(thrustForce);

		// Hold space to increase acceleration and top speed
		if (Game.keyboard().keyDown(KeyEvent.VK_SHIFT)) {
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
			Vector2 dragForce = velocity.scale(-drag);
			// Break (increase drag)
			if (Game.keyboard().keyDown(KeyEvent.VK_SPACE))
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

		parent.transform.move(velocity);
	}

	private enum MoveState {
		MOVING, BOOSTING, RECOVERING;
	}

}
