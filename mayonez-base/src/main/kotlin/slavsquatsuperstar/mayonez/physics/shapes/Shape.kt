package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.Vec2

/**
 * A bounded, flat figure that exists in the 2D plane and has no intersecting edges. Can also represent an
 * infinitesimally thin cross-section of a solid (lamina). Shape objects are immutable, meaning their component points
 * and other characteristics cannot change.
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

        /**
         * Finds the most extreme points from the Minkowski difference set between two shapes.
         *
         * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting shapes into supporting points
         */
        fun support(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 =
            shape1.supportPoint(dir) - shape2.supportPoint(-dir)
    }

    // Geometric Properties

    /**
     * The number of vertices in this shape. Round shapes have zero.
     */
    abstract val numVertices: Int

    /**
     * The area of the shape, or the amount of space it takes up in the 2D plane.
     */
    abstract fun area(): Float

    /**
     * The centroid, or geometric center of the shape, calculated by averaging all the points in the shape.
     * The centroid is also the center of mass for a shape of uniform density.
     */
    abstract fun center(): Vec2 // TODO move to physical?

    // Min Bounds Methods

    /**
     * The bounding circle (sphere) of a shape is the smallest circle that contains all points in the shape and shares
     * the same centroid. Useful in broad-phase collision detection since circle collision checks are simpler.
     */
    abstract fun boundingCircle(): Circle

    /**
     * The axis-aligned minimum bounding rectangle (AABB) of a shape is the smallest rectangle that contains all points
     * in the shape and is aligned with the cartesian axes. The rectangle may not share the shape's centroid.
     */
    abstract fun boundingRectangle(): BoundingBox

    // Shape vs Point Methods

    /**
     * The support point in any direction is the furthest point on a shape towards that direction. Used by the GJK
     * collision algorithm.
     *
     * @param direction the direction to search (does not have to be a unit vector)
     */
    abstract fun supportPoint(direction: Vec2): Vec2

    /**
     * Returns the point on or inside the shape with the least distance to the given position.
     *
     * @param position the point to search
     * @return the nearest point
     */
    open fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position else supportPoint(position - center())
    }

    /**
     * Whether the specified point lies on or within the boundary of the shape.
     *
     * @param point the point to check
     * @return the scaled shape
     */
    // TODO boundary vs interior vs exterior
    abstract operator fun contains(point: Vec2): Boolean

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
     * This is a rigid transformation and preserves the area.
     *
     * @param direction the direction to move
     * @return the translated shape
     */
    abstract fun translate(direction: Vec2): Shape

    /**
     * Rotates every point on this shape around the given origin by the same angle.
     * This is a rigid transformation and preserves the area.
     *
     * @param angle  the counterclockwise angle in degrees
     * @param origin the point to rotate around, or the shape's centroid if null
     * @return the rotated shape
     */
    abstract fun rotate(angle: Float, origin: Vec2? = null): Shape

    /**
     * Stretches the dimensions of this shape by a given scale vector. This is a non-rigid and non-linear (anisotropic)
     * transformation and may alter the overall form and area. Some shapes may only be transformed linearly,
     * where both dimensions will be scaled by a single scale factor and the overall form is maintained.
     *
     * @param factor how much to scale the shape along each axis
     * @param origin the point to scale from, or the shape's centroid if null
     * @return the scaled shape
     */
    abstract fun scale(factor: Vec2, origin: Vec2? = null): Shape

    // Overrides

    /**
     * Whether this shape is equivalent to another shape before applying any rigid transformations.
     */
    abstract override operator fun equals(other: Any?): Boolean

    /**
     * A string representation of the shape.
     */
    abstract override fun toString(): String

}