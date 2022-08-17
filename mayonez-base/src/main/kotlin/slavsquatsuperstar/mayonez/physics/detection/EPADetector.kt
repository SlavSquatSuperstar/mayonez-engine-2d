package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Colors
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.graphics.DebugDraw
import slavsquatsuperstar.mayonez.physics.collision.Collisions
import slavsquatsuperstar.mayonez.physics.shapes.Polygon
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
     * Source: https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm
     */
    @ExperimentalFeature
    // See § Alternatives for optimizations
    fun getPenetration(): Penetration? {
        val disp = Vec2(50f, 40f)
        val poly = Polygon(*simplex.points.toTypedArray()).translate(disp)
        DebugDraw.drawShape(poly, Colors.BLACK)

        val expandedSimplex = simplex.expand(Collisions.MAX_EPA_ITERATIONS) // sort edges prior

        /*
        * Loop while true
        * Find closest edge
        * Support point for edge normal
        * Find component of point on normal (depth)
        * If (depth) roughly equals edge length, success
        * Else insert point into simplex and retry
        */
        for (i in 0 until Collisions.MAX_EPA_ITERATIONS) {
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

            val supp = Collisions.support(shape1, shape2, closest.norm)
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
            val norm = Vec2.tripleProduct(vecAB, -ptA, vecAB).unit() // n = (AB x OA) x AB
            val dist = abs(ptA.dot(norm)) // project any point on normal

            if (dist < face.dist) {
                face.dist = dist
                face.norm = norm
                face.index = i
            }
        }
        return face
    }

    //  Helpers
    // TODO make private

    // norm: edge normal, dist: edge distance to origin, index: which edge
    class Face(var norm: Vec2, var dist: Float, var index: Int)

    class Penetration(var norm: Vec2, var depth: Float)

}