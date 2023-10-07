package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*

/**
 * Detects if two shapes are colliding and finds their penetration using
 * the separating-axis theorem (SAT).
 *
 * @author SlavSquatSuperstar
 */
internal class SATDetector : CollisionDetector<Shape>, PenetrationSolver {

    override fun checkIntersection(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            shape1 is Circle && shape2 is Circle ->
                CircleDetector.checkIntersection(shape1, shape2)

            shape1 is Circle && shape2 is Polygon ->
                getCirclePolygonPenetration(shape1, shape2, false) != null

            shape1 is Polygon && shape2 is Circle ->
                getCirclePolygonPenetration(shape2, shape1, true) != null

            shape1 is Polygon && shape2 is Polygon ->
                checkPolygonPolygonIntersection(shape1, shape2)

            else -> false // cannot solve using SAT
        }
    }

    override fun getPenetration(shape1: Shape?, shape2: Shape?): Penetration? {
        return when {
            shape1 is Circle && shape2 is Polygon -> getCirclePolygonPenetration(shape1, shape2, false)
            shape1 is Polygon && shape2 is Circle -> getCirclePolygonPenetration(shape2, shape1, true)
            shape1 is Polygon && shape2 is Polygon -> getPolygonPolygonPenetration(shape1, shape2)
            else -> null // cannot solve using SAT
        }
    }

}

// Circle vs Polygon: 1 contact point
private fun getCirclePolygonPenetration(circle: Circle, polygon: Polygon, flip: Boolean): Penetration? {
    val closestToCircle = polygon.nearestPoint(circle.center()) // Point from shape deepest in circle
    if (closestToCircle !in circle) return null

    val depth = circle.radius - closestToCircle.distance(circle.center())
    val normal = (closestToCircle - circle.center()).unit()
    return Penetration(if (flip) -normal else normal, depth)
}

// Polygon vs Polygon: 1-2 contact points

private fun checkPolygonPolygonIntersection(polygon1: Polygon, polygon2: Polygon): Boolean {
    // Project shapes onto axes and test for a separating axis
    val axes = polygon1.normals + polygon2.normals
    for (axis in axes) {
        if (!axis.hasOverlap(polygon1, polygon2)) return false
    }
    return true
}

private fun getPolygonPolygonPenetration(polygon1: Polygon, polygon2: Polygon): Penetration? {
    // Track minimum penetration vector
    var minOverlap = Float.MAX_VALUE
    var minAxis = Vec2()

    // Project shapes onto axes and test for a separating axis
    val axes = polygon1.normals + polygon2.normals
    for (axis in axes) {
        val overlap = axis.getOverlap(polygon1, polygon2) ?: return null
        if (overlap < minOverlap) {
            minOverlap = overlap
            minAxis = axis
        }
    }
    return Penetration(minAxis, minOverlap)
}