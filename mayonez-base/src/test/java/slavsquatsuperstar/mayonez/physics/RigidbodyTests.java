package slavsquatsuperstar.mayonez.physics;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.math.MathUtils;
import slavsquatsuperstar.mayonez.math.Vec2;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link slavsquatsuperstar.mayonez.physics.Rigidbody} class.
 *
 * @author SlavSquatSuperstar
 */
public class RigidbodyTests {

    @Test
    public void appliedForceChangesVelocity() {
        Rigidbody rb = new Rigidbody(2);
        rb.addForce(new Vec2(2, 0));
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(1, 0), rb.getVelocity());
    }

    @Test
    public void appliedTorqueChangesAngVelocity() {
        Rigidbody rb = new Rigidbody(2);
        rb.addTorque(2);
        physicsUpdate(rb, 1);
        assertEquals(1, rb.getAngVelocity());
    }

    @Test
    public void torqueScalesWithRadius() {
        Rigidbody rb = new Rigidbody(2);
        rb.addForceAtPoint(new Vec2(2, 0), new Vec2(0, 2));
        physicsUpdate(rb, 1);
        assertEquals(-2, rb.getAngVelocity());
    }

    @Test
    public void angularVelocityScalesWithRadius() {
        Rigidbody rb = new Rigidbody(1, 0, 0);
        rb.addAngularVelocity(360 / MathUtils.PI);
        physicsUpdate(rb, 1);
        assertEquals(2, rb.getRelativePointVelocity(new Vec2(1, 0)).len(), MathUtils.FLOAT_EPSILON);
        assertEquals(new Vec2(0, 2), rb.getRelativePointVelocity(new Vec2(1, 0)));
    }

    @Test
    public void pointVelocityScalesWithRadius() {
        Rigidbody rb = new Rigidbody(1, 0, 0);
        rb.addAngularVelocity(360 / MathUtils.PI);
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(0, 2), rb.getRelativePointVelocity(new Vec2(1, 0)));
    }

    @Test
    public void pointVelocityAddsWithBodyVelocity() {
        Rigidbody rb = new Rigidbody(1, 0, 0);
        rb.addVelocity(new Vec2(1, 0));
        rb.addAngularVelocity(360 / MathUtils.PI);
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(1, 2), rb.getPointVelocity(rb.getPosition().add(new Vec2(1, 0))));
        assertEquals(new Vec2(1, -2), rb.getPointVelocity(rb.getPosition().add(new Vec2(-1, 0))));
    }

    static void physicsUpdate(Rigidbody rb, float dt) {
        rb.integrateForce(dt);
        rb.integrateVelocity(dt);
    }

}
