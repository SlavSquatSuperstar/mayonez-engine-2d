package mayonez.physics.detection

import mayonez.math.MathUtils
import mayonez.math.Range
import mayonez.math.Vec2
import mayonez.physics.shapes.Circle
import mayonez.physics.shapes.Polygon
import mayonez.physics.shapes.Shape
import kotlin.math.min

/**
 * Detects if two shapes are colliding and finds their contacts using the separating-axis theorem.
 *
 * @author SlavSquatSuperstar
 */
class SATDetector : CollisionDetector {

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
            val range1 = polygon1.projectOnAxis(axis)
            val range2 = polygon2.projectOnAxis(axis)
            if (!range1.overlaps(range2)) return false // no overlap
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
            val range1 = polygon1.projectOnAxis(axis)
            val range2 = polygon2.projectOnAxis(axis)
            if (!range1.overlaps(range2)) return null // no overlap

            val overlap = range1.getOverlap(range2)
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
         * Whether a collision involving the given shape can be efficiently detected. SAT detection works best with polygons
         * with a small vertex count (boxes and triangles) and also supports circle-polygon collisions.
         */
        internal fun preferred(shape: Shape): Boolean { // use SAT for boxes, triangles, and circles
            return when (shape) {
                is Circle -> true
                is Polygon -> shape.numVertices <= 4
                // TODO need to test edge colliders
                else -> false
            }
        }

        private fun Polygon.projectOnAxis(axis: Vec2): Range { // positive is in axis direction
            val projections = FloatArray(this.numVertices) { this.vertices[it].dot(axis) }
            return Range(MathUtils.min(*projections), MathUtils.max(*projections))
        }

        /**
         * If two intervals overlap each other.
         */
        private fun Range.overlaps(other: Range): Boolean = (this.min <= other.max) && (this.max >= other.min)

        /**
         * How much two intervals overlap each other. Non-negative result if they overlap and undefined if they do not.
         */
        private fun Range.getOverlap(other: Range): Float {
            return min(other.max - this.min, this.max - other.min)
        }

    }
}
