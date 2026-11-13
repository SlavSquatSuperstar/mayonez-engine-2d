package mayonez.math.shapes

import mayonez.Transform
import mayonez.math.*

/**
 * A bounded, flat figure that exists in the two-dimensional plane and has no
 * intersecting edges. Can also represent an infinitesimally thin cross-section
 * of a solid (lamina). Shape objects are immutable, meaning their component
 * points and other characteristics cannot change.
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
        internal fun Vec2.scale(factor: Vec2, center: Vec2): Vec2 {
            return (factor * (this - center)) + center
        }

        /**
         * Finds the most extreme points from the Minkowski difference set
         * between two shapes.
         *
         * Source: https://blog.winter.dev/2020/gjk-algorithm/ § Abstracting
         * shapes into supporting points
         */
        fun support(shape1: Shape, shape2: Shape, dir: Vec2): Vec2 {
            return shape1.supportPoint(dir) - shape2.supportPoint(-dir)
        }
    }

    // Geometric Properties

    /**
     * Calculates the area of the shape `A`, or the amount of space it takes up
     * in the plane.
     *
     * @return the area
     */
    abstract fun area(): Float

    /**
     * Calculates the centroid `C`, or geometric center, of the shape by
     * averaging all the points in the shape. The centroid is also the center
     * of mass for a shape of uniform density.
     *
     * @return the centroid
     */
    abstract fun center(): Vec2 // TODO move to physics?

    // Min Bounds Methods

    /**
     * Calculates the minimum axis-aligned bounding box (AABB), which is the
     * smallest rectangle that contains all points in the shape and is aligned
     * with the Cartesian axes. Useful in broad-phase collision detection to
     * reduce unnecessary calls to expensive algorithms.
     *
     * Note: The rectangle may or may not share the shape's centroid. If the
     * minimum AABB is not possible, then the result should be as close as
     * possible while not being smaller.
     *
     * @return the bounding box
     */
    abstract fun boundingBox(): BoundingBox

    // Shape vs Point Methods

    /**
     * Calculates the support point the given direction, which is the farthest
     * point on a shape towards that direction. Used by the GJK collision
     * algorithm.
     *
     * @param direction the direction to check (does not need to be normalized)
     * @return the support point
     */
    abstract fun supportPoint(direction: Vec2): Vec2

    /**
     * Calculates the point on or inside the shape with the least distance to
     * the given position.
     *
     * @param position the point to test
     * @return the nearest point
     */
    open fun nearestPoint(position: Vec2): Vec2 {
        return if (position in this) position else supportPoint(position - center())
    }

    /**
     * Tests whether the specified point lies on or within the boundary of the
     * shape.
     *
     * @param point the point to test
     * @return if the point is in the shape
     */
    // TODO boundary vs interior vs exterior
    abstract operator fun contains(point: Vec2): Boolean

    // Physical Properties

    /**
     * The mass of the shape `m`, which measures its resistance to changes in
     * linear motion, assuming a uniform density throughout the shape.
     *
     * @param density the area density (mass per area) of the shape
     */
    open fun mass(density: Float): Float = density * area()

    /**
     * The moment of inertia `I` (rotational mass) of the shape, which measures
     * its resistance to changes in angular motion assuming a uniform density
     * throughout the shape. This function calculates the second polar moment
     * of area `I_z`/`J_z` for a flat shape rotating around its centroid and
     * inside its plane. The second polar moment of area has units of `L^4`, so
     * multiplying it by area density (`m/L^2`) gives it the same units as the
     * moment of inertia (`mL^2`).
     *
     * The polar moment of area `I_z` is the sum of the two non-polar moments
     * of area, `I_x` and `I_y` via the perpendicular axis theorem. For
     * composite shapes, the moment of area can be calculated by summing those
     * of the component shapes (geometric decomposition). For shapes not
     * rotating about their centroid, the parallel axis theorem can be used to
     * calculate the moment of area, where `I_new = I + Ad^2`, given area `A`
     * and distance `d`.
     *
     * @param mass the mass of the shape
     * @return the angular mass
     */
    // TODO write unit tests
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

    /**
     * Transforms every point on this object by the given [Transform], first
     * scaling, then rotating, and lastly translating.
     *
     * @param transform an affine transformation
     * @param origin the transform origin, or a predetermined point if null
     */
    override fun transform(transform: Transform, origin: Vec2?): Shape {
        return this.scale(transform.scale, origin)
            .rotate(transform.rotation, origin)
            .translate(transform.position)
    }

    // Overrides

    /**
     * Whether this shape has the same form, position, size, and orientation as
     * another. For this program, equality is not the same as congruency, where
     * translations, rotations, and reflections may be applied. Equivalent
     * shapes have the same area and perimeter, and their constituent parts are
     * also equal.
     *
     * @param other another shape
     * @return if the two shapes are equal
     */
    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    abstract override fun toString(): String

}