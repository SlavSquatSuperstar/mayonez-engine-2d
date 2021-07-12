package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;
import slavsquatsuperstar.mayonez.physics2d.primitives.Collider2D;
import slavsquatsuperstar.util.MathUtils;

/**
 * A physical object with mass that responds to forces and collisions.
 *
 * @author SlavSquatSuperstar
 */
public class Rigidbody2D extends Component {

    // Physics Update Fields
    /**
     * A reference to the parent object's {@link Transform}.
     */
    Transform transform;
    private Collider2D collider;
    private Vector2 netForce = new Vector2();
    private Vector2 velocity = new Vector2();

    // Physics Properties
    private float mass;
    private float drag = 0.1f; // Modeled using F_d = -b*v
    private boolean followGravity = true;

    public Rigidbody2D(float mass) {
        this.setMass(mass);
    }

    public Rigidbody2D(float mass, boolean followsGravity) {
        setMass(mass);
        this.setFollowGravity(followsGravity);
    }

    @Override
    public void start() {
        collider = parent.getComponent(Collider2D.class);
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

    // Component Getters and Setters

    @Override
    public Rigidbody2D setParent(GameObject parent) {
        super.setParent(parent);
        transform = parent.transform;
        return this;
    }

    // For unit testing mainly
    public Rigidbody2D setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    /**
     * Returns the parent object's {@link Collider2D}. May be null.
     *
     * @return the attached collider
     */
    public Collider2D getCollider() {
        return collider;
    }

    // Property Getter and Setters

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
        return MathUtils.equals(mass, 0f);
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

    public boolean followGravity() {
        return followGravity;
    }

    public Rigidbody2D setFollowGravity(boolean followGravity) {
        this.followGravity = followGravity;
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
