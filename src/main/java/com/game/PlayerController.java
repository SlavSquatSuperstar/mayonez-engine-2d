package com.game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.mayonez.Game;
import com.mayonez.components.Component;
import com.mayonez.components.RigidBody;
import com.mayonez.components.Sprite;
import com.util.Vector2;

public class PlayerController extends Component {

	private RigidBody rb;

	public Sprite[] layers;

	private double thrustForce = 10;
	private double breakForce = 2;
	private double topSpeed = 6;
	private double mass = 2;
	private double drag = 0.4; // [0, 1]

	public PlayerController(Sprite layer1, Sprite layer2, Sprite layer3, Color color1, Color color2) {
		int threshold = 200;
		this.layers = new Sprite[] { layer1, layer2, layer3 };
		Color[] colors = { color1, color2 };

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
		if (rb.speed() > topSpeed)
			velocity = velocity.scale(topSpeed / rb.speed());

		parent.transform.move(velocity);
	}

}
