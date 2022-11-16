package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.mayonez.physics.detection.*
import slavsquatsuperstar.mayonez.physics.shapes.Circle
import slavsquatsuperstar.mayonez.physics.shapes.Ray
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Implements collision detection algorithms using primitive shapes for the physics engine.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    @JvmStatic
    fun checkCollision(shape1: Shape?, shape2: Shape?): Boolean = IntersectionDetector.checkCollision(shape1, shape2)

    @JvmStatic
    fun getContacts(shape1: Shape?, shape2: Shape?): Manifold? {
        if (shape1 == null || shape2 == null) return null
        if (shape1 is Circle && shape2 is Circle) return CircleDetector.getContacts(shape1, shape2)
        val pen = if (SATDetector.preferred(shape1) && SATDetector.preferred(shape2))
            SATDetector().getPenetration(shape1, shape2)
        else GJKDetector().getPenetration(shape1, shape2)
        return pen?.getContacts(shape1, shape2)
    }

    /**
     * Casts a ray onto a shape and calculates the contact point, distance, and normal.
     *
     * @param shape the shape to raycast
     * @param ray   the ray to cast
     * @param limit the max length the ray can travel
     * @return the contact information, or null if the ray misses
     */
    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): RaycastInfo? = RaycastDetector.raycast(shape, ray, limit)

}