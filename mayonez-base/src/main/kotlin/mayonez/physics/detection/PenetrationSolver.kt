package mayonez.physics.detection

import mayonez.math.shapes.*

/**
 * Calculates the penetration vector between two intersecting shapes.
 * Solving for the overlap is the second step in collision detector, after
 * [mayonez.physics.detection.CollisionDetector].
 *
 * @author SlavSquatSuperstar
 */
internal fun interface PenetrationSolver {
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