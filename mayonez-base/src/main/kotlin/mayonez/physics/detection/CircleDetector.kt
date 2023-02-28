package mayonez.physics.detection

import mayonez.math.shapes.*
import mayonez.physics.resolution.*

/**
 * A class that detects collisions between circles.
 *
 * @author SlavSquatSuperstar
 */
object CircleDetector {

    /**
     * Checks whether two circles are intersecting.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return if the two circles overlap or touch
     */
    @JvmStatic
    fun checkIntersection(circle1: Circle?, circle2: Circle?): Boolean {
        if (circle1 == null || circle2 == null) return false
        val sumRadii = circle1.radius + circle2.radius
        return circle2.center().distanceSq(circle1.center()) <= sumRadii * sumRadii
    }

    // Circle vs Circle: 1 contact point
    /**
     * Detects a collision between two circles and calculates the contact and
     * penetration.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return the collision information, or null if no collision
     */
    @JvmStatic
    fun getContacts(circle1: Circle?, circle2: Circle?): Manifold? {
        if (circle1 == null || circle2 == null) return null
        val sumRadii = circle1.radius + circle2.radius
        val vecDist = circle2.center() - circle1.center() // Distance between centers
        if (vecDist.lenSq() > sumRadii * sumRadii) return null // Circles too far away

        val dist = vecDist.len()
        val depth = sumRadii - dist // Penetration depth
        val normal = vecDist / dist
        val result = Manifold(circle1, circle2, normal, depth)
        result.addContact(circle1.center() + (normal * (circle1.radius - depth)))
        return result
    }

}