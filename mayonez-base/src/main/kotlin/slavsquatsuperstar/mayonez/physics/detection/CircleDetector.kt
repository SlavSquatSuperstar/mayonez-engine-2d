package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.Circle

/**
 * A class that detects collisions between circles.
 *
 * @author SlavSquatSuperstar
 */
object CircleDetector {

    // Circle vs Circle: 1 contact point
    /**
     * Calculates the contact and separation between two circles.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return the collision information, or null if no collision
     */
    @JvmStatic
    fun detect(circle1: Circle?, circle2: Circle?): CollisionInfo? {
        if (circle1 == null || circle2 == null) return null
        val sumRadii = circle1.radius + circle2.radius
        val distance = circle2.center() - circle1.center()
        if (distance.lenSq() >= sumRadii * sumRadii) return null // Circles too far away

        val dist = distance.len()
        val depth = sumRadii - dist
        val normal = distance / dist
        val result = CollisionInfo(circle1, circle2, normal, depth)
        result.addContact(circle1.center() + (normal * (circle1.radius - depth)))
        return result
    }

}