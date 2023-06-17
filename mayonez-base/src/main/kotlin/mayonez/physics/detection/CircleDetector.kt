package mayonez.physics.detection

import mayonez.math.shapes.*
import mayonez.physics.manifold.*

/**
 * Performs collision checks between circles.
 *
 * @author SlavSquatSuperstar
 */
internal object CircleDetector: CollisionDetector<Circle> {

    /**
     * Checks whether two circles are touching or overlapping.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return if the two circles intersect
     */
    override fun checkIntersection(circle1: Circle?, circle2: Circle?): Boolean {
        if (circle1 == null || circle2 == null) return false
        val sumRadii = circle1.radius + circle2.radius
        val distSq = circle2.center().distanceSq(circle1.center())
        return distSq <= sumRadii * sumRadii
    }

    /**
     * Detects a collision between two circles and calculates the contact and
     * penetration.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return the collision information, or null if no collision
     */
    internal fun getContacts(circle1: Circle?, circle2: Circle?): Manifold? {
        if (circle1 == null || circle2 == null) return null
        val sumRadii = circle1.radius + circle2.radius
        val vecDist = circle2.center() - circle1.center() // Distance between centers
        if (vecDist.lenSq() > sumRadii * sumRadii) return null // Circles too far away

        // Calculate manifold
        val dist = vecDist.len()
        val depth = sumRadii - dist // Penetration depth
        val normal = vecDist / dist
        val result = Manifold(circle1, circle2, normal, depth)
        result.addContact(circle1.center() + (normal * (circle1.radius - depth)))
        return result
    }

}