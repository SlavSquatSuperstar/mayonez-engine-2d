package mayonez.physics.manifold

import mayonez.math.shapes.*
import mayonez.physics.detection.*

/**
 * Calculates the contact points (collision manifold) between two
 * intersecting shapes given their [mayonez.physics.detection.Penetration].
 * Solving for contacts is the third and final step in collision
 * detection, after [mayonez.physics.detection.PenetrationSolver].
 *
 * @author SlavSquatSuperstar
 */
internal fun interface ContactSolver {
    /**
     * Calculates the contact points between two shapes.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @param penetration the penetration vector
     * @return the collision manifold, or null if no collision
     */
    fun getContacts(shape1: Shape, shape2: Shape, penetration: Penetration?): Manifold?
}