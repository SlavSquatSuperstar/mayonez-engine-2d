package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.Circle

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
     * @return if the two circles overlap
     */
    fun checkIntersection(circle1: Circle?, circle2: Circle?): Boolean {
        if (circle1 == null || circle2 == null) return false
        val sumRadii = circle1.radius + circle2.radius
        return (circle2.center().distanceSq(circle1.center()) >= sumRadii * sumRadii)
    }

    // Circle vs Circle: 1 contact point
    /**
     * Detects a collision between two circles and calculates the contact and penetration.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     * @return the collision information, or null if no collision
     */
    fun detect(circle1: Circle?, circle2: Circle?): CollisionInfo? {
        if (circle1 == null || circle2 == null) return null
        val sumRadii = circle1.radius + circle2.radius
        val vecDist = circle2.center() - circle1.center() // Distance between centers
        if (vecDist.lenSq() >= sumRadii * sumRadii) return null // Circles too far away

        val dist = vecDist.len()
        val depth = sumRadii - dist // Penetration depth
        val normal = vecDist / dist
        val result = CollisionInfo(circle1, circle2, normal, depth)
        result.addContact(circle1.center() + (normal * (circle1.radius - depth)))
        return result
    }

//    /**
//     * Calculates the magnitude of the radius pointing towards a certain point.
//     */
//    private fun Ellipse.radiusToPoint(point: Vec2): Float {
//        return if (this is Circle) this.radius
//        else this.getRadius(point - this.center()).len()
//    }

}