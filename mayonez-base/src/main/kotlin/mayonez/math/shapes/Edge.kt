package mayonez.math.shapes

import mayonez.math.*
import java.util.*
import kotlin.math.*

/**
 * A line segment defined by two endpoints inside the 2D plane. An `Edge` can
 * be used to model collision for the ground or a wall, but using a thin
 * [Rectangle] is recommended for more accurate collision detection against
 * fast-moving objects.
 *
 * @constructor Constructs an edge with the given endpoints. See also
 *     [fromLength].
 * @param start the start point
 * @param end the end point
 *
 * @author SlavSquatSuperstar
 */
class Edge(val start: Vec2, val end: Vec2) : Shape() {

    companion object {

        /**
         * Creates an edge with the given center, length, and angle. The edge's
         * direction points along the positive x-axis plus the given angle.
         *
         * @param center the center
         * @param length the length
         * @param angle the angle in degrees
         */
        @JvmStatic
        @JvmOverloads
        fun fromLength(center: Vec2, length: Float, angle: Float = 0f): Edge {
            val halfDirection = Vec2(length * 0.5f, 0f).rotate(angle)
            return Edge(center - halfDirection, center + halfDirection)
        }

    }

    // Edge Properties

    /** The length of the edge, `l`. */
    val length = start.distance(end)

    /** The square of the edge's length, equal to `l^2`. */
    private val lengthSq
        get() = start.distanceSq(end)

    /**
     * A vector representation of this edge, equal to the end vertex minus the
     * start vertex.
     */
    fun toVector(): Vec2 = end - start

    /**
     * The left unit normal vector of this edge, a vector perpendicular to this
     * edge with a length of 1. By default, points 90 degrees counterclockwise
     * from the vector from the start to the end.
     *
     * @return the left unit normal
     */
    fun unitNormalLeft(): Vec2 = toVector().normal() / length

    /**
     * The right unit normal vector of this edge, a vector perpendicular to
     * this edge with a length of 1. By default, points 90 degrees clockwise
     * from the vector from the start to the end.
     *
     * @return the right unit normal
     */
    fun unitNormalRight(): Vec2 = -toVector().normal() / length

    /**
     * The unit normal vector of this edge that points toward a certain
     * direction.
     *
     * @param direction the direction the normal should point toward
     * @return the unit normal vector towards given direction, or (0, 0) if the
     *     direction is parallel to this edge.
     */
    fun unitNormal(direction: Vec2): Vec2 {
        return Vec2.tripleProduct(toVector(), direction, toVector()).unit()
    }

    /** The area of the edge, equal to 0 because it has only one dimension. */
    override fun area(): Float = 0f

    /** The center of the edge, or the midpoint of the two vertices. */
    override fun center(): Vec2 = start.midpoint(end)

    // Lerp

    /**
     * Linearly interpolates (lerps) between the two endpoints, or finds the
     * point on this line that is the given percentage of the distance between
     * the start to the end.
     *
     * @param distance what percent to travel from the start
     * @return the interpolated point
     */
    fun lerp(distance: Float): Vec2 = start + toVector() * distance

    /**
     * Takes a point on this line and calculates its percent distance along
     * this line. If the point is not on this, its projection is used instead.
     *
     * @param point a point along this line
     * @return the percent distance along the line
     */
    fun invLerp(point: Vec2): Float = (point - start).component(toVector()) / length

    // Bounds

    override fun boundingBox(): BoundingBox {
        val width = abs(end.x - start.x).coerceAtLeast(MathUtils.FLOAT_EPSILON)
        val height = abs(end.y - start.y).coerceAtLeast(MathUtils.FLOAT_EPSILON)
        return BoundingBox(center(), Vec2(width, height))
    }

    // Edge vs Point

    /**
     * Calculates the distance from a point to this line segment.
     *
     * @param to the point to measure
     * @return the absolute distance the point to this line
     */
    fun distance(to: Vec2): Float = to.distance(nearestPoint(to))

    override fun supportPoint(direction: Vec2): Vec2 {
        return if (start.dot(direction) > end.dot(direction)) start else end
    }

    /**
     * Calculates the point inside this segment (including the endpoints) with
     * the least distance to the given position.
     *
     * @param to the point to test
     * @return the nearest point
     */
    override fun nearestPoint(to: Vec2): Vec2 {
        // Project point onto segment
        val projLength = (to - start).dot(toVector()) / length
        // Projection is past segment end
        return if (projLength > length) end
        // Projection is behind segment start
        else if (projLength < 0) start
        // Projection is inside segment
        else start + (toVector() * (projLength / length))
    }

    override fun contains(point: Vec2): Boolean {
        // Point is an endpoint
        if (point == start || point == end) return true
        // Project point onto segment
        val startToPoint = point - start
        val projLength = startToPoint.dot(toVector()) / length
        // Projection is outside segment
        return if (projLength !in 0f..length) false
        // Check if the point is collinear with the endpoints
        else MathUtils.equals(startToPoint.cross(toVector()), 0f)
    }

    // Physical Properties

    /**
     * The mass of the edge `m`, assuming a uniform density throughout the edge.
     *
     * @param density the linear density (mass per length) of the edge
     */
    override fun mass(density: Float): Float = density * length

    /**
     * The edge's centroidal moment of inertia, equal to `1/12 * ml^2`.
     * Equivalent to that of a very thin rectangle.
     *
     * Second moment of area: `I_z = 1/12 * l^3 = 1/12 * l * l^2`.
     */
    override fun angularMass(mass: Float): Float = mass / 12f * lengthSq

    // Transformations

    override fun translate(direction: Vec2): Edge {
        return Edge(start + direction, end + direction)
    }

    override fun rotate(angle: Float, origin: Vec2?): Edge {
        return Edge(
            start.rotate(angle, origin ?: center()),
            end.rotate(angle, origin ?: center())
        )
    }

    override fun scale(factor: Vec2, origin: Vec2?): Edge {
        return if (origin != null) Edge(start * factor, end * factor)
        else Edge(start.scale(factor, center()), end.scale(factor, center()))
    }

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Edge) && (this.start == other.start)
                && (this.end == other.end)
    }

    override fun hashCode(): Int = Objects.hash(start, end)

    override fun toString(): String = "Edge ($start, $end)"
}