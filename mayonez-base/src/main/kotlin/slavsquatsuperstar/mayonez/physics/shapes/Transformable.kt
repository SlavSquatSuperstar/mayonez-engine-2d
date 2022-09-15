package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.Vec2

/**
 * Indicates a geometric object that supports three basic transformations: translation, rotation, and scaling.
 *
 * @author SlavSquatSuperstar
 */
interface Transformable {

    /**
     * Moves every point on this object by the same amount in the given direction. This is a rigid transformation and
     * preserves the distance between every pair of points.
     *
     * @param direction the vector to move along
     * @return the translated object
     */
    fun translate(direction: Vec2): Transformable

    /**
     * Rotates every point on this object by the same angle around the given origin. This is a rigid transformation and
     * preserves the distance between every pair of points.
     *
     *
     * @param angle  the counterclockwise angle in degrees
     * @param origin the point to rotate around, or a predetermined point if null
     * @return the rotated object
     */
    fun rotate(angle: Float, origin: Vec2? = null): Transformable

    /**
     * Stretches or shrinks every point on object by the same scale factor from the given origin. This is a non-rigid
     * transformation and will alter the distance between every pair of points. Scaling may also be a non-uniform
     * (anisotropic) transformation and scale by a separate factor in different directions.
     *
     * @param factor how much to scale the shape along each axis
     * @param origin the point to scale from, or a predetermined point if null
     * @return the scaled object
     */
    fun scale(factor: Vec2, origin: Vec2? = null): Transformable

}