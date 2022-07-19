package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature

/**
 * A single, discrete but infinitesimal location in space with no dimensions. A point is the simplest zero-dimensional object
 * (simplex), formed when two lines intersect.
 */
@ExperimentalFeature
class Point(val position: Vec2) : Shape() {

    /**
     * The area of the point, equal to 0 because it has no dimensions.
     */
    override fun area(): Float = 0f

    /**
     * The center of the point, equal to its position.
     */
    override fun center(): Vec2 = position

    /**
     * Creates a bounding circle centered around the point with the radius as [Float.MIN_VALUE].
     */
    override fun boundingCircle(): Circle = Circle(position, Float.MIN_VALUE)

    /**
     * Creates a bounding rectangle centered around the point with both dimensions as [Float.MIN_VALUE].
     */
    override fun boundingRectangle(): BoundingRectangle = BoundingRectangle(position, Vec2(Float.MIN_VALUE))

    /**
     * The support point of a point in any direction is its own position.
     */
    override fun supportPoint(direction: Vec2): Vec2 = position

    /**
     * The point's centroidal moment of inertial around itself, equal to 0.
     */
    override fun angularMass(mass: Float): Float = 0f

    override fun translate(direction: Vec2): Shape = Point(position + direction)

    override fun rotate(angle: Float, origin: Vec2?): Shape =
        if (origin == null) Point(position) else Point(position.rotate(angle))

    override fun scale(factor: Vec2, origin: Vec2?): Shape =
        if (origin == null) Point(position) else Point(position * factor)

    override fun contains(point: Vec2): Boolean = (position == point)

    override fun equals(other: Any?): Boolean = (other is Point) && (position == other.position)

    /**
     * A description of the point in the form Point (x, y).
     */
    override fun toString(): String = "Point $position"
}