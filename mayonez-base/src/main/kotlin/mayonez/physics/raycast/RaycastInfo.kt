package mayonez.physics.raycast

import mayonez.math.*

/**
 * Provides additional information about a raycast against a shape.
 *
 * @author SlavSquatSuperstar
 */
// TODO save all intersections
data class RaycastInfo(
    /** The intersection point of the raycast. */
    val contact: Vec2,
    /** The contact normal, facing out of the shape. */
    val normal: Vec2,
    /** The length along the ray to the contact. */
    val distance: Float
) {
    companion object {
        /** Creates a [RaycastInfo] object and normalizes the normal vector. */
        fun createNormalized(
            contact: Vec2, normal: Vec2, distance: Float
        ): RaycastInfo {
            return RaycastInfo(contact, normal.unit(), distance)
        }
    }
}