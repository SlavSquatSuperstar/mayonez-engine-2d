package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;

public class RigidBody2D extends Component {

    public boolean followsGravity = true;

    public float mass;
    private Vector2 netForce = new Vector2();
    private Vector2 velocity = new Vector2();

    public RigidBody2D(float mass) {
        this.mass = mass;
    }

    public void physicsUpdate(float dt) {
        if (mass == 0) // Zero mass means static
            return;

//        // TODO account for direction of gravity
//        if (Math.abs(velocity.y) > Preferences.TERMINAL_VELOCITY)
//            velocity.y = Math.signum(velocity.y) * Preferences.TERMINAL_VELOCITY;

        velocity = velocity.add(netForce.div(mass).mul(dt));
        parent.transform.move(velocity); // s = v*t

        netForce.set(0, 0); // Reset accumulated forces
    }

    // Physics Methods

    public void addForce(Vector2 force) {
        netForce = netForce.add(force);
        // dv = F/m*t
//        velocity = velocity.add(force.mul(Game.timestep).div(mass));
    }

    public void addImpulse(Vector2 impulse) {
        // dv = dp/m = m*dv/m
        velocity = velocity.add(impulse.div(mass));
    }

    // Object Properties

    public Vector2 velocity() {
        return velocity;
    }

    public float speed() {
        return velocity.magnitude();
    }

    // Getters and Setters

    public Vector2 getPosition() {
        return parent.transform.position;
    }

    public void setPosition(Vector2 position) {
        parent.transform.position = position;
    }

    public float getRotation() {
        return parent.transform.rotation;
    }

    public void setRotation(float rotation) {
        parent.transform.rotation = rotation;
    }

//    public enum BodyType {
}
