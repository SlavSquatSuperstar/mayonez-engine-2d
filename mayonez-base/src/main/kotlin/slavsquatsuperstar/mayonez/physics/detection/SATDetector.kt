package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Range
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.*
import kotlin.math.min

/**
 * Detects if two shapes are colliding and finds their contacts using the separating-axis theorem.
 *
 * @author SlavSquatSuperstar
 */
object SATDetector {

    /**
     * Detects a collision between two shapes and calculates the contact and penetration.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return the collision information, or null if no collision
     */
    @JvmStatic
    fun detect(shape1: Shape, shape2: Shape): CollisionInfo? {
        return when {
            shape1 is Circle && shape2 is Circle -> CircleDetector.detect(shape1, shape2)
            shape1 is Circle && shape2 is Polygon -> detectCirclePolygon(shape1, shape2)
            shape1 is Polygon && shape2 is Circle -> detectCirclePolygon(shape2, shape1)?.flip()
            shape1 is Polygon && shape2 is Polygon -> detectPolygonPolygon(shape1, shape2)
            else -> null // cannot solve using SAT
        }
    }

    // Circle vs Polygon: 1 contact point
    private fun detectCirclePolygon(circle: Circle, polygon: Polygon): CollisionInfo? {
        val closestToCircle = polygon.nearestPoint(circle.center()) // Point from shape deepest in circle
        if (closestToCircle !in circle) return null

        val depth = circle.radius - closestToCircle.distance(circle.center())
        val result = CollisionInfo(circle, polygon, closestToCircle - circle.center(), depth)
        result.addContact(closestToCircle)
        return result
    }

    // Polygon vs Polygon: 1-2 contact points
    private fun detectPolygonPolygon(polygon1: Polygon, polygon2: Polygon): CollisionInfo? {
        // Track minimum penetration vector
        var minOverlap = Float.MAX_VALUE
        var minAxis = Vec2()

        // Project shapes onto axes and test for a separating axis
        val axes = polygon1.normals + polygon2.normals
        for (axis in axes) {
            val range1 = polygon1.projectOnAxis(axis)
            val range2 = polygon2.projectOnAxis(axis)
            if (!range1.overlaps(range2)) return null // no overlap

            val overlap = range1.getOverlap(range2)
            if (overlap < minOverlap) {
                minOverlap = overlap
                minAxis = axis
            }
        }

        return PenetrationSolver(polygon1, polygon2, minAxis, minOverlap).solve()
    }

    // SAT Helpers

    /**
     * Whether a collision involving the given shape can be efficiently detected. SAT detection works best with polygons
     * with a small vertex count (boxes and triangles) and also supports circle-polygon collisions.
     */
    internal fun preferred(shape: Shape): Boolean { // use SAT for boxes, triangles, and circles
        return when (shape) {
            // Round
            is Circle -> true
            is Ellipse -> false
            // Polygons
            is Triangle -> true
            is Rectangle -> true
            is Polygon -> shape.isBox
            // Other
            is Edge -> false // TODO need to test edge colliders
            else -> false
        }
    }

    private fun Polygon.projectOnAxis(axis: Vec2): Range { // positive is in axis direction
        val projections = FloatArray(this.numVertices) { this.vertices[it].dot(axis) }
        return Range(MathUtils.min(*projections), MathUtils.max(*projections))
    }

    private fun Range.overlaps(other: Range): Boolean = (this.min <= other.max) && (this.min <= other.max)

    private fun Range.getOverlap(other: Range): Float {
        return min(this.max - other.min, other.max - this.min)
    }

}
