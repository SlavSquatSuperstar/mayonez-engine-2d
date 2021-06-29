package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Preferences;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.components.Component;

public class RigidBody2D extends Component {

    public float mass = 1;
    public boolean followsGravity = true;
    private Vector2 velocity, netForce;

    public RigidBody2D() {
        this.velocity = new Vector2();
        this.netForce = new Vector2();
    }

    @Override
    public void update(float dt) {
        // Don't do anything if parent object is static
        if (!parent.followPhysics)
            return;

        // TODO account for direction of gravity
        if (Math.abs(velocity.y) > Preferences.TERMINAL_VELOCITY)
            velocity.y = Math.signum(velocity.y) * Preferences.TERMINAL_VELOCITY;

        velocity = velocity.add(netForce.div(mass).mul(dt));
        parent.transform.move(velocity); // s = v*t
        netForce.x = netForce.y = 0; // reset accumulated forces
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

    // TODO Top left vs center positioning
    public Vector2 position() {
        return parent.transform.position;
    }

    public void setPosition(Vector2 position) {
        parent.transform.position = position;
    }

    public float rotation() {
        return parent.transform.rotation;
    }

    public void setRotation(float rotation) {
        parent.transform.rotation = rotation;
    }

}
