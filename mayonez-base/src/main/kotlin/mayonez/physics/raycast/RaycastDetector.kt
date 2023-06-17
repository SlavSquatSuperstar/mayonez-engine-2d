package mayonez.physics.raycast

import mayonez.math.shapes.*

/**
 * Casts rays onto shapes and detects if there is a hit, how far the ray
 * has traveled, and where the contact is.
 */
internal fun interface RaycastDetector<T: Shape> {

    /**
     * Casts a ray onto a shape and calculates the contact point, distance, and
     * normal.
     *
     * @param shape the shape to raycast
     * @param ray the ray to cast
     * @param limit the max length the ray can travel
     * @return the raycast information, or null if the ray misses
     */
    fun raycast(shape: T, ray: Ray, limit: Float): RaycastInfo?

}