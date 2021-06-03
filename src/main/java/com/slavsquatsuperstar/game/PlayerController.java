package com.slavsquatsuperstar.game;

import com.slavsquatsuperstar.mayonez.Game;
import com.slavsquatsuperstar.mayonez.GameObject;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.components.Script;
import com.slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import com.slavsquatsuperstar.mayonez.physics2d.RigidBody2D;
import com.slavsquatsuperstar.util.MathUtil;

@SuppressWarnings("unused")
public class PlayerController extends Script {

    // Movement Fields
    private RigidBody2D rb;
    private AlignedBoxCollider2D box;
    public GameObject ground;

    private float topSpeed = 10f;
    private float thrustForce = 150f;
    private float brakeForce = 8f;
    private float mass = 8f;
    private float drag = 0.5f; // [0, 1]

    public PlayerController(GameObject ground) {
        this.ground = ground;
    }

    @Override
    public void start() {
//        getScene().getCamera().setSubject(parent.getComponent(Sprite.class)); // TODO pass camera in player c'tor?
        box = parent.getComponent(AlignedBoxCollider2D.class);
        rb = parent.getComponent(RigidBody2D.class);
        rb.mass = mass;
    }

    @Override
    public void update(float dt) {

        // Detect player input
        int xInput = Game.keyboard().getAxis("horizontal");
        int yInput = 0; //Game.keyboard().getAxis("vertical");

        // Don't want to move faster diagonally so normalize
        Vector2 input = new Vector2(xInput, yInput).unit().mul(thrustForce);
        rb.addForce(input);

        Vector2 velocity = rb.velocity();
        velocity.x = MathUtil.clamp(velocity.x, -topSpeed, topSpeed);

//         Apply  Drag Unless Stationary (prevent divide by 0)
		if (velocity.magnitude() != 0) {
			Vector2 dragForce = velocity.div(-drag);
//			dragForce.y = 0; // horizontal only
			// Increase drag by braking
			if (Game.keyboard().keyDown("space"))
				dragForce = dragForce.mul((drag + brakeForce) / drag);
			rb.addForce(dragForce);

			// Just stop if moving really slow and not pressing move keys
			if (xInput == 0 && Math.abs(velocity.x) < drag)
				velocity.x = 0;
			if (yInput == 0 && Math.abs(velocity.y) < drag)
				velocity.y = 0;
		}

        // Collide with walls
        if (box.min().x < 0 || box.max().x > scene().getWidth())
            rb.velocity().x = 0;
        // Collide with ground
        if (box.max().y > ground.getY()) { // TODO detect & resolve collisions elsewhere
            parent.setY(ground.getY() - box.size().y);
            rb.velocity().y = 0;
            // Jump if on ground
            if (Game.keyboard().keyDown("up"))
                // Impulse must be big enough to not get stuck on ground next frame
                rb.addImpulse(new Vector2(0, -thrustForce));
        } else {
            // Ground Pound if in air
            if (Game.keyboard().keyDown("down"))
                rb.addImpulse(new Vector2(0, thrustForce / 5));
        }

        // Bounds detection
        // TODO: Make collision detector, move to physics
        // TODO make KeepInScene script (but detach Collider from RB)
        if (scene().isBounded()) {
            parent.setX(MathUtil.clamp(box.min().x, 0, scene().getWidth() - box.size().x));
            parent.setY(MathUtil.clamp(box.min().y, 0, scene().getHeight() - box.size().y));
        }

        // Limit Top Speed
//		if (rb.speed() > topSpeed)
//			velocity = velocity.multiply(topSpeed / rb.speed());

//		Logger.log("Player: %.2f, %.2f", parent.getX(), parent.getY());

    }

}
