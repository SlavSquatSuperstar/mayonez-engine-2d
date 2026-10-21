package mayonez.math.shapes

import mayonez.Transform
import mayonez.math.*

/**
 * A geometric object that supports three basic transformations:
 * translation, rotation, and scaling.
 *
 * @author SlavSquatSuperstar
 */
interface Transformable {

    /**
     * Moves every point on this object by the same amount in the given
     * direction. This is a rigid transformation and preserves the distance
     * between points.
     *
     * @param direction the vector to move along
     * @return the translated object
     */
    fun translate(direction: Vec2): Transformable

    /**
     * Rotates every point on this object by the same angle around the given
     * origin. This is a rigid transformation and preserves the distance
     * between points.
     *
     * @param angle the counterclockwise angle in degrees
     * @param origin the point to rotate around, or a predetermined point if
     *     null
     * @return the rotated object
     */
    fun rotate(angle: Float, origin: Vec2? = null): Transformable

    /**
     * Stretches or shrinks every point on this object by the same scale factor
     * from the given origin. This is a non-rigid transformation and alters the
     * distance between points. Scaling may also be non-uniform (anisotropic)
     * if the separate factor is different along each direction.
     *
     * @param factor how much to scale the shape along each axis
     * @param origin the point to scale from, or a predetermined point if null
     * @return the scaled object
     */
    fun scale(factor: Vec2, origin: Vec2? = null): Transformable

    /**
     * Transforms every point on this object by the given [Transform], first
     * scaling, then rotating, and lastly translating.
     *
     * @param transform an affine transformation
     * @param origin the transform origin, or a predetermined point if null
     */
    fun transform(transform: Transform, origin: Vec2? = null): Transformable {
        return this.scale(transform.scale, origin)
            .rotate(transform.rotation, origin)
            .translate(transform.position)
    }

}