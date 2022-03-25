package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.Vec2

/**
 * A bounded, flat figure that exists in the 2D plane and has no intersecting edges.
 * Can also represent an infinitesimally thin cross-section of a solid (lamina).
 * Shape objects are immutable, meaning their position and other characteristics cannot change.
 *
 * @author SlavSquatSuperstar
 */
abstract class Shape() {

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
     * The angular mass, (moment of inertia/rotational inertia) of the shape around its centroid.
     *
     * @param mass the mass of the shape
     */
    abstract fun angMass(mass: Float): Float

    // Overrides

    /**
     * Whether the specified point lies on or within the boundary of the shape.
     *
     * @param point a vector in 2D space
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