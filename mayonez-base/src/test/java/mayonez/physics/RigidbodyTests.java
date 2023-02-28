package mayonez.physics;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.physics.Rigidbody} class.
 *
 * @author SlavSquatSuperstar
 */
public class RigidbodyTests {

    @Test
    public void appliedForceChangesVelocity() {
        var rb = new Rigidbody(2);
        rb.applyForce(new Vec2(2, 0)); // 2 N right
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(1, 0), rb.getVelocity()); // 1 m/s right
    }

    @Test
    public void appliedTorqueChangesAngVelocity() {
        var rb = new Rigidbody(2);
        rb.applyTorque(FloatMath.toRadians(2)); // 2 N•m (rad) CCW
        physicsUpdate(rb, 1);
        assertEquals(1, rb.getAngVelocity()); // 1 deg/sec right
    }

    @Test
    public void torqueScalesWithRadius() {
        var rb = new Rigidbody(2);
        var force = new Vec2(2, 0); // 2 N right
        rb.applyForce(force);
        var radius = new Vec2(0, FloatMath.toRadians(2)).sub(rb.getPosition());
        rb.applyTorque(radius.cross(force)); // 4 N•m CW
        physicsUpdate(rb, 1);
        assertEquals(-2, rb.getAngVelocity()); // 2 deg/s CW
    }

    @Test
    public void pointVelocityScalesWithRadius() {
        var rb = new Rigidbody(1, 0, 0);
        rb.addAngularVelocity(FloatMath.toDegrees(2)); // 2 rad/sec
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(0, 2), rb.getPointVelocity(new Vec2(1, 0))); // 2 m/s up
    }

    @Test
    public void pointVelocityAddsWithLinearVelocity() {
        var rb = new Rigidbody(1, 0, 0);
        rb.addVelocity(new Vec2(1, 0)); // 1 m/s right
        rb.addAngularVelocity(FloatMath.toDegrees(2)); // 2 rad/sec
        physicsUpdate(rb, 1);
        // 1 m/s right, 2 m/s up
        assertEquals(new Vec2(1, 2), rb.getPointVelocity(rb.getPosition().add(new Vec2(1, 0))));
        // 1 m/s right, 2 m/s down
        assertEquals(new Vec2(1, -2), rb.getPointVelocity(rb.getPosition().add(new Vec2(-1, 0))));
    }

    static void physicsUpdate(Rigidbody rb, float dt) {
        rb.integrateForce(dt);
        rb.integrateVelocity(dt);
    }

}
