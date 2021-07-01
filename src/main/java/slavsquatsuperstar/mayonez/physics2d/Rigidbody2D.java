package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;

public class Rigidbody2D extends Component {

    public boolean followsGravity = true;
    public float mass;
    public float drag = 1f; // Modeled using F_d = -b*v
    /**
     * A reference to the parent object's {@link Transform}.
     */
    private Transform transform;
    private Vector2 netForce = new Vector2();
    private Vector2 velocity = new Vector2();

    public Rigidbody2D(float mass) {
        this.mass = mass;
    }

    @Override
    public void start() {
        transform = parent.transform;
    }

    public void physicsUpdate(float dt) {
        if (mass == 0) // Zero mass means static
            return;

//        // TODO account for direction of gravity
//        if (Math.abs(velocity.y) > Preferences.TERMINAL_VELOCITY)
//            velocity.y = Math.signum(velocity.y) * Preferences.TERMINAL_VELOCITY;

        velocity = velocity.add(netForce.div(mass).mul(dt)); // dv = F/m*dt
        transform.move(velocity); // ds = v*t
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
        return velocity.length();
    }

    // Getters and Setters

    public Vector2 getPosition() {
        return transform.position;
    }

    public void setPosition(Vector2 position) {
        transform.position = position;
    }

    public float getRotation() {
        return transform.rotation;
    }

    public void setRotation(float rotation) {
        transform.rotation = rotation;
    }

    // For unit testing mainly
    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    //    public enum BodyType {
//        /**
//         * Cannot be moved by scripts or physics.
//         */
//        STATIC,
//        /**
//         * Object transform can be modified through scripts.
//         */
//        KINEMATIC,
//        /**
//         * Can be moved by physics engine and scripts.
//         */
//        DYNAMIC
//    }

}
