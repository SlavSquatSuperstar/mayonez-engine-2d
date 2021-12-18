package slavsquatsuperstar.test.physicstests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.Rigidbody2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link Rigidbody2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class RigidbodyTests {

    @Test
    public void appliedForceChangesVelocity() {
        Rigidbody2D rb = new Rigidbody2D(2);
        rb.addForce(new Vec2(2, 0));
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(1, 0), rb.getVelocity());
    }

    @Test
    public void appliedTorqueChangesAngVelocity() {
        Rigidbody2D rb = new Rigidbody2D(2);
        rb.addTorque(2);
        physicsUpdate(rb, 1);
        assertEquals(1, rb.getAngVelocity());
    }

    @Test
    public void torqueScalesWithRadius() {
        Rigidbody2D rb = new Rigidbody2D(2);
        rb.addForceAtPoint(new Vec2(2, 0), new Vec2(0, 2));
        physicsUpdate(rb, 1);
        assertEquals(-2, rb.getAngVelocity());
    }

    @Test
    public void angularVelocityScalesWithRadius() {
        Rigidbody2D rb = new Rigidbody2D(1, 0, 0);
        rb.addAngularVelocity(360 / MathUtils.PI);
        physicsUpdate(rb, 1);
        assertEquals(2, rb.getRelativePointVelocity(new Vec2(1, 0)).len(), MathUtils.EPSILON);
        assertEquals(new Vec2(0, 2), rb.getRelativePointVelocity(new Vec2(1, 0)));
    }

    @Test
    public void pointVelocityScalesWithRadius() {
        Rigidbody2D rb = new Rigidbody2D(1, 0, 0);
        rb.addAngularVelocity(360 / MathUtils.PI);
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(0, 2), rb.getRelativePointVelocity(new Vec2(1, 0)));
    }

    @Test
    public void pointVelocityAddsWithBodyVelocity() {
        Rigidbody2D rb = new Rigidbody2D(1, 0, 0);
        rb.addVelocity(new Vec2(1, 0));
        rb.addAngularVelocity(360 / MathUtils.PI);
        physicsUpdate(rb, 1);
        assertEquals(new Vec2(1, 2), rb.getPointVelocity(new Vec2(1, 0)));
        assertEquals(new Vec2(1, -2), rb.getPointVelocity(new Vec2(-1, 0)));
    }
    
    static void physicsUpdate(Rigidbody2D rb, float dt) {
        rb.integrateForce(dt);
        rb.integrateVelocity(dt);
    }

}
