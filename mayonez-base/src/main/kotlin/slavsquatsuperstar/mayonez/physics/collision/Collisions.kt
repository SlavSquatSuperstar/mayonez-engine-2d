package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.mayonez.physics.detection.CircleDetector
import slavsquatsuperstar.mayonez.physics.detection.GJKDetector
import slavsquatsuperstar.mayonez.physics.detection.IntersectionDetector
import slavsquatsuperstar.mayonez.physics.detection.RaycastDetector
import slavsquatsuperstar.mayonez.physics.shapes.Circle
import slavsquatsuperstar.mayonez.physics.shapes.Ray
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Implements collision detection algorithms using primitive shapes for the physics engine.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    const val MAX_GJK_ITERATIONS: Int = 20
    const val MAX_EPA_ITERATIONS: Int = 40

    // Raycast Methods

    /**
     * Casts a ray onto a shape and returns the contact information
     *
     * @param shape the shape to raycast
     * @param ray   the ray to cast
     * @param limit the max length the ray can travel
     * @return data containing the distance, contact point, and surface normal, or null if the ray misses
     */
    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): RaycastInfo? = RaycastDetector.raycast(shape, ray, limit)

    // Collision Detection

    @JvmStatic
    fun detectCollision(shape1: Shape?, shape2: Shape?): Boolean = IntersectionDetector.detect(shape1, shape2)

    @JvmStatic
    fun getCollisionInfo(shape1: Shape?, shape2: Shape?): CollisionInfo? {
        if (shape1 == null || shape2 == null) return null
        if (shape1 is Circle && shape2 is Circle) return CircleDetector.detect(shape1, shape2)
//        if (SATDetector.preferred(shape1) && SATDetector.preferred(shape2)) return SATDetector(shape1, shape2).detect()
//        return null
        return GJKDetector(shape1, shape2).detect()
    }

}