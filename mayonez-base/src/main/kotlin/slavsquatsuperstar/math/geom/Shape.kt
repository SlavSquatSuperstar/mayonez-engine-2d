package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.Vec2

/**
 * A bounded, flat figure that exists in the 2D plane and has no intersecting edges.
 * Can also represent an infinitesimally thin cross-section of a solid (lamina).
 * Shape objects are immutable, meaning their component points and other characteristics cannot change.
 *
 * @author SlavSquatSuperstar
 */
abstract class Shape {

    // Geometric Properties

    /**
     * The area of the shape, or the amount of space it takes up in the 2D plane.
     */
    abstract fun area(): Float

    /**
     * The length of the shape's boundary.
     */
    abstract fun perimeter(): Float

    /**
     * The centroid, or geometric center of the shape, calculated by averaging all the points in the shape.
     * The centroid is also the center of mass for a shape of uniform density.
     */
    abstract fun center(): Vec2

    // Phyiscal Properties

    /**
     * The angular mass, (moment of inertia/rotational inertia) of the shape around its centroid, assuming a uniform
     * density throughout the shape.
     *
     * @param mass the mass of the shape
     */
    abstract fun angMass(mass: Float): Float

    // Geometric Transformations

    /**
     * Translates every point on this shape along the same vector.
     * This is a rigid transformation and preserves the area and perimeter.
     *
     * @param direction the direction to move
     * @return the translated shape
     */
    abstract fun translate(direction: Vec2): Shape

    /**
     * Rotates every point on this shape around the center of mass by the same angle.
     * This is a rigid transformation and preserves the area and perimeter.
     *
     * @param angle the counterclockwise angle
     * @return the rotated shape
     */
    abstract fun rotate(angle: Float): Shape

    /**
     * Stretches the dimensions of this shape by a given scale vector.
     * This is a non-linear (anisotropic) transformation and may alter the overall form and area.
     *
     * @param factor how much to scale the dimensions by
     * @return the scaled shape
     */
    open fun scale(factor: Vec2): Shape {
        throw UnsupportedOperationException("Must be a non-regular shape")
    }

    /**
     * Stretches both dimensions of this shape by the same scale factor.
     * This is a linear but not rigid transformation and maintains the overall form but not the area.
     *
     * @param factor how much to scale the shape by
     * @return the scaled shape
     */
    open fun scale(factor: Float): Shape {
        throw UnsupportedOperationException("Must be a regular shape")
    }

    // Overrides

    /**
     * Whether the specified point lies on or within the boundary of the shape.
     *
     * @param point a vector in 2D space
     * @return the scaled shape
     */
    // TODO boundary vs interior vs exterior
    abstract operator fun contains(point: Vec2): Boolean

    /**
     * Whether this shape is equivalent to another shape before applying any rigid transformations.
     */
    abstract override operator fun equals(other: Any?): Boolean

//    /**
//     * Whether this shape is geometrically congruent to another shape, or can be perfectly superimposed onto the other
//     * using translation, rotation, and reflection.
//     */
//    abstract fun congruent(other: Shape?): Boolean

    /**
     * A string representation of the shape.
     */
    abstract override fun toString(): String
}