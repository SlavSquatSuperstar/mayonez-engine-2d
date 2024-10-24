package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*

/**
 * A class that detects whether shapes intersect. Uses simple tests when
 * possible, otherwise performs the GJK algorithm for complex shapes.
 *
 * @author SlavSquatSuperstar
 */
internal object IntersectionDetector : CollisionDetector<Shape> {

    override fun checkIntersection(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            (shape1 == null) || (shape2 == null) -> false
            (shape1 is Edge) && (shape2 is Edge) -> intersectEdges(shape1, shape2)
            (shape1 is Circle) && (shape2 is Circle) -> CircleDetector.checkIntersection(shape1, shape2)
            (shape1 is Rectangle) && (shape2 is Rectangle) -> intersectRectangles(shape1, shape2)
            else -> return GJKDetector().getSimplex(shape1, shape2) != null // use GJK for complex shapes
        }
    }

    // Intersection Helper Methods

    /**
     * Performs a simple line segment intersection test by calculating the
     * contact point between two rays and ensuring it is within both segments.
     *
     * @param edge1 the first edge
     * @param edge2 the second edge
     * @return if the two edges cross or touch
     */
    private fun intersectEdges(edge1: Edge, edge2: Edge): Boolean {
        // Find line directions
        val dir1 = edge1.toVector() / edge1.length
        val dir2 = edge2.toVector() / edge2.length
        val cross = dir1.cross(dir2)

        return if (MathUtils.equals(cross, 0f)) { // Lines are parallel
            doEdgesOverlap(edge1, edge2)
        } else {
            // Calculate intersection point
            val diffStarts = edge2.start - edge1.start
            val dist1 = diffStarts.cross(dir2) / cross
            val dist2 = diffStarts.cross(dir1) / cross

            // Contact must be in both lines
            MathUtils.inRange(dist1, 0f, edge1.length) &&
                    MathUtils.inRange(dist2, 0f, edge2.length)
        }
    }

    private fun doEdgesOverlap(edge1: Edge, edge2: Edge): Boolean {
        return edge1.containsEndpoint(edge2) || edge2.containsEndpoint(edge1)
    }

    private fun Edge.containsEndpoint(other: Edge): Boolean {
        return this.start in other || this.end in other
    }

    /**
     * Performs a simple rectangle vs. rectangle intersection test using the
     * separating-axis theorem on their x- and y-axes.
     *
     * @param rect1 the first rectangle
     * @param rect2 the second rectangle
     * @return if the two rectangles overlap or touch
     */
    private fun intersectRectangles(rect1: Rectangle, rect2: Rectangle): Boolean {
        return if (rect1.isAxisAligned && rect2.isAxisAligned) {
            return intersectAABBs(rect1, rect2)
        } else {
            SATDetector().checkIntersection(rect1, rect2)
        }
    }

    private fun intersectAABBs(rect1: Rectangle, rect2: Rectangle): Boolean {
        // Perform SAT on x-axis
        val min1 = rect1.min()
        val max1 = rect1.max()
        val min2 = rect2.min()
        val max2 = rect2.max()
        // a.min <= b.max && a.max <= b.min
        val overlapsX = (min1.x <= max2.x) && (min2.x <= max1.x)
        val overlapsY = (min1.y <= max2.y) && (min2.y <= max1.y)
        return overlapsX && overlapsY
    }

}