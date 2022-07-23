package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle
import slavsquatsuperstar.mayonez.physics.shapes.Circle
import slavsquatsuperstar.mayonez.physics.shapes.Edge
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Implements collision detection algorithms using primitive shapes for the physics engine.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
object Collisions {

    @JvmStatic
    fun detectCollision(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            (shape1 == null) || (shape2 == null) -> false
            (shape1 is Circle) && (shape2 is Circle) -> circleVsCircle(shape1, shape2)
            (shape1 is BoundingRectangle) && (shape2 is BoundingRectangle) -> rectVsRect(shape1, shape2)
            (shape1 is Edge) && (shape2 is Edge) -> edgeVsEdge(shape1, shape2)
            else -> detectCollisionGJK(shape1, shape2)
        }
    }

    /**
     * Performs a simple circle vs. circle intersection test by comparing the distances their centers and the sum of
     * their radii.
     *
     * @param circle1 the first circle
     * @param circle2 the second circle
     *
     * @return if the two circles intersect or touch
     */
    private fun circleVsCircle(circle1: Circle, circle2: Circle): Boolean {
        val distSq = circle1.center().distanceSq(circle2.center())
        val sumRadiiSq = MathUtils.squared(circle1.radius + circle2.radius)
        return distSq <= sumRadiiSq
    }

    /**
     * Performs a simple rectangle vs. rectangle intersection test using the separating-axis theorem on their x- and
     * y-axes.
     *
     * @param rect1 the first rectangle
     * @param rect2 the second rectangle
     *
     * @return if the two rectangles intersect or touch
     */
    private fun rectVsRect(rect1: BoundingRectangle, rect2: BoundingRectangle): Boolean {
        // Perform SAT on x-axis
        val min1 = rect1.min()
        val max1 = rect1.max()
        val min2 = rect2.min()
        val max2 = rect2.max()
        // a.min <= b.max && a.max <= b.min
        val satX = (min1.x <= max2.x) && (min2.x <= max1.x)
        val satY = (min1.y <= max2.y) && (min2.y <= max1.y)
        return satX && satY
    }

    /**
     * Performs a simple line segment intersection test.
     *
     * @param edge1 the first edge
     * @param edge2 the second edge
     *
     * @return if the two edges intersect or touch
     */
    // TODO linear systems matrix
    private fun edgeVsEdge(edge1: Edge, edge2: Edge): Boolean {
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
     * Executes the GJK collision algorithm between two shapes.
     *
     * Source: https://blog.winter.dev/2020/gjk-algorithm/ § GJK: Surrounding the origin
     */
    private fun detectCollisionGJK(shape1: Shape, shape2: Shape): Boolean {
        // Make first guess for simplex point
        var startPoint = findMinkDiffExtreme(shape1, shape2, -shape1.center())
        var searchDir = -startPoint // Point to origin

        val simplex = arrayOfNulls<Vec2>(3)
        simplex[0] = startPoint

        var numPoints = 1
        // TODO max loop
        while (numPoints < 3) {
            val nextPoint = findMinkDiffExtreme(shape1, shape2, searchDir) // Keep searching toward origin
            // TODO < or ≤?
            if (nextPoint.dot(searchDir) < 0f) return false // If opposite direction, no collision

            // Find if simplex contains origin
            val toStart = startPoint - nextPoint // origin between two points in simplex
            val toOrigin = -nextPoint

            if (toStart.dot(toOrigin) > 0f) {
                val perp = toStart.normal()
                searchDir = if (perp.dot(toOrigin) > 0f) perp else -perp // make sure perp points to toOrigin
                simplex[numPoints++] = nextPoint // update simplex
            } else {
                // Restart search
                simplex[0] = nextPoint
                startPoint = nextPoint
                searchDir = toOrigin
                numPoints = 1
            }
        }

        // 3 points, found intersection
//        println("simplex=${simplex.contentToString()}")
        return true // TODO return intersection points
    }

    /**
     * Finds the most extreme points from the Minkowski difference set between two shapes.
     *
     * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting shapes into supporting points
     */
    private fun findMinkDiffExtreme(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 {
        val pt1 = shape1.supportPoint(dir)
        val pt2 = shape2.supportPoint(-dir)
        return pt1 - pt2
    }

}