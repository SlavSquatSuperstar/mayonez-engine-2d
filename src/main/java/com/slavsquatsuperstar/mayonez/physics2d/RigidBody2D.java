package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Game;
import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.components.Component;
import com.slavsquatsuperstar.util.Preferences;

public class RigidBody2D extends Component {

    private Vector2 velocity;
    public float mass, rotation;
    public boolean followsGravity;

    public RigidBody2D(float mass, boolean followsGravity) {
        this.mass = mass;
        this.followsGravity = followsGravity;
        this.velocity = new Vector2();
    }

    @Override
    public void update(float dt) {
        // TODO account for direction of gravity
        if (Math.abs(velocity.y) > Preferences.TERMINAL_VELOCITY)
            velocity.y = Math.signum(velocity.y) * Preferences.TERMINAL_VELOCITY;

        if (followsGravity)
            addAcceleration(Preferences.GRAVITY);

        parent.transform.move(velocity); // s = v*t
    }

    // Physics Methods

    // ForceMode.Force, should be used with keyboard axes
    public void addForce(Vector2 force) {
        // dv = F*t/m
        velocity = velocity.add(force.mul(Game.timestep).div(mass));
    }

    // ForceMode.Acceleration, should be used with keyboard axes
    public void addAcceleration(Vector2 acceleration) {
        // dv = a*t
        velocity = velocity.add(acceleration.mul(Game.timestep));
    }

    // ForceMode.Impulse, should be used with button presses
    public void addImpulse(Vector2 impulse) {
        // dv = dp/m
        velocity = velocity.add(impulse.div(mass));
    }

//	// ForceMode.VelocityChange, should be used with button presses
//	public void addVelocityChange(Vector2 velocityChange) {
//		// dv
//		velocity = velocity.add(velocityChange).multiply(Game.timestep);
//	}

//	public void addDisplacement(Vector2 displacement) {
//		velocity = displacement;
//	}

    // Object Properties

    public Vector2 velocity() {
        return velocity;
    }

    public float speed() {
        return velocity.magnitude();
    }

    public float mass() {
        return mass;
    }

    // TODO Top left vs center positioning
    public Vector2 position() {
        return parent.transform.position;
    }

}
