package mayonez.physics.raycast

import mayonez.annotations.*
import mayonez.math.*
import mayonez.math.shapes.*

/**
 * Raycasts onto convex shapes using the GJK algorithm.
 *
 * Source: org.dyn4j.collision.narrowphase.Gjk
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
@Suppress("ALL")
class GJKRaycastDetector {
    private fun raycastGJK(shape: Shape, ray: Ray, limit: Float): RaycastInfo? {
        val lambda = 0.0
        val vecA = Vec2()
        val vecB = Vec2()

        val start = ray.origin
        val closest = start
        return null
        // TODO finish me
    }
}