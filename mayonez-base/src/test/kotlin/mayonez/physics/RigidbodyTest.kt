package mayonez.physics

import mayonez.math.*
import mayonez.math.FloatMath.toDegrees
import mayonez.math.FloatMath.toRadians
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.physics.Rigidbody] class.
 *
 * @author SlavSquatSuperstar
 */
internal class RigidbodyTest {
    @Test
    fun appliedForceChangesVelocity() {
        val rb = Rigidbody(2f)
        rb.applyForce(Vec2(2f, 0f)) // 2 N right
        rb.physicsUpdateOneSecond()
        assertEquals(Vec2(1f, 0f), rb.velocity) // 1 m/s right
    }

    @Test
    fun appliedTorqueChangesAngVelocity() {
        val rb = Rigidbody(2f)
        rb.applyTorque(toRadians(2f)) // 2 N•m (rad) CCW
        rb.physicsUpdateOneSecond()
        assertEquals(1f, rb.angVelocity) // 1 deg/sec right
    }

    @Test
    fun torqueScalesWithRadius() {
        val rb = Rigidbody(2f)
        val force = Vec2(2f, 0f) // 2 N right
        rb.applyForce(force)
        val radius = Vec2(0f, toRadians(2f)) - rb.position
        rb.applyTorque(radius.cross(force)) // 4 N•m CW
        rb.physicsUpdateOneSecond()
        assertEquals(-2f, rb.angVelocity) // 2 deg/s CW
    }

    @Test
    fun pointVelocityScalesWithRadius() {
        val rb = Rigidbody(1f, 0f, 0f)
        rb.addAngularVelocity(toDegrees(2f)) // 2 rad/sec
        rb.physicsUpdateOneSecond()
        assertEquals(Vec2(0f, 2f), rb.getPointVelocity(Vec2(1f, 0f))) // 2 m/s up
    }

    @Test
    fun pointVelocityAddsWithLinearVelocity() {
        val rb = Rigidbody(1f, 0f, 0f)
        rb.addVelocity(Vec2(1f, 0f)) // 1 m/s right
        rb.addAngularVelocity(toDegrees(2f)) // 2 rad/sec
        rb.physicsUpdateOneSecond()
        // 1 m/s right, 2 m/s up
        assertEquals(Vec2(1f, 2f), rb.getPointVelocity(rb.position + Vec2(1f, 0f)))
        // 1 m/s right, 2 m/s down
        assertEquals(Vec2(1f, -2f), rb.getPointVelocity(rb.position + Vec2(-1f, 0f)))
    }

    private fun Rigidbody.physicsUpdateOneSecond() {
        this.integrateForce(1f, Vec2())
        this.integrateVelocity(1f)
    }
}
