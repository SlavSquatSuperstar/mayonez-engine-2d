package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.math.Vec2.Companion.tripleProduct
import slavsquatsuperstar.mayonez.Colors
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import slavsquatsuperstar.mayonez.physics.shapes.*
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * Implements collision detection algorithms using primitive shapes for the physics engine.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    const val maxGJKIterations: Int = 20
    const val maxEPAIterations: Int = 40

    // Raycasting

    /**
     * Casts a ray onto a shape and returns the contact information
     *
     * @param shape the shape to raycast
     * @param ray   the ray to cast
     * @param limit the max length the ray can travel
     * @return data containing the distance, contact point, and surface normal, or null if the ray misses
     */
    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): Raycast? {
        return when {
            (shape == null) || (ray == null) -> null
            (shape is Edge) -> raycastEdge(shape, ray, limit)
            (shape is Circle) -> raycastCircle(shape, ray, limit)
            (shape is Rectangle) -> raycastRectangle(shape, ray, limit)
            (shape is Polygon) -> raycastPolygon(shape, ray, limit)
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
        return Raycast(contact, edge.unitNormal(dir1), dist2 / ray.length)
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

        val normal = edges[minIndex].unitNormal()
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
        return Raycast(contact, normal, contact.distance(ray.origin) / ray.length)
    }

    private fun Vec2.unsafeDivide(v: Vec2): Vec2 {
        return Vec2(this.x / v.x, this.y / v.y)
    }

    // Collision Detection

    @JvmStatic
    fun detectCollision(shape1: Shape?, shape2: Shape?): Boolean {
        return when {
            (shape1 == null) || (shape2 == null) -> false
            (shape1 is Edge) && (shape2 is Edge) -> intersectEdges(shape1, shape2)
            (shape1 is Circle) && (shape2 is Circle) -> collideCircles(shape1, shape2)
            (shape1 is Rectangle) && (shape2 is Rectangle) -> collideRects(shape1, shape2)
            else -> getGJKSimplex(shape1, shape2) != null
        }
    }

    @JvmStatic
    fun getCollisionInfo(shape1: Shape?, shape2: Shape?) {
        if (shape1 == null || shape2 == null) return
        val simplex = getGJKSimplex(shape1, shape2) ?: return
        val contact = getEPAPenetration(shape1, shape2, simplex) ?: return
//        println(contact.depth)
//        DebugDraw.drawPoint(contact.contact, Colors.RED)
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
    private fun collideCircles(circle1: Circle, circle2: Circle): Boolean {
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
    private fun collideRects(rect1: Rectangle, rect2: Rectangle): Boolean {
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
     * Executes a modified GJK (Gilbert-Johnson-Keerthi) distance algorithm to determine whether two shapes overlap.
     * This function searches for a simplex of support points surrounding the origin, but will not find contact points.
     * Compared to the SAT (separating-axis theorem), GJK relies only on the support functions and the Minkowski
     * difference. GJK does not need a separate algorithm to handle round shapes or loop through every normal.
     *
     * Sources:
     * - https://blog.winter.dev/2020/gjk-algorithm/ § GJK: Surrounding the origin
     * - https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/ § Determining Collision
     * - https://youtu.be/ajv46BSqcK4
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return the simplex if the overlap, otherwise null
     */
    // TODO cache recent support points
    private fun getGJKSimplex(shape1: Shape, shape2: Shape): Simplex? {
        // Search in any initial direction
        val startPt = support(shape1, shape2, shape2.center() - shape1.center())
        var searchDir = -startPt // Search toward origin to surround it
        val simplex = Simplex(startPt) // Create simplex with first point

        for (loop in 1..maxGJKIterations) {
            val ptA = support(shape1, shape2, searchDir) // Get new support point
            if (ptA.dot(searchDir) < 0f) return null // Continue only if next point passes origin

            simplex.add(ptA) // Add point to simplex
            if (simplex.size == 2) { // Pick the normal of the line that points toward origin
                // Line: B (start), A (next), d = tripleProduct(AB, AO, AB)
                val vecAB = startPt - ptA
                val vecAO = -ptA
                searchDir = tripleProduct(vecAB, vecAO, vecAB)
            } else { // Find if triangle contains origin
                // Triangle: C (first), B (start), A (next)
                val vecAC = simplex[0] - ptA
                val vecAB = simplex[1] - ptA
                val vecAO = -ptA

                // Check reach region past line AB and AC for origin
                val perpAB = tripleProduct(vecAC, vecAB, vecAB) // tripleProduct(AC, AB, AB)
                val perpAC = tripleProduct(vecAB, vecAC, vecAC) // tripleProduct(AB, AC, AC)

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

        return null // Assume no collision if looped too many times
    }

    /**
     * Performs the expanding polytope (polygon) algorithm on a GJK simplex to find the contact point between two
     * shapes.
     *
     * Source: https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm
     */
    @ExperimentalFeature
    // See § Alternatives for optimizations
    private fun getEPAPenetration(shape1: Shape, shape2: Shape, simplex: Simplex): Penetration? {
        val poly = Polygon(*simplex.points.toTypedArray())
        DebugDraw.drawShape(poly.translate(Vec2(50f, 40f)), Colors.BLACK);

        val expandedSimplex = simplex.expand(maxEPAIterations)

        /*
        * Loop while true
        * Find closest edge
        * Support point for edge normal
        * Find component of point on normal (depth)
        * If (depth) roughly equals edge length, success
        * Else insert point into simplex and retry
        */
        for (i in 0 until maxEPAIterations) {
            val edge = closestEdgeToOrigin(expandedSimplex) // Find closest edge
            val contact = support(shape1, shape2, edge.norm)
            val dist = contact.dot(edge.norm) // distance along normal is depth
            // cannot expand simplex any more
            if (MathUtils.equals(dist, edge.dist)) return Penetration(contact, edge.norm, edge.dist)
            else expandedSimplex[edge.index] = contact
        }
        return null
    }

    private fun closestEdgeToOrigin(simplex: Simplex): Face {
        val face = Face(Vec2(), Float.POSITIVE_INFINITY, 0)
        for (i in 0 until simplex.size) {
            val j = (i + 1) % simplex.size

            val ptA = simplex[i]
            val vecEdge = simplex[j] - ptA // vector AB
            // don't use triple product for small penetrations
            // point left for counterclockwise "winding"
//            val norm = vecEdge.normal().unit()
            val norm = tripleProduct(vecEdge, -ptA, vecEdge).unit() // unit normal of edge pointing to origin
            val dist = norm.dot(ptA)

            if (dist < face.dist) {
                face.dist = dist
                face.norm = norm
                face.index = i
            }
        }
        return face
    }

    // Helper Functions

    // norm: edge normal, dist: edge distance to origin, index: which edge
    private class Face(var norm: Vec2, var dist: Float, var index: Int) {}

    private class Penetration(var contact: Vec2, var norm: Vec2, var depth: Float) {}

    /**
     * Finds the most extreme points from the Minkowski difference set between two shapes.
     *
     * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting shapes into supporting points
     */
    private fun support(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 =
        shape1.supportPoint(dir) - shape2.supportPoint(-dir)

}