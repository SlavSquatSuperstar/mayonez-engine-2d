package slavsquatsuperstar.mayonez.physics2d.collision

import slavsquatsuperstar.math.Vec2

/**
 * Provides additional information about a raycast if passed into [Collider2D].raycast().
 *
 * @author SlavSquatSuperstar
 */
// TODO save all intersections?
class RaycastResult(
    val contact: Vec2, // contact point
    normal: Vec2, // contact normal
    val distance: Float // unit lengths along ray to intersection
) {
    val normal = normal.unit()
}