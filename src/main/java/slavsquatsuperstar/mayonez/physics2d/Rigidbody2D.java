package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;
import slavsquatsuperstar.util.MathUtils;

/**
 * A physical object with mass that interacts with the world.
 *
 * @author SlavSquatSuperstar
 */
public class Rigidbody2D extends Component {

    // Physics Properties
    public boolean followsGravity = true;
    /**
     * A reference to the parent object's {@link Transform}.
     */
    Transform transform;
    // Physics Update Fields
    private Vector2 netForce = new Vector2();
    private Vector2 velocity = new Vector2();
    private float mass;
    private float drag = 0.1f; // Modeled using F_d = -b*v
    private float bounce = 0.5f;

    public Rigidbody2D(float mass) {
        this.setMass(mass);
    }

    public Rigidbody2D(float mass, boolean followsGravity) {
        setMass(mass);
        this.followsGravity = followsGravity;
    }

    public void physicsUpdate(float dt) {
        if (hasInfiniteMass())
            return;

//        // TODO account for direction of gravity
//        if (Math.abs(velocity.y) > Preferences.TERMINAL_VELOCITY)
//            velocity.y = Math.signum(velocity.y) * Preferences.TERMINAL_VELOCITY;

        velocity = velocity.add(netForce.div(getMass()).mul(dt)); // dv = F/m*dt
        // TODO use average velocity?
        transform.move(velocity); // ds = v*t
        netForce.set(0, 0); // Reset accumulated forces
    }

    // Physics Methods

    /**
     * Applies a force to this rigidbody's center of mass.
     *
     * @param force a vector
     */
    public void addForce(Vector2 force) {
        netForce = netForce.add(force);

    }

    public void addAcceleration(Vector2 acceleration) {
        netForce = netForce.add(acceleration.mul(mass)); // dF = a/m
    }

    /**
     * Applies an impulse to the object's center of mass.
     *
     * @param impulse a vector
     */
    public void addImpulse(Vector2 impulse) {
        velocity = velocity.add(impulse.div(getMass())); // dv = J/m = m*dv/m
    }

    public void addVelocity(Vector2 velocityChange) {
        velocity = velocity.add(velocityChange);
    }

    // Object Properties

    public Vector2 velocity() {
        return velocity;
    }

    public float speed() {
        return velocity.length();
    }

    // Getters and Setters

    @Override
    public Rigidbody2D setParent(GameObject parent) {
        super.setParent(parent);
        transform = parent.transform;
        return this;
    }

    public float getMass() {
        return mass;
    }

    public Rigidbody2D setMass(float mass) {
        if (mass < 0)
            mass = 0;
        this.mass = mass;
        return this;
    }

    public boolean hasInfiniteMass() {
        return MathUtils.equals(getMass(), 0f);
    }

    public float getInverseMass() {
        return hasInfiniteMass() ? 0 : 1 / getMass();
    }

    public float getDrag() {
        return drag;
    }

    public Rigidbody2D setDrag(float drag) {
        this.drag = MathUtils.clamp(drag, 0, 1);
        return this;
    }

    public float getBounce() {
        return bounce;
    }

    public Rigidbody2D setBounce(float bounce) {
        this.bounce = MathUtils.clamp(bounce, 0f, 1f);
        return this;
    }

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
    public Rigidbody2D setTransform(Transform transform) {
        this.transform = transform;
        return this;
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
