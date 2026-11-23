package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*

const val MAX_GJK_ITERATIONS: Int = 20


/**
 * Detects if two shapes overlap using a modified GJK (Gilbert-Johnson-Keerthi)
 * distance algorithm. This class uses an [EPASolver] to find the penetration
 * if the intersection test succeeds. The GJK method attempts to construct a
 * [Simplex] (triangle) surrounding the origin. This class only works on convex
 * shapes.
 *
 * Compared to the [SATDetector] class, GJK relies only on support functions
 * (see [Shape.supportPoint]) and the Minkowski sum (technically using the
 * difference operator). GJK does not need a separate algorithm to handle round
 * shapes or loop through every normal.
 *
 * Sources:
 * - [dyn4j](https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/) §
 * Determining Collision
 * - [Winter's Blog](https://blog.winter.dev/2020/gjk-algorithm/) § GJK:
 * Surrounding the origin
 * - [YouTube: Reducible](https://youtu.be/ajv46BSqcK4)
 * @author SlavSquatSuperstar
 */
internal class GJKDetector : CollisionDetector<Shape>, PenetrationSolver {

    override fun checkIntersection(shape1: Shape?, shape2: Shape?): Boolean {
        return getSimplex(shape1, shape2) != null
    }

    override fun getPenetration(shape1: Shape?, shape2: Shape?): Penetration? {
        return EPASolver(shape1, shape2).getPenetration(getSimplex(shape1, shape2))
    }

    /**
     * Finds the simplex between two convex shapes. This method does not solve
     * for the penetration or contact points; a [PenetrationSolver] or
     * [mayonez.physics.manifold.ContactSolver] is required.
     *
     * @param shape1 the first shape, must be convex
     * @param shape2 the second shape, must be convex
     * @return the simplex if they overlap, otherwise null
     */
    // TODO cache recent support points
    fun getSimplex(shape1: Shape?, shape2: Shape?): Simplex? {
        if (shape1 == null || shape2 == null) return null

        // Get initial support point and search direction
        val startDir = shape2.center() - shape1.center() // Chose any initial axis
        val startPt = Shape.support(shape1, shape2, startDir)
        val simplex = Simplex(startPt) // Create simplex with first point

        var searchDir = -startPt // Search toward origin to surround it

        for (i in 1..MAX_GJK_ITERATIONS) {
            val currPt = Shape.support(shape1, shape2, searchDir) // Get next support point
            if (currPt.dot(searchDir) < 0f) return null // Continue only if next point passes origin
            simplex.add(currPt) // Add point to simplex
            searchDir = simplex.getNextSearchDir(currPt) ?: return simplex
        }
        return null // Assume no collision if looped too many times
    }

    private fun Simplex.getNextSearchDir(currPt: Vec2): Vec2? {
        if (this.size == 2) {
            /*
             * Add the third point to the simplex
             * Given: A (currPt), B (prevPt), O (origin)
             * Pick the normal d of the line AB that points toward O
             */
            val vecAB = this[0] - currPt
            val vecAO = -currPt
            return if (MathUtils.equals(vecAB.cross(vecAO), 0f)) {
                // Origin is on the line AB; use any normal
                vecAB.normal()
            } else {
                Vec2.tripleProduct(vecAB, vecAO, vecAB) // (AB × AO) × AB
            }
        } else {
            /*
             * Check if the simplex contains the origin, else add the next point
             * Given: A (currPt), B (prevPt), C (firstPt), O (origin)
             * Check the regions outside lines AB and AC for O
             * Find the edge closest to O and discard the point farthest from O
             */
            val vecAC = this[0] - currPt
            val vecAB = this[1] - currPt
            val vecAO = -currPt

            val normAB = Vec2.tripleProduct(vecAC, vecAB, vecAB) // (AC × AB) × AB
            val normAC = Vec2.tripleProduct(vecAB, vecAC, vecAC) // (AB × AC) × AC

            return if (normAB.dot(vecAO) > 0) {
                // C is farthest and AB is closest
                this.remove(0)
                normAB
            } else if (normAC.dot(vecAO) > 0) {
                // B is farthest and AC is closest
                this.remove(1)
                normAC
            } else {
                null // Triangle contains origin, found intersection
            }
        }
    }

}