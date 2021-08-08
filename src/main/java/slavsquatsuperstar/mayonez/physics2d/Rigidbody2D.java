package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.colliders.AlignedBoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.BoxCollider2D;
import slavsquatsuperstar.mayonez.physics2d.colliders.CircleCollider;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

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

    // Linear Movement
    private Vec2 netForce = new Vec2();
    private Vec2 velocity = new Vec2();
    private float drag = 0.2f; // Modeled using F_d = -b*v

    // Angular Movement
    private float netTorque = 0f;
    private float angularVelocity = 0f;
    private float angularDrag = 0.5f; // Modeled using F_d = -b*W
    private boolean fixedRotation = false;
    // TODO moment of inertia based on collider

    // Physics Properties
    private float mass;
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
        if (collider instanceof AlignedBoxCollider2D)
            fixedRotation = true;
    }

    public void physicsUpdate(float dt) {
        if (!hasInfiniteMass()) {
            // Integrate velocity
            velocity = velocity.add(netForce.mul(getInverseMass() * dt)); // dv = F/m*dt
            // Apply drag unless stationary
            if (!MathUtils.equals(velocity.lenSquared(), 0))
                addForce(velocity.mul(-drag));
            // Integrate position
            transform.move(velocity.mul(dt)); // ds = v*dt

            // Do the same for angular motion
            if (!fixedRotation) {
                angularVelocity += (netTorque * getInverseAngularMass() * dt); // dw = T/I*dt
                if (!MathUtils.equals(angularVelocity, 0))
                    addTorque(angularVelocity * -angularDrag);
                transform.rotate(MathUtils.toDegrees(angularVelocity) * dt); // dTheta = w*dt, convert radians to degrees
            }
        }

        // Reset accumulated forces/torques
        netForce.set(0, 0);
        netTorque = 0;
    }

    // Linear Movement Methods

    /**
     * Applies a force to this body's center of mass.
     *
     * @param force a vector with the units <code>kg•m/s/s</code>
     */
    public void addForce(Vec2 force) {
        netForce = netForce.add(force);
    }

    public void addForceAtPoint(Vec2 force, Vec2 position) {
        addForce(force);
        addTorque(position.sub(transform.position).cross(force));
    }

    /**
     * Accelerates this body in the given direction.
     *
     * @param acceleration a vector with the units <code>m/s/s</code>
     */
    public void addAcceleration(Vec2 acceleration) {
        netForce = netForce.add(acceleration.mul(getInverseMass())); // dF = a/m
    }

    /**
     * Applies an impulse to the body's center of mass.
     *
     * @param impulse a vector with the units <code>kg•m/s</code>
     */
    public void addImpulse(Vec2 impulse) {
        velocity = velocity.add(impulse.mul(getInverseMass())); // dv = J/m = m*dv/m
    }

    public void addImpulseAtPoint(Vec2 impulse, Vec2 position) {
        addImpulse(impulse);
        addAngularImpulse(position.sub(transform.position).cross(impulse));
    }

    /**
     * Adds a velocity to this body in the given direction.
     *
     * @param velocityChange a vector with the units <code>m/s</code>
     */
    public void addVelocity(Vec2 velocityChange) {
        velocity = velocity.add(velocityChange);
    }

    // Angular Movement Methods

    /**
     * Applies a torque to this body's center of mass in the clockwise direction.
     *
     * @param torque a scalar with units <code>kg•m•m/s/s</code>
     */
    public void addTorque(float torque) {
        netTorque += torque;
    }

    /**
     * Applies an angular impulse to this body in the clockwise direction.
     *
     * @param impulse a scalar with units <code>deg/s</code>
     */
    public void addAngularImpulse(float impulse) {
        angularVelocity += impulse * getInverseAngularMass();
    }

    /**
     * Adds an angular velocity to this body in the clockwise direction.
     *
     * @param velocityChange a scalar with units <code>deg/s</code>
     */
    public void addAngularVelocity(float velocityChange) {
        angularVelocity += velocityChange;
    }

    // Linear Motion Properties

    public Vec2 getPosition() {
        return transform.position;
    }

    public void setPosition(Vec2 position) {
        transform.position = position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return velocity.len();
    }

    public float getMass() {
        return mass;
    }

    private Rigidbody2D setMass(float mass) {
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

    // Angular Motion Properties

    public float getRotation() {
        return transform.rotation;
    }

    public void setRotation(float rotation) {
        transform.rotation = rotation;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    private float getInverseAngularMass() {
        if (collider == null || hasInfiniteMass())
            return 0;

        float angularMass = mass;
        if (collider instanceof CircleCollider)
            angularMass *= MathUtils.PI * 0.5f * ((CircleCollider) collider).radius() * ((CircleCollider) collider).radius();
        else if (collider instanceof BoxCollider2D)
            angularMass *= MathUtils.pythagoreanSquared(((BoxCollider2D) collider).width(), ((BoxCollider2D) collider).height()) / 12f;
        return 1 / angularMass;
    }

    public float getAngularDrag() {
        return angularDrag;
    }

    public Rigidbody2D setAngularDrag(float angularDrag) {
        this.angularDrag = MathUtils.clamp(angularDrag, 0, 1);
        return this;
    }

    // Behavior Getters and Setters

    public boolean followGravity() {
        return followGravity;
    }

    public Rigidbody2D setFollowGravity(boolean followGravity) {
        this.followGravity = followGravity;
        return this;
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
