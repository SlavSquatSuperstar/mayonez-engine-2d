package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.mayonez.math.MathUtils
import slavsquatsuperstar.mayonez.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Shape
import kotlin.math.abs

/**
 * Calculates the penetration between two shapes from their Minkowski sum (simplex) by performing the expanding
 * polytope/polygon algorithm. Used in conjunction with [GJKDetector].
 *
 * Sources
 * - https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm
 * - https://blog.winter.dev/2020/epa-algorithm/ § 2D (JavaScript)
 *
 * @author SlavSquatSuperstar
 */
class EPASolver(private val shape1: Shape?, private val shape2: Shape?) {

    companion object {
        const val MAX_EPA_ITERATIONS: Int = 40
    }

    /**
     * Calculate the penetration between the two shapes from their simplex.
     *
     * @param simplex the Minkowski sum of the two shapes
     * @return the penetration, or null if the intersection is too small
     */
    // TODO See § Alternatives for optimizations
    fun getPenetration(simplex: Simplex?): Penetration? {
        if (shape1 == null || shape2 == null || simplex == null) return null

        val disp = Vec2(50f, 40f)
        val expandedSimplex = simplex.expand(MAX_EPA_ITERATIONS) // sort edges prior

        for (i in 0 until MAX_EPA_ITERATIONS) {
            // 1. Find the closest face in the simplex to the origin
            val closest = closestEdgeToOrigin(expandedSimplex)

//            val poly = expandedSimplex.toPolygon().translate(disp)
//            val cEdge = poly.edges[closest.index]
//            DebugDraw.drawShape(cEdge, Colors.LIGHT_GREEN)
//            DebugDraw.drawVector(cEdge.center(), closest.norm, Colors.LIGHT_GREEN)

            // 2. Look for a point in the Minkowski sum in the direction of the face's normal
            val supp = Shape.support(shape1, shape2, closest.norm)
            val suppDist = supp.dot(closest.norm) // distance along normal is depth
            // support point already on closest edge; cannot expand simplex anymore
            if (MathUtils.equals(suppDist, closest.dist)) {
//                DebugDraw.drawPoint(supp + disp, Colors.RED)
//                DebugDraw.drawShape(poly, Colors.BLUE)
//                val edge = poly.edges[closest.index]
//                DebugDraw.drawShape(edge, Colors.GREEN)
//                DebugDraw.drawVector(edge.center(), closest.norm, Colors.RED)
                return Penetration(closest.norm, suppDist + MathUtils.FLOAT_EPSILON)
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
            val norm = vecAB.normal().unit() // outward unit normal of edge
//            val norm = Vec2.tripleProduct(vecAB, ptA, vecAB).unit() // n = (AB x OA) x AB
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

    // norm: edge normal, dist: edge distance to origin, index: which edge
    private class Face(var norm: Vec2, var dist: Float, var index: Int)

}