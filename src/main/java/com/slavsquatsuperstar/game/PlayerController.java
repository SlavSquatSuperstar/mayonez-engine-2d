package com.slavsquatsuperstar.game;

import java.awt.Color;
import java.awt.Rectangle;

import com.slavsquatsuperstar.mayonez.Game;
import com.slavsquatsuperstar.mayonez.components.BoxCollider;
import com.slavsquatsuperstar.mayonez.components.Component;
import com.slavsquatsuperstar.mayonez.components.Sprite;
import com.slavsquatsuperstar.util.Logger;
import com.slavsquatsuperstar.util.Vector2;

public class PlayerController extends Component {

	// Rendering Fields
	public Sprite[] layers;

	// Movement Fields
//	private RigidBody rb;

	private float topSpeed = 8;
//	private float thrustForce = 10;
//	private float brakeForce = 2;
	private float mass = 1;
//	private float drag = 0.3; // [0, 1]
//	private float bounceModifier = 0;

	public PlayerController(Sprite layer1, Sprite layer2, Sprite layer3, Color color1, Color color2) {
		int threshold = 200;
		this.layers = new Sprite[] { layer1, layer2, layer3 };
		Color[] colors = { color1, color2 };

		// Create sprite layers
		for (int i = 0; i < colors.length; i++) {
			Sprite l = layers[i];
			for (int y = 0; y < l.getImage().getWidth(); y++) {
				for (int x = 0; x < l.getImage().getHeight(); x++) {
					Color color = new Color(l.getImage().getRGB(x, y));
					if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold)
						l.getImage().setRGB(x, y, colors[i].getRGB());
				}
			}
		}
	}

	@Override
	public void start() {
		scene.getCamera().setSubject(parent.getComponent(Sprite.class)); // TODO pass camera in player c'tor?
//		rb = parent.getComponent(RigidBody.class);
//		rb.mass = mass;
	}

	@Override
	public void update(float dt) {
//		Logger.log("Player: %.2f, %.2f", parent.getX(), parent.getY());

		float xInput = Game.keyboard().getAxis("horizontal") * topSpeed;
		float yInput = Game.keyboard().getAxis("vertical") * topSpeed;

		float speed = (float) Math.sqrt((xInput * xInput) + (yInput * yInput));
		if (speed > topSpeed) {
			xInput = xInput / speed * topSpeed;
			yInput = yInput / speed * topSpeed;
		}
		Vector2 input = new Vector2(xInput, yInput);
		parent.transform.move(input);

		// Don't want to move faster diagonally
//		int xInput = Game.keyboard().getAxis("horizontal");
//		int yInput = Game.keyboard().getAxis("vertical");
//		Vector2 input = new Vector2(xInput, yInput).unitVector();
//		parent.transform.move(input.multiply(topSpeed));

		// Unit vector function is really messing with the input vector values

//		Logger.log(xInput + ", " + yInput);
//		Logger.log(input);
//		

//		Vector2 appliedForce = new Vector2(xInput, yInput).unitVector().scale(thrustForce);
//
//		Vector2 velocity = rb.velocity();
//		rb.addForce(appliedForce);
//
//		// Apply Drag Unless Stationary (prevent divide by 0)
//		if (velocity.magnitude() != 0) {
//			Vector2 dragForce = velocity.scale(-drag);
//			// Break (increase drag)
//			if (Game.keyboard().keyDown(KeyEvent.VK_SPACE))
//				dragForce = dragForce.scale((drag + brakeForce) / drag);
//			rb.addForce(dragForce);
//
//			// Just stop if moving really slow and not pressing move keys
//			if (xInput == 0 && Math.abs(velocity.x) < drag)
//				velocity.x = 0;
//			if (yInput == 0 && Math.abs(velocity.y) < drag)
//				velocity.y = 0;
//
//			// TODO: When recovering, prevent acceleration in the direction of motion
//		}
//
//		// Limit Top Speed
//		if (rb.speed() > topSpeed)
//			velocity = velocity.scale(topSpeed / rb.speed());
//
//		parent.transform.move(velocity);

		// Bounds detection
		// TODO: When hitting walls, set velocity to 0
		// TODO: Make collision detector, move to physics

//		RigidBody rb = parent.getComponent(RigidBody.class);
//		Vector2 velocity = (rb == null) ? Vector2.ZERO : rb.velocity();
		Rectangle hitbox = parent.getComponent(BoxCollider.class).getBounds();

		if (parent.getX() < 0) {
			parent.setX(0);
//			velocity.x = -bounceModifier * velocity.x;
		} else if (parent.getX() > scene.getWidth() - hitbox.width) {
			System.out.println("trigger x" + parent.getX());
			parent.setX(scene.getWidth() - hitbox.width);
//			velocity.x = -bounceModifier * velocity.x;
		}

		if (parent.getY() < 0) {
			parent.setY(0);
//			velocity.y = -bounceModifier * velocity.y;
		} else if (parent.getY() > scene.getHeight() - hitbox.height) {
			System.out.println("trigger y " + parent.getY());
			parent.setY(scene.getHeight() - hitbox.height);
//			velocity.y = -bounceModifier * velocity.y;
		}

//		Logger.log("Player: %.2f, %.2f", parent.getX(), parent.getY());

	}

}
