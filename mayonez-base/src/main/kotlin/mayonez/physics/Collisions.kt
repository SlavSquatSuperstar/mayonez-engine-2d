package mayonez.physics

import mayonez.math.MathUtils
import mayonez.math.shapes.*
import mayonez.physics.detection.*
import mayonez.physics.manifold.*

/**
 * Checks collisions and calculates contact points for primitive shapes. Uses
 * simple tests when possible, otherwise performs the SAT or GJK algorithms for
 * complex shapes.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    const val MAX_SAT_VERTICES: Int = 4

    private val sat = SATDetector()
    private val gjk = GJKDetector()
    private val clip = ClippingManifoldSolver()

    // Intersection Methods

    @JvmStatic
    fun checkCollision(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            (shape1 == null) || (shape2 == null) -> false
            (shape1 is Edge) && (shape2 is Edge) -> intersectEdges(shape1, shape2)
            shape1.isAABB() && shape2.isAABB() -> intersectAABBs(shape1 as Rectangle, shape2 as Rectangle)
            shape1.isSATPreferred() && shape2.isSATPreferred() -> sat.checkIntersection(
                shape1,
                shape2
            ) // Use SAT for circles simple polygons
            else -> gjk.getSimplex(shape1, shape2) != null // Use GJK for complex shapes
        }
    }

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
            // Do edge overlap
            edge1.containsEndpoint(edge2) || edge2.containsEndpoint(edge1)
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

    private fun Edge.containsEndpoint(other: Edge): Boolean {
        return this.start in other || this.end in other
    }

    /**
     * Performs a simple AABB vs. AABB intersection test using their intervals
     * on the x- and y-axes
     *
     * @param rect1 the first rectangle
     * @param rect2 the second rectangle
     * @return if the two rectangles overlap or touch
     */
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

    private fun Shape.isAABB(): Boolean {
        return this is Rectangle && this.isAxisAligned
    }

    // Contact Methods

    @JvmStatic
    fun getContacts(shape1: Shape?, shape2: Shape?): Manifold? {
        return when {
            (shape1 == null) || (shape2 == null) -> {
                null
            }

            (shape1 is Circle) && (shape2 is Circle) -> {
                CircleDetector.getContacts(shape1, shape2)
            }

            else -> {
                val pen = if (shape1.isSATPreferred() && shape2.isSATPreferred()) {
                    sat.getPenetration(shape1, shape2)
                } else {
                    gjk.getPenetration(shape1, shape2)
                }
                clip.getContacts(shape1, shape2, pen)
            }
        }
    }

    /**
     * Whether a collision involving the given shape can be efficiently
     * detected. SAT detection works best with polygons with a small
     * vertex count (boxes and triangles) and circle-polygon collisions.
     */
    private fun Shape.isSATPreferred(): Boolean {
        return when (this) {
            is Circle -> true
            is Polygon -> this.numVertices <= MAX_SAT_VERTICES
            // TODO need to test edge colliders
            else -> false
        }
    }

}