package mayonez.physics.resolution

import mayonez.math.*

/**
 * Provides additional information about a raycast against a shape.
 *
 * @author SlavSquatSuperstar
 */
// TODO save all intersections?
// TODO restricted constructor
class RaycastInfo(
    val contact: Vec2, // contact point
    normal: Vec2, // contact normal
    val distance: Float // unit lengths along ray to intersection
) {
    val normal = normal.unit()
}