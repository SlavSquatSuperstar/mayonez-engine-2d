package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import slavsquatsuperstar.mayonez.physics.collision.Collisions
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Detects if two shapes are colliding and finds the Minkowski sum using the GJK distance algorithm.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
class GJKDetector(private val shape1: Shape, private val shape2: Shape) {

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
     * @return the simplex if they overlap, otherwise null
     */
    // TODO cache recent support points
    fun getSimplex(): Simplex? {
        // Search in any initial direction
        val startPt = Collisions.support(shape1, shape2, shape2.center() - shape1.center())
        var searchDir = -startPt // Search toward origin to surround it
        val simplex = Simplex(startPt) // Create simplex with first point

        for (loop in 1..Collisions.MAX_GJK_ITERATIONS) {
            val ptA = Collisions.support(shape1, shape2, searchDir) // Get new support point
            if (ptA.dot(searchDir) < 0f) return null // Continue only if next point passes origin

            simplex.add(ptA) // Add point to simplex
            if (simplex.size == 2) { // Pick the normal of the line that points toward origin
                // Line: B (start), A (next), d = tripleProduct(AB, AO, AB)
                val vecAB = startPt - ptA
                val vecAO = -ptA
                searchDir = Vec2.tripleProduct(vecAB, vecAO, vecAB)
            } else { // Find if triangle contains origin
                // Triangle: C (first), B (start), A (next)
                val vecAC = simplex[0] - ptA
                val vecAB = simplex[1] - ptA
                val vecAO = -ptA

                // Check reach region past line AB and AC for origin
                val perpAB = Vec2.tripleProduct(vecAC, vecAB, vecAB) // tripleProduct(AC, AB, AB)
                val perpAC = Vec2.tripleProduct(vecAB, vecAC, vecAC) // tripleProduct(AB, AC, AC)

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

}