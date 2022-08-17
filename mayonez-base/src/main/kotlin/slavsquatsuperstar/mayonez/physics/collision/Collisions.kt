package slavsquatsuperstar.mayonez.physics.collision

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.math.Vec2.Companion.tripleProduct
import slavsquatsuperstar.mayonez.Colors
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import slavsquatsuperstar.mayonez.physics.detection.*
import slavsquatsuperstar.mayonez.physics.shapes.Polygon
import slavsquatsuperstar.mayonez.physics.shapes.Ray
import slavsquatsuperstar.mayonez.physics.shapes.Shape
import kotlin.math.abs

/**
 * Implements collision detection algorithms using primitive shapes for the physics engine.
 *
 * @author SlavSquatSuperstar
 */
object Collisions {

    const val MAX_GJK_ITERATIONS: Int = 20
    const val MAX_EPA_ITERATIONS: Int = 40

    // Raycast Methods

    /**
     * Casts a ray onto a shape and returns the contact information
     *
     * @param shape the shape to raycast
     * @param ray   the ray to cast
     * @param limit the max length the ray can travel
     * @return data containing the distance, contact point, and surface normal, or null if the ray misses
     */
    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): RaycastInfo? = RaycastDetector(shape, ray, limit).detect()

    // Collision Detection

    @JvmStatic
    fun detectCollision(shape1: Shape?, shape2: Shape?): Boolean = IntersectionDetector(shape1, shape2).detect()

    @JvmStatic
    fun getCollisionInfo(shape1: Shape?, shape2: Shape?): CollisionInfo ?{
        if (shape1 == null || shape2 == null) return null
        val simplex = GJKDetector(shape1, shape2).getSimplex() ?: return null
        return SATDetector(shape1, shape2).detect()
//        val contact = getEPAPenetration(shape1, shape2, simplex) ?: return
//        println(contact.depth)
    }

    // GJK/EPA Methods

    /**
     * Performs the expanding polytope (polygon) algorithm on a GJK simplex to find the contact point between two
     * shapes.
     *
     * Source: https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm
     */
    @ExperimentalFeature
    // See § Alternatives for optimizations
    private fun getEPAPenetration(shape1: Shape, shape2: Shape, simplex: Simplex): Penetration? {
        val disp = Vec2(50f, 40f)
        val poly = Polygon(*simplex.points.toTypedArray()).translate(disp)
        DebugDraw.drawShape(poly, Colors.BLACK)

        val expandedSimplex = simplex.expand(MAX_EPA_ITERATIONS) // sort edges prior

        /*
        * Loop while true
        * Find closest edge
        * Support point for edge normal
        * Find component of point on normal (depth)
        * If (depth) roughly equals edge length, success
        * Else insert point into simplex and retry
        */
        for (i in 0 until MAX_EPA_ITERATIONS) {
//            DebugDraw.drawShape(Polygon(*expandedSimplex.points.toTypedArray()).translate(disp), Colors.DARK_GRAY)
//            expandedSimplex.points.forEach { DebugDraw.drawPoint(it + disp, Colors.RED) }
            val closest = closestEdgeToOrigin(expandedSimplex) // Find closest edge

            val color = when (i) {
                0 -> Colors.RED
                1 -> Colors.ORANGE
                2 -> Colors.YELLOW
                3 -> Colors.LIGHT_GREEN
                else -> Colors.BLACK
            }
            if (i >= 3) break
            val edge = poly.edges[closest.index]
            DebugDraw.drawShape(edge, color)
            DebugDraw.drawVector(edge.center(), closest.norm, color)

            val supp = support(shape1, shape2, closest.norm)
            val projDist = supp.dot(closest.norm) // distance along normal is depth
            // cannot expand simplex any more
            if (MathUtils.equals(projDist, closest.dist)) return Penetration(closest.norm, projDist)
//            else expandedSimplex[edge.index] = contact
//            else expandedSimplex.insert(supp, edge.index)
            else expandedSimplex.add(supp)
        }
        return null
    }

    private fun closestEdgeToOrigin(simplex: Simplex): Face {
        val face = Face(Vec2(), Float.POSITIVE_INFINITY, 0)
        for (i in 0 until simplex.size) {
            val j = (i + 1) % simplex.size

            val ptA = simplex[i]
            val vecAB = simplex[j] - ptA // vector AB, the edge
            // don't use triple product for small penetrations
            // point left for counterclockwise "winding"
            // TODO to or ƒrom depends on winding
//            val norm = vecEdge.normal().unit()
            // unit normal of edge towards origin
            val norm = tripleProduct(vecAB, -ptA, vecAB).unit() // n = (AB x OA) x AB
            val dist = abs(ptA.dot(norm)) // project any point on normal

            if (dist < face.dist) {
                face.dist = dist
                face.norm = norm
                face.index = i
            }
        }
        return face
    }

    // GJK/EPA Helpers

    // norm: edge normal, dist: edge distance to origin, index: which edge
    private class Face(var norm: Vec2, var dist: Float, var index: Int) {}

    private class Penetration(var norm: Vec2, var depth: Float) {}

    /**
     * Finds the most extreme points from the Minkowski difference set between two shapes.
     *
     * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting shapes into supporting points
     */
    // TODO move to different class
    fun support(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 =
        shape1.supportPoint(dir) - shape2.supportPoint(-dir)

}