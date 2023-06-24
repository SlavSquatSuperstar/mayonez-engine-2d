package mayonez.math.shapes

import mayonez.math.*

/**
 * A bounded, flat figure that exists in the 2D plane and has no
 * intersecting edges. Can also represent an infinitesimally thin
 * cross-section of a solid (lamina). Shape objects are immutable, meaning
 * their component points and other characteristics cannot change.
 *
 * @author SlavSquatSuperstar
 */
abstract class Shape : Transformable {

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
         * Finds the most extreme points from the Minkowski difference set between
         * two shapes.
         *
         * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting shapes
         * into supporting points
         */
        fun support(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 =
            shape1.supportPoint(dir) - shape2.supportPoint(-dir)
    }

    // Geometric Properties

    /** The number of vertices in this shape. Round shapes have zero. */
    abstract val numVertices: Int

    /**
     * The area of the shape, or the amount of space it takes up in the 2D
     * plane.
     */
    abstract fun area(): Float

    /**
     * The centroid, or geometric center of the shape, calculated by averaging
     * all the points in the shape. The centroid is also the center of mass for
     * a shape of uniform density.
     */
    abstract fun center(): Vec2 // TODO move to physical?

    // Min Bounds Methods

    /**
     * The bounding circle (sphere) of a shape is the smallest circle that
     * contains all points in the shape and shares the same centroid. Useful
     * in broad-phase collision detection since circle collision checks are
     * simpler.
     */
    abstract fun boundingCircle(): Circle

    /**
     * The axis-aligned minimum bounding rectangle (AABB) of a shape is the
     * smallest rectangle that contains all points in the shape and is aligned
     * with the cartesian axes. The rectangle may not share the shape's
     * centroid.
     */
    abstract fun boundingRectangle(): BoundingBox

    // Shape vs Point Methods

    /**
     * The support point in any direction is the farthest point on a shape
     * towards that direction. Used by the GJK collision algorithm.
     *
     * @param direction the direction to search (does not have to be a unit
     *     vector)
     */
    abstract fun supportPoint(direction: Vec2): Vec2

    /**
     * Returns the point on or inside the shape with the least distance to the
     * given position.
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
     * The mass of the shape, or its resistance to changes in linear motion,
     * assuming a uniform density throughout the shape.
     *
     * @param density the area density (mass per area) of the shape
     */
    fun mass(density: Float): Float = density * area()

    /**
     * The two-dimensional moment of inertia (angular mass) of the shape around
     * its centroid, or its resistance to changes in rotation. This function
     * specifically calculates the polar moment of area, I_z/J_z, of a lamina
     * (thin object equivalent to a 2D shape) while assuming a uniform density
     * throughout the shape. While the second moment of area has units of L^4,
     * multiplying it by area density (m/L^2) gives it the same units as moment
     * of inertia (mL^2).
     *
     * @param mass the mass of the shape
     */
    abstract fun angularMass(mass: Float): Float

    // Geometric Transformations

    /**
     * Moves every point on this shape along the same vector, preserving its
     * area and perimeter.
     *
     * @param direction the vector to move along
     * @return the translated shape
     */
    abstract override fun translate(direction: Vec2): Shape

    /**
     * Rotates every point on this shape around the given origin by the same
     * angle, preserving its area and perimeter.
     *
     * @param angle the counterclockwise angle in degrees
     * @param origin the point to rotate around, or the shape's centroid if
     *     null
     * @return the rotated shape
     */
    abstract override fun rotate(angle: Float, origin: Vec2?): Shape

    /**
     * Stretches the dimensions of this shape by a given scale vector, altering
     * its area and parameter. Scaling also changes the overall form of the
     * shape if the scale factor is non-uniform.
     *
     * @param factor how much to scale the shape along each axis
     * @param origin the point to scale from, or the shape's centroid if null
     * @return the scaled shape
     */
    abstract override fun scale(factor: Vec2, origin: Vec2?): Shape

    // Overrides

    /**
     * Whether this shape has the same form, position size, and orientation as
     * another. For this program, equality is not the same as congruency, where
     * translations, rotations, and reflections may be applied. Equivalent
     * shapes have the same area and perimeter, and their constituent parts are
     * also equal.
     *
     * @param other another shape
     * @return if the two shapes are equal
     */
    abstract override fun equals(other: Any?): Boolean

    /** An integer hashcode function for this shape. */
    abstract override fun hashCode(): Int

    /** A string representation of the shape. */
    abstract override fun toString(): String

}