package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.physics.collision.Collisions
import slavsquatsuperstar.mayonez.physics.shapes.Shape
import kotlin.math.abs

/**
 * Takes a GJK simplex and calculates contact points between two shapes using EPA.
 *
 * @author SlavSquatSuperstar
 */
class EPADetector(private val shape1: Shape, private val shape2: Shape, private val simplex: Simplex) {

    /**
     * Performs the expanding polytope (polygon) algorithm on a GJK simplex to find the contact point between two
     * shapes.
     *
     * Sources
     * - https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm
     * - https://blog.winter.dev/2020/epa-algorithm/ § 2D (JavaScript)
     */
    @ExperimentalFeature
    // TODO See § Alternatives for optimizations
    fun getPenetration(): PenetrationSolver? { // need additional work for circles
        /*
         * Outline
         * - Find the closest face in the simplex to the origin
         * - Look for a point in the Minkowski sum in the direction of the face's normal
         */

        val disp = Vec2(50f, 40f)
        val expandedSimplex = simplex.expand(Collisions.MAX_EPA_ITERATIONS) // sort edges prior

        for (i in 0 until Collisions.MAX_EPA_ITERATIONS) {
            val closest = closestEdgeToOrigin(expandedSimplex)
            val poly = expandedSimplex.toPolygon().translate(disp)
//            val cEdge = poly.edges[closest.index]
//            DebugDraw.drawShape(cEdge, Colors.LIGHT_GREEN)
//            DebugDraw.drawVector(cEdge.center(), closest.norm, Colors.LIGHT_GREEN)

            val supp = Collisions.support(shape1, shape2, closest.norm)
            val suppDist = supp.dot(closest.norm) // distance along normal is depth
            // support point already on closest edge; cannot expand simplex anymore
            if (MathUtils.equals(suppDist, closest.dist)) {
//                DebugDraw.drawPoint(supp + disp, Colors.RED)
//                DebugDraw.drawShape(poly, Colors.BLUE)
//                val edge = poly.edges[closest.index]
//                DebugDraw.drawShape(edge, Colors.GREEN)
//                DebugDraw.drawVector(edge.center(), closest.norm, Colors.RED)
                return PenetrationSolver(shape1, shape2, closest.norm, suppDist + MathUtils.FLOAT_EPSILON)
            } else expandedSimplex.add(closest.index, supp)
        }
        return null
    }

    private fun closestEdgeToOrigin(simplex: Simplex): Face {
        val face = Face(Vec2(), Float.POSITIVE_INFINITY, 0)
        for (i in 0 until simplex.size) {
            val j = (i + 1) % simplex.size

            val ptA = simplex[i]
            val vecAB = simplex[j] - ptA // vector AB, the edge
            /*
             * TODO check winding of vertices instead of triple product
             * triple product is unreliable for small penetrations
             * point right for counterclockwise winding and v.v.
             */
//            val norm = vecAB.normal().unit() // outward unit normal of edge
            val norm = Vec2.tripleProduct(vecAB, ptA, vecAB).unit() // n = (AB x OA) x AB
            val dist = abs(ptA.dot(norm)) // project any point on normal

            if (dist < face.dist) {
                face.dist = dist
                face.norm = norm
                face.index = j
            }
        }
        return face
    }

    //  Helpers
    // TODO make private

    // norm: edge normal, dist: edge distance to origin, index: which edge
    class Face(var norm: Vec2, var dist: Float, var index: Int)

}