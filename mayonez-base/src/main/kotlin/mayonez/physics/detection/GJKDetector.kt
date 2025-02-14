package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*

const val MAX_GJK_ITERATIONS: Int = 20


/**
 * Detects if two shapes are colliding and finds the Minkowski sum using
 * the GJK (Gilbert-Johnson-Keerthi) distance algorithm. This class
 * constructs a simplex and checks whether it contains the origin, and then
 * uses a [mayonez.physics.detection.EPASolver] to find the penetration.
 *
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
     * Executes a modified GJK (Gilbert-Johnson-Keerthi) distance algorithm
     * to determine whether two shapes overlap. This function searches for a
     * simplex of support points surrounding the origin, but will not find
     * contact points. Compared to the SAT (separating-axis theorem), GJK
     * relies only on the support functions and the Minkowski difference.
     * GJK does not need a separate algorithm to handle round shapes or loop
     * through every normal.
     *
     * Sources:
     * - [Winter's Blog](https://blog.winter.dev/2020/gjk-algorithm/) § GJK:
     *   Surrounding the origin
     * - [dyn4j](https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/) §
     *   Determining Collision
     * - [YouTube](https://youtu.be/ajv46BSqcK4)
     *
     * @return the simplex if they overlap, otherwise null
     */
    // TODO cache recent support points
    fun getSimplex(shape1: Shape?, shape2: Shape?): Simplex? {
        if (shape1 == null || shape2 == null) return null

        // Get initial search direction
        val startPt = Shape.support(shape1, shape2, shape2.center() - shape1.center())
        var searchDir = -startPt // Search toward origin to surround it
        val simplex = Simplex(startPt) // Create simplex with first point

        for (i in 1..MAX_GJK_ITERATIONS) {
            val ptA = Shape.support(shape1, shape2, searchDir) // Get new support point
            if (ptA.dot(searchDir) < 0f) return null // Continue only if next point passes origin
            simplex.add(ptA) // Add point to simplex

            searchDir = if (simplex.size == 2) {
                // Pick the normal of the line that points toward origin
                // Line: B (start), A (next), d = tripleProduct(AB, AO, AB)
                val vecAB = simplex[0] - ptA
                val vecAO = -ptA
                Vec2.tripleProduct(vecAB, vecAO, vecAB)
            } else {
                // Find if triangle contains origin
                simplex.getNextSearchDir(ptA) ?: return simplex
            }
        }
        return null // Assume no collision if looped too many times
    }

    private fun Simplex.getNextSearchDir(nextPt: Vec2): Vec2? {
        // Triangle: C (first), B (start), A (next)
        val vecAC = this[0] - nextPt
        val vecAB = this[1] - nextPt
        val vecAO = -nextPt

        // Check reach region past line AB and AC for origin
        val perpAB = Vec2.tripleProduct(vecAC, vecAB, vecAB) // tripleProduct(AC, AB, AB)
        val perpAC = Vec2.tripleProduct(vecAB, vecAC, vecAC) // tripleProduct(AB, AC, AC)

        return if (perpAB.dot(vecAO) > 0) {
            this.remove(0) // remove C
            perpAB
        } else if (perpAC.dot(vecAO) > 0) {
            this.remove(1) // remove B
            perpAC
        } else {
            null // Contains origin, found intersection
        }
    }

}