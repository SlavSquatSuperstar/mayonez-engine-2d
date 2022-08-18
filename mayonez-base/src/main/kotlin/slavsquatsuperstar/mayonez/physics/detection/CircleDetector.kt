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

//    internal fun detect(ellipse1: Ellipse?, ellipse2: Ellipse?): CollisionInfo? {
//        return if (ellipse1?.isCircle == true && ellipse2?.isCircle == true)
//            detect(ellipse1.toCircle(), ellipse2.toCircle())
//        else null
//    }
//
//    private fun Ellipse.toCircle(): Circle = if (this is Circle) this else Circle(center(), size.x * 0.5f)

}