package mayonez.physics

import mayonez.math.shapes.*
import mayonez.physics.detection.*
import mayonez.physics.manifold.*

/**
 * Checks collisions and calculates contact points for primitive shapes.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    @JvmStatic
    fun checkCollision(shape1: Shape?, shape2: Shape?): Boolean {
        return IntersectionDetector.checkIntersection(shape1, shape2)
    }

    @JvmStatic
    fun getContacts(shape1: Shape?, shape2: Shape?): Manifold? {
        return when {
            (shape1 == null) || (shape2 == null) -> null
            (shape1 is Circle) && (shape2 is Circle) -> CircleDetector.getContacts(shape1, shape2)
            else -> {
                val pen = getPenetration(shape1, shape2)
                ClippingManifoldSolver().getContacts(shape1, shape2, pen)
            }
        }
    }

    private fun getPenetration(shape1: Shape, shape2: Shape): Penetration? {
        return if (SATDetector.preferred(shape1) && SATDetector.preferred(shape2)) {
            SATDetector().getPenetration(shape1, shape2)
        } else {
            GJKDetector().getPenetration(shape1, shape2)
        }
    }

}