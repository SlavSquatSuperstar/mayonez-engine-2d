package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

/**
 * Calculates the [Penetration] between two shapes using the expanding polytope
 * (polygon) algorithm (EPA). This class takes a [Simplex], which should be a
 * triangle containing the origin, typically returned from a [GJKDetector].
 * EPA expands the simplex by adding support points until the edge closest to
 * the origin also lies on the boundary of the Minkowski sum.
 *
 * Sources
 * - [dyn4j](https://dyn4j.org/2010/05/epa-expanding-polytope-algorithm)
 * - [Winter's Blog](https://blog.winter.dev/2020/epa-algorithm/) § 2D
 *   (JavaScript)
 *
 * @author SlavSquatSuperstar
 */
internal class EPASolver(
    private val maxIterations: Int = MAX_EPA_ITERATIONS,
    private val distanceMargin: Float = DISTANCE_MARGIN
) {

    companion object {
        const val MAX_EPA_ITERATIONS: Int = 20
        const val DISTANCE_MARGIN: Float = 1e-4f
    }

    /**
     * Calculate the penetration between two intersecting shapes given their
     * simplex.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @param simplex the GJK termination simplex
     * @return the penetration, or null if the intersection is too small
     */
    // TODO Augmenting simplexes
    internal fun getPenetration(shape1: Shape?, shape2: Shape?, simplex: Simplex?): Penetration? {
        if (shape1 == null || shape2 == null || simplex == null) return null
        val expandedSimplex = simplex.sorted().expand(maxIterations)

        for (i in 0..<maxIterations) {
            // Find the closest face in the simplex to the origin
            val closest = expandedSimplex.getClosestEdgeToOrigin()

            // Look for a point in the Minkowski sum in the direction of the face's normal
            val suppPt = Shape.support(shape1, shape2, closest.norm)
            val depth = suppPt.dot(closest.norm) // Support point distance along normal

            if (MathUtils.equals(depth, closest.dist, distanceMargin)) {
                // Found support point on closest edge
                return Penetration(closest.norm, depth)
            } else {
                // Insert the support point between the two endpoints and keep looking
                expandedSimplex.add(closest.index, suppPt)
            }
        }
        return null
    }

    private fun Simplex.getClosestEdgeToOrigin(): Face {
        var face = Face(Vec2(), Float.POSITIVE_INFINITY, 0)

        for (i in 0..<this.size) {
            val j = (i + 1) % this.size

            /*
             * Given: A (start), B (end), O (origin)
             * Find the outward normal of line AB, n = (AB x OA) x AB
             * Since the triple product is unreliable for small penetrations,
             * rely on a consistent winding order and use the unit normal
             */
            val ptA = this[i]
            val vecAB = Edge(ptA, this[j])
            val norm = vecAB.unitNormalRight() // Outward unit normal of edge (CCW)
            val dist = abs(ptA.dot(norm)) // Project either point onto normal
            if (dist < face.dist) face = Face(norm, dist, j)
        }
        return face
    }

    // Helper Class

    /**
     * A face made by connecting two adjacent points on a simplex.
     *
     * @param norm the outward edge normal
     * @param dist the edge distance to the origin
     * @param index the index of the edge in the simplex
     */
    private data class Face(val norm: Vec2, val dist: Float, val index: Int)

}