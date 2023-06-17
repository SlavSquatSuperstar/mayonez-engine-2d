package mayonez.physics.detection

import mayonez.math.shapes.*

/**
 * Detects whether two shapes are intersecting. Checking for intersections
 * is the first step in collision detection.
 *
 * @author SlavSquatSuperstar
 */
internal fun interface CollisionDetector {
    /**
     * Determines whether two shapes are touching or overlapping.
     *
     * @param shape1 the first shape
     * @param shape2 the second shape
     * @return if the two shapes intersect
     */
    fun checkIntersection(shape1: Shape?, shape2: Shape?): Boolean
}