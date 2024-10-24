package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

/**
 * Calculates the penetration between two shapes from their Minkowski sum
 * (simplex) by performing the expanding polytope/polygon algorithm (EPA).
 * Used by [mayonez.physics.detection.GJKDetector].
 *
 * Sources
 * - [dyn4j](https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm)
 * - [Winter's Blog](https://blog.winter.dev/2020/epa-algorithm/) § 2D
 *   (JavaScript)
 *
 * @author SlavSquatSuperstar
 */
internal class EPASolver(private val shape1: Shape?, private val shape2: Shape?) {

    companion object {
        private const val MAX_EPA_ITERATIONS: Int = 40
    }

    /**
     * Calculate the penetration between the two shapes from their simplex.
     *
     * @param simplex the Minkowski sum of the two shapes
     * @return the penetration, or null if the intersection is too small
     */
    // TODO See § Alternatives for optimizations
    internal fun getPenetration(simplex: Simplex?): Penetration? {
        if (shape1 == null || shape2 == null || simplex == null) return null
        val expandedSimplex = simplex.expand(MAX_EPA_ITERATIONS) // sort edges prior

        for (i in 0..<MAX_EPA_ITERATIONS) {
            // 1. Find the closest face in the simplex to the origin
            val closest = expandedSimplex.getClosestEdgeToOrigin()

            // 2. Look for a point in the Minkowski sum in the direction of the face's normal
            val supp = Shape.support(shape1, shape2, closest.norm)
            val suppPen = expandedSimplex.findPointPenetration(supp, closest)
            if (suppPen != null) return suppPen
        }
        return null
    }

    private fun Simplex.getClosestEdgeToOrigin(): Face {
        var face = Face(Vec2(), Float.POSITIVE_INFINITY, 0)

        for (i in 0..<this.size) {
            val j = (i + 1) % this.size

            val ptA = this[i]
            val vecAB = this[j] - ptA // vector AB, the edge
            /*
             * TODO check winding of vertices instead, triple product, unreliable for small penetrations
             * point right for counterclockwise winding and v.v.
             */
            val norm = vecAB.normal().unit() // outward unit normal of edge
//            val norm = Vec2.tripleProduct(vecAB, ptA, vecAB).unit() // n = (AB x OA) x AB
            val dist = abs(ptA.dot(norm)) // project any point on normal
            if (dist < face.dist) face = Face(norm, dist, j)
        }
        return face
    }

    private fun Simplex.findPointPenetration(supp: Vec2, closest: Face): Penetration? {
        val suppDist = supp.dot(closest.norm) // distance along normal is depth

        return if (supportPointOnClosestEdge(suppDist, closest)) {
            // cannot expand simplex anymore
            Penetration(closest.norm, suppDist + MathUtils.FLOAT_EPSILON)
        } else {
            // keep looking
            this.add(closest.index, supp)
            null
        }
    }

    private fun supportPointOnClosestEdge(suppDist: Float, closest: Face): Boolean {
        return MathUtils.equals(suppDist, closest.dist)
    }

    // Helper Class

    /**
     * A face made by connecting two adjacent points from a simplex.
     *
     * @param norm the outward edge normal
     * @param dist the edge distance to the origin
     * @param index the index of the edge in the simplex
     */
    private data class Face(val norm: Vec2, val dist: Float, val index: Int)

}