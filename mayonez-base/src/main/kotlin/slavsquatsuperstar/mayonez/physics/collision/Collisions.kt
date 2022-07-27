package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.*
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * Implements collision detection algorithms using primitive shapes for the physics engine.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    // Raycasting

    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): Raycast? {
        return when {
            (shape == null) || (ray == null) -> null
            (shape is Circle) -> raycastCircle(shape, ray, limit)
            (shape is Edge) -> raycastEdge(shape, ray, limit)
            (shape is Polygon) -> raycastPolygon(shape, ray, limit)
            (shape is Rectangle) -> raycastRectangle(shape, ray, limit)
            else -> null
        }
    }

    // Source: https://youtu.be/23kTf-36Fcw
    private fun raycastCircle(circle: Circle, ray: Ray, limit: Float): Raycast? {
        // Trace the ray's origin to the circle's center
        val originToCenter = circle.center() - ray.origin

        // Project originToCenter onto the ray and find length
        // Adjacent leg of right triangle -> inverse hypotenuse for opposite leg
        val projLength = originToCenter.component(ray.direction)
        val distNearestSq =
            originToCenter.lenSq() - projLength * projLength // Closest distance from center to extended ray
        if (distNearestSq > circle.radiusSq) return null // Nearest point on ray is outside the circle

        // Find length of projected vector inside circle
        val contactToNearest = sqrt(circle.radiusSq - distNearestSq)
        // Distance along ray to contact, depends if ray starts in circle
        val hitDist = if (ray.origin in circle) projLength + contactToNearest
        else projLength - contactToNearest

        if (limit > 0f && hitDist > limit * ray.length) return null // Ray exceeds limit
        if (hitDist < 0f) return null // Contact point is behind ray

        val point = ray.getPoint(hitDist)
        return Raycast(point, point - circle.center(), hitDist / ray.length)
    }

    private fun raycastEdge(edge: Edge, ray: Ray, limit: Float): Raycast? {
        // Find line directions
        val dir1 = edge.toVector() / edge.length
        val dir2 = ray.direction / ray.length
        val cross = dir1.cross(dir2)

        // If ray is parallel, then raycast is undefined
        // Ray either misses or hits endpoint (no normal)
        if (MathUtils.equals(cross, 0f)) return null

        // Calculate intersection point
        val startToOrigin = ray.origin - edge.start
        val dist1 = startToOrigin.cross(dir2) / cross
        val dist2 = startToOrigin.cross(dir1) / cross

        // Contact must be inside edge and inside ray if limit is enabled
        if (!MathUtils.inRange(dist1, 0f, edge.length) || dist2 < 0
            || (limit > 0 && dist2 > limit * ray.length)
        )
            return null

        val contact = edge.start + (dir1 * dist1)
        val edgeNormal = edge.toVector().normal().unit()
        val normal = if (dir1.dot(edgeNormal) < 0) edgeNormal else -edgeNormal
        return Raycast(contact, normal, dist2 / ray.length)
    }

    private fun raycastPolygon(poly: Polygon, ray: Ray, limit: Float): Raycast? {
        val edges = poly.edges
        // Find raycast distance to closest edge
        val distances = FloatArray(edges.size)
        for (i in distances.indices) {
            val rc = raycastEdge(edges[i], ray, limit)
            distances[i] = rc?.distance ?: Float.POSITIVE_INFINITY
        }

        val minIndex = MathUtils.minIndex(*distances)
        val minDist = distances[minIndex]
        if (minDist == Float.POSITIVE_INFINITY) return null // no successful raycasts

        val normal = edges[minIndex].toVector().normal().unit()
        return Raycast(ray.getPoint(minDist), normal, minDist)
    }

    // Source: https://youtu.be/8JJ-4JgR7Dg
    private fun raycastRectangle(rect: Rectangle, ray: Ray, limit: Float): Raycast? {
        // Parametric distance to min/max x and y axes of box
        val tNear = (rect.min() - (ray.origin)).unsafeDivide(ray.direction)
        val tFar = (rect.max() - (ray.origin)).unsafeDivide(ray.direction)

        // Swap near and far components if they're out of order
        if (tNear.x > tFar.x) {
            val temp = tNear.x
            tNear.x = tFar.x
            tFar.x = temp
        }
        if (tNear.y > tFar.y) {
            val temp = tNear.y
            tNear.y = tFar.y
            tFar.y = temp
        }
        if (tNear.x > tFar.y || tNear.y > tFar.x) return null // No intersection

        // Parametric distances to near and far contact along ray
        val tHitNear = (tNear.x).coerceAtLeast(tNear.y)
        val tHitFar = (tFar.x).coerceAtMost(tFar.y)
        if (tHitFar < 0 || tHitNear > tHitFar) return null // Ray is pointing away

        // If ray starts inside shape, use far for contact
        val distToRect = if (tHitNear < 0) tHitFar else tHitNear

        // Contact point is past the ray limit
        if (limit > 0 && distToRect > limit) return null
        var normal = Vec2() // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = Vec2(-sign(ray.direction.x), 0f)
        else if (tNear.x < tNear.y) // Vertical collision
            normal = Vec2(0f, -sign(ray.direction.y))

        val contact = ray.getPoint(distToRect)
        return Raycast(contact, normal, contact.distance(ray.origin))
    }

    private fun Vec2.unsafeDivide(v: Vec2): Vec2 {
        return Vec2(this.x / v.x, this.y / v.y)
    }

    // Collision Detection

    @JvmStatic
    fun detectCollision(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            (shape1 == null) || (shape2 == null) -> false
            (shape1 is Circle) && (shape2 is Circle) -> collisionCircles(shape1, shape2)
            (shape1 is Edge) && (shape2 is Edge) -> collisionEdges(shape1, shape2)
            (shape1 is Rectangle) && (shape2 is Rectangle) -> collisionRects(shape1, shape2)
            else -> detectCollisionGJK(shape1, shape2) != null
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
    private fun collisionCircles(circle1: Circle, circle2: Circle): Boolean {
        val distSq = circle1.center().distanceSq(circle2.center())
        val sumRadiiSq = MathUtils.squared(circle1.radius + circle2.radius)
        return distSq <= sumRadiiSq
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
    private fun collisionEdges(edge1: Edge, edge2: Edge): Boolean {
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
     * @return if the two rectangles intersect or touch
     */
    private fun collisionRects(rect1: Rectangle, rect2: Rectangle): Boolean {
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

    /*
     * Collision Algorithms
     * - SAT (separating axis theorem): easier to visually understand, but needs to loop through and project on all
     * normals of two shapes (slow on 3D) and requires separate tests for round shapes.
     * - GJK (Gilbert-Johnson-Keerthi): relies only on support function (can handle round shapes), needs another
     * function to find contacts, may be harder to understand
     *
     * GJK Outline: Create a simplex from the Minkowski difference that surrounds the origin
     */
    /**
     * Executes the GJK collision algorithm between two shapes.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return a simplex surrounding the origin if the shapes overlap, or otherwise null
     *
     * Source: https://blog.winter.dev/2020/gjk-algorithm/ § GJK: Surrounding the origin
     * Source: https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/ § Determining Collision
     * Source: https://youtu.be/ajv46BSqcK4
     */
    // TODO cache recent support points
    private fun detectCollisionGJK(shape1: Shape, shape2: Shape): Simplex? {
        // Search in any initial direction
        val startPt = support(shape1, shape2, shape2.center() - shape1.center())
        var searchDir = -startPt // Search toward origin to surround it
        val simplex = Simplex(startPt) // Create simplex with first point

        val maxLoop = 10 // TODO calculate max loop
        for (loop in 1..maxLoop) {
            val ptA = support(shape1, shape2, searchDir) // Get new support point
            if (ptA.dot(searchDir) < 0f) return null // Continue only if next point passes origin

            simplex.add(ptA) // Add point to simplex
            if (simplex.size == 2) { // Pick the normal of the line that points toward origin
                // Line: B (start), A (next), d = tripleProduct(AB, AO, AB)
                val vecAB = startPt - ptA
                val vecAO = -ptA
                searchDir = vecAB.tripleProduct(vecAO, vecAB)
            } else { // Find if triangle contains origin
                // Triangle: C (first), B (start), A (next)
                val vecAC = simplex[0] - ptA
                val vecAB = simplex[1] - ptA
                val vecAO = -ptA

                // Check reach region past line AB and AC for origin
                val perpAB = vecAC.tripleProduct(vecAB, vecAB) // tripleProduct(AC, AB, AB)
                val perpAC = vecAB.tripleProduct(vecAC, vecAC) // tripleProduct(AB, AC, AC)

                if (perpAB.dot(vecAO) > 0) {
                    simplex.remove(0) // remove C
                    searchDir = perpAB
                } else if (perpAC.dot(vecAO) > 0) {
                    simplex.remove(1) // remove B
                    searchDir = perpAC
                } else {
                    return simplex // contains origin, found intersection
                }
            }
        }

//        println("simplex=${simplex.contentToString()}")
//        val newSimplex = arrayOf(simplex[0]!!, simplex[1]!!, simplex[2]!!)
//        val pt1 = simplex[0] + Vec2(20f)
//        val pt2 = simplex[1] + Vec2(20f)
//        val pt3 = simplex[2] + Vec2(20f)
//        DebugDraw.drawLine(pt1, pt2, Colors.BLACK)
//        DebugDraw.drawLine(pt2, pt3, Colors.BLACK)
//        DebugDraw.drawLine(pt3, pt1, Colors.BLACK)

        return null // Assume no collision if looped too many times
    }

    /**
     * Finds the most extreme points from the Minkowski difference set between two shapes.
     *
     * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting shapes into supporting points
     */
    private fun support(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 =
        shape1.supportPoint(dir) - shape2.supportPoint(-dir)

}