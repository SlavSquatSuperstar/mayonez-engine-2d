package mayonez.math.shapes

import mayonez.math.*
import java.util.*
import kotlin.math.*

/**
 * A 2D line segment defined by two endpoints. An edge is a one-dimensional
 * simplex formed when two plane figures intersect.
 *
 * @author SlavSquatSuperstar
 */
class Edge(val start: Vec2, val end: Vec2) : Shape() {

    // Edge Properties

    override val numVertices: Int = 2

    /** The length of the edge, l. */
    val length = start.distance(end)

    /** The square of the edge's length, equal to l^2. */
    private val lengthSq = length * length

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
    fun unitNormalLeft(): Vec2 = toVector().normal().unit()

    /**
     * The right unit normal vector of this edge, a vector perpendicular to
     * this edge with a length of 1. By default, points 90 degrees clockwise
     * from the vector from the start to the end.
     *
     * @return the right unit normal
     */
    fun unitNormalRight(): Vec2 = -toVector().normal().unit()

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

    override fun boundingCircle(): Circle = Circle(center(), length * 0.5f)

    override fun boundingRectangle(): BoundingBox {
        val width = abs(end.x - start.x).coerceAtLeast(MathUtils.FLOAT_EPSILON)
        val height = abs(end.y - start.y).coerceAtLeast(MathUtils.FLOAT_EPSILON)
        return BoundingBox(center(), Vec2(width, height))
    }

    // Edge vs Point

    /**
     * Calculates the distance from a point to this line segment.
     *
     * @param point a point
     * @return the absolute distance the point to this line
     */
    fun distance(point: Vec2): Float = point.distance(nearestPoint(point))

    override fun supportPoint(direction: Vec2): Vec2 {
        return if (start.dot(direction) > end.dot(direction)) start else end
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        val projLength = position.sub(start).dot(toVector()) / length // find point shadow on line
        if (projLength > length) // past line end
            return end else if (projLength < 0) // behind line start
            return start
        return start.add(toVector().mul(projLength / length)) // inside line
    }

    override fun contains(point: Vec2): Boolean {
        if (point == start || point == end) return true // point is endpoint
        val projLen = (point - start).component(end - start) // project v1->P onto v1->v2
        if (projLen < 0 || projLen > length) return false // point outside line
        return point == start + (end - start) * (projLen / length)
    }

    // Physical Properties

    /**
     * The mass of the edge, assuming a uniform density throughout the edge.
     *
     * @param density the linear density (mass per length) of the edge
     */
    override fun mass(density: Float): Float = density * length

    /**
     * The edge's centroidal moment of inertia, equal to 1/12*ml^2. Equivalent
     * to that of a very thin rectangle.
     *
     * Second moment of area: I_z = 1/12*l^3 = 1/12*l*l^2
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

    /** A description of the edge in the form Edge (v1, v2) */
    override fun toString(): String = "Edge ($start, $end)"
}