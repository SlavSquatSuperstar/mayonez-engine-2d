package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.mayonez.physics.shapes.Circle
import slavsquatsuperstar.mayonez.physics.shapes.Edge
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * A class that detects whether shapes intersect. Uses simple tests when possible, otherwise performs the GJK algorithm
 * for complex shapes.
 *
 * @author SlavSquatSuperstar
 */
object IntersectionDetector {

    /**
     * Determines whether two shapes are touching or overlapping.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return whether the shapes intersect
     */
    @JvmStatic
    fun detect(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            (shape1 == null) || (shape2 == null) -> false
            (shape1 is Edge) && (shape2 is Edge) -> intersectEdges(shape1, shape2)
            (shape1 is Circle) && (shape2 is Circle) -> intersectCircles(shape1, shape2)
            (shape1 is Rectangle) && (shape2 is Rectangle) -> intersectRectangles(shape1, shape2)
            else -> intersectGJK(shape1, shape2) // use GJK for complex shapes
        }
    }

    /**
     * Performs a simple circle vs. circle intersection test by comparing the distances their centers and the sum of
     * their radii.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     *
     * @return if the two circles overlap or touch
     */
    private fun intersectCircles(circle1: Circle, circle2: Circle): Boolean {
        val distSq = circle1.center().distanceSq(circle2.center())
        val sumRadiiSq = MathUtils.squared(circle1.radius + circle2.radius)
        return distSq <= sumRadiiSq
    }

    /**
     * Performs a simple line segment intersection test by calculating the contact point between two rays and ensuring
     * it is within both segments.
     *
     * @param edge1 the first edge
     * @param edge2 the second edge
     *
     * @return if the two edges cross or touch
     */
    // TODO linear systems matrix
    private fun intersectEdges(edge1: Edge, edge2: Edge): Boolean {
        // Find line directions
        val dir1 = edge1.toVector() / edge1.length
        val dir2 = edge2.toVector() / edge2.length
        val cross = dir1.cross(dir2)

        // If lines are parallel, then they must be overlapping
        return if (MathUtils.equals(cross, 0f)) {
            ((edge2.start in edge1 || edge2.end in edge1)
                    || (edge1.start in edge2 || edge1.end in edge2))
        } else {
            // Calculate intersection point
            val diffStarts = edge2.start - edge1.start
            val dist1 = diffStarts.cross(dir2) / cross
            val dist2 = diffStarts.cross(dir1) / cross

            // Contact must be in both lines
            MathUtils.inRange(dist1, 0f, edge1.length) && MathUtils.inRange(dist2, 0f, edge2.length)
        }
    }

    /**
     * Performs a simple rectangle vs. rectangle intersection test using the separating-axis theorem on their x- and
     * y-axes.
     *
     * @param rect1 the first rectangle
     * @param rect2 the second rectangle
     *
     * @return if the two rectangles overlap or touch
     */
    private fun intersectRectangles(rect1: Rectangle, rect2: Rectangle): Boolean {
        return if (rect1.isAxisAligned && rect2.isAxisAligned) {
            // Perform SAT on x-axis
            val min1 = rect1.min()
            val max1 = rect1.max()
            val min2 = rect2.min()
            val max2 = rect2.max()
            // a.min <= b.max && a.max <= b.min
            val satX = (min1.x <= max2.x) && (min2.x <= max1.x)
            val satY = (min1.y <= max2.y) && (min2.y <= max1.y)
            return satX && satY
        } else {
            SATDetector(rect1, rect2).checkIntersection()
        }
    }

    private fun intersectGJK(shape1: Shape, shape2: Shape): Boolean {
        return GJKDetector(shape1, shape2).getSimplex() != null
    }

}