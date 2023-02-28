package mayonez.physics.detection

import mayonez.math.shapes.*

/**
 * Detects whether two shapes are intersecting and calculates the
 * penetration vector if they are. Checking for intersections is the first
 * step in collision detection, and solving for the overlap is the second.
 *
 * @author SlavSquatSuperstar
 */
interface CollisionDetector {
//abstract class CollisionDetector(protected val shape1: Shape?, protected val shape2: Shape?) {

    /**
     * Checks whether two circles are intersecting (overlapping or touching).
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return if the two shapes collide
     */
    fun checkIntersection(shape1: Shape?, shape2: Shape?): Boolean

    /**
     * Calculates the minimum penetration direction and depth between two
     * shapes if they are colliding.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return the penetration, or null if no collision
     */
    fun getPenetration(shape1: Shape?, shape2: Shape?): Penetration?

}