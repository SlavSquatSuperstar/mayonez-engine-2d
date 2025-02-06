package mayonez.physics.colliders

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.dynamics.*

/**
 * A box collider for fast moving objects that automatically extends itself along its path
 * to avoid tunneling.
 *
 * @author SlavSquatSuperstar
 */
class BulletBoxCollider(size: Vec2) : BoxCollider(size) {

    private lateinit var displacement: Vec2
    private var rb: PhysicsBody? = null

    // Collider Properties
    var sweepFactor: Float = 1f // How much to stretch swept box
    var isPrimaryAxisX: Boolean = true // Whether 0º is at +x or +y

    override fun start() {
        super.start()
        displacement = Vec2()
        rb = physicsBody
    }

    protected override fun update(dt: Float) {
        // Assume velocity direction is along object orientation
        // Assume no rotational velocity
        if (rb != null) {
            displacement.set(rb!!.velocity * dt * sweepFactor)
        }
    }

    override fun getShape(): Shape {
        val addedSize = if (this.isPrimaryAxisX) {
            Vec2(displacement.len(), 0f)
        } else {
            Vec2(0f, displacement.len())
        }

        // New center is midpoint
        // New length is length + displacement
        return Rectangle(
            transform.position + (displacement * 0.5f),
            (this.size * transform.scale) + addedSize,
            transform.rotation
        )
    }

//    protected override fun debugRender() {
//        val originalShape = super.getShape()
//        val nextShape = originalShape.translate(displacement)
//        val sweptShape = this.getShape()
//
//        scene.debugDraw.drawShape(originalShape, Colors.BLUE)
//        scene.debugDraw.drawShape(nextShape, Colors.RED)
//        scene.debugDraw.drawShape(sweptShape, Colors.LIGHT_GREEN)
//    }

}