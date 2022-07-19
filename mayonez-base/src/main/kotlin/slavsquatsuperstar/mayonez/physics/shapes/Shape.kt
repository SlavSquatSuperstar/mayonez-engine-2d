package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.Vec2

/**
 * A bounded, flat figure that exists in the 2D plane and has no intersecting edges.
 * Can also represent an infinitesimally thin cross-section of a solid (lamina).
 * Shape objects are immutable, meaning their component points and other characteristics cannot change.
 *
 * @author SlavSquatSuperstar
 */
abstract class Shape {

    companion object {
        /**
         * Scales a single vertex from a center.
         *
         * @param factor the scale factor
         * @param center the center for scaling
         * @return the scaled vertex
         */
        internal fun Vec2.scale(factor: Vec2, center: Vec2): Vec2 = (factor * (this - center)) + center
    }

    // Geometric Properties

    /**
     * The area of the shape, or the amount of space it takes up in the 2D plane.
     */
    abstract fun area(): Float

    /**
     * The centroid, or geometric center of the shape, calculated by averaging all the points in the shape.
     * The centroid is also the center of mass for a shape of uniform density.
     */
    abstract fun center(): Vec2

    // Collision Properties

    /**
     * The bounding circle (sphere) of a shape is the smallest circle that contains all points in the shape and shares
     * the same centroid. Useful in broad-phase collision detection since circle collision checks are simpler.
     */
    abstract fun boundingCircle(): Circle

    /**
     * The axis-aligned minimum bounding rectangle (AABB) of a shape is the smallest rectangle that contains all points
     * in the shape and is aligned with the cartesian axes. The rectangle may not share the shape's centroid.
     */
    abstract fun boundingRectangle(): BoundingRectangle

    /**
     * The support point in any direction is the furthest point on a shape towards that direction. Used by the GJK
     * collision algorithm.
     */
    abstract fun supportPoint(direction: Vec2): Vec2

    // Physical Properties

    /**
     * The mass (translational inertia) of the shape, assuming a uniform density throughout the shape.
     *
     * @param density the area density (mass per area) of the shape
     */
    fun mass(density: Float): Float = density * area()

    /**
     * The angular mass (rotational inertia) of the shape around its centroid, I_z, assuming a uniform density throughout
     * the shape. This function specifically calculates the polar moment of area of a lamina (thin object equivalent
     * to a 2D shape). While the second moment of area has units of L^4, multiplying it by area density (m/L^2) gives it
     * the same units as moment of inertia (mL^2).
     *
     *
     * @param mass the mass of the shape
     */
    abstract fun angularMass(mass: Float): Float

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
     * Rotates every point on this shape around the given origin by the same angle.
     * This is a rigid transformation and preserves the area and perimeter.
     *
     * @param angle the counterclockwise angle
     * @param origin The point to rotate around. Pass in null to rotate around the centroid.
     * @return the rotated shape
     */
    abstract fun rotate(angle: Float, origin: Vec2? = null): Shape

    /**
     * Stretches the dimensions of this shape by a given scale vector. This is a non-rigid and non-linear (anisotropic)
     * transformation and may alter the overall form and area. Some shapes may only be transformed linearly,
     * where both dimensions will be scaled by a single scale factor and the overall form is maintained.
     *
     * @param factor how much to scale the dimensions by
     * @param origin The point to rotate around. Pass in null to rotate around the centroid.
     * @return the scaled shape
     */
    // TODO change centered to origin
    abstract fun scale(factor: Vec2, origin: Vec2? = null): Shape

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

    /**
     * A string representation of the shape.
     */
    abstract override fun toString(): String
}