package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

/**
 * Detects if two shapes are colliding and finds their penetration using the
 * separating-axis theorem (SAT).
 *
 * @author SlavSquatSuperstar
 */
internal class SATDetector : CollisionDetector, PenetrationSolver {

    override fun checkIntersection(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            shape1 is Circle && shape2 is Circle -> CircleDetector.checkIntersection(shape1, shape2)
            shape1 is Circle && shape2 is Polygon -> detectCirclePolygon(shape1, shape2, false) != null
            shape1 is Polygon && shape2 is Circle -> detectCirclePolygon(shape2, shape1, true) != null
            shape1 is Polygon && shape2 is Polygon -> checkPolygonPolygon(shape1, shape2)
            else -> false // cannot solve using SAT
        }
    }

    private fun checkPolygonPolygon(polygon1: Polygon, polygon2: Polygon): Boolean {
        // Project shapes onto axes and test for a separating axis
        val axes = polygon1.normals + polygon2.normals
        for (axis in axes) {
            if (!axis.hasOverlap(polygon1, polygon2)) return false
        }
        return true
    }

    // TODO write unit tests
    override fun getPenetration(shape1: Shape?, shape2: Shape?): Penetration? {
        return when {
            shape1 is Circle && shape2 is Polygon -> detectCirclePolygon(shape1, shape2, false)
            shape1 is Polygon && shape2 is Circle -> detectCirclePolygon(shape2, shape1, true)
            shape1 is Polygon && shape2 is Polygon -> detectPolygonPolygon(shape1, shape2)
            else -> null // cannot solve using SAT
        }
    }

    // Circle vs Polygon: 1 contact point
    private fun detectCirclePolygon(circle: Circle, polygon: Polygon, flip: Boolean): Penetration? {
        val closestToCircle = polygon.nearestPoint(circle.center()) // Point from shape deepest in circle
        if (closestToCircle !in circle) return null

        val depth = circle.radius - closestToCircle.distance(circle.center())
        val normal = (closestToCircle - circle.center()).unit()
        return Penetration(if (flip) -normal else normal, depth)
    }

    // Polygon vs Polygon: 1-2 contact points
    private fun detectPolygonPolygon(polygon1: Polygon, polygon2: Polygon): Penetration? {
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

    // SAT Helpers
    companion object {
        /**
         * Whether a collision involving the given shape can be efficiently
         * detected. SAT detection works best with polygons with a small vertex
         * count (boxes and triangles) and also supports circle-polygon collisions.
         */
        internal fun preferred(shape: Shape): Boolean { // use SAT for boxes, triangles, and circles
            return when (shape) {
                is Circle -> true
                is Polygon -> shape.numVertices <= 4
                // TODO need to test edge colliders
                else -> false
            }
        }

        private fun Polygon.projectOnAxis(axis: Vec2): Interval { // positive is in axis direction
            val projections = FloatArray(this.numVertices) { this.vertices[it].dot(axis) }
            return Interval(FloatMath.min(*projections), FloatMath.max(*projections))
        }

        /** Whether two intervals overlap each other on this axis. */
        private fun Vec2.hasOverlap(poly1: Polygon, poly2: Polygon): Boolean {
            val range1 = poly1.projectOnAxis(this)
            val range2 = poly2.projectOnAxis(this)
            return range1.overlaps(range2)
        }

        /**
         * Calculate the overlap between two intervals on this axis, or return null
         * if they do not overlap.
         */
        private fun Vec2.getOverlap(poly1: Polygon, poly2: Polygon): Float? {
            val range1 = poly1.projectOnAxis(this)
            val range2 = poly2.projectOnAxis(this)
            if (!range1.overlaps(range2)) return null
            return min(range2.max - range1.min, range1.max - range2.min)
        }

        /** Whether two intervals overlap each other. */
        private fun Interval.overlaps(other: Interval): Boolean = (this.min <= other.max) && (this.max >= other.min)

    }
}
