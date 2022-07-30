package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import kotlin.math.abs

/**
 * A one-dimensional "shape", modeling a line segment defined by two endpoints. An edge is a one-dimensional simplex,
 * formed when two plane figures intersect.
 */
// TODO merge with Edge2D
@ExperimentalFeature
class Edge(val start: Vec2, val end: Vec2) : Shape() {

    // Edge Properties

    override val numVertices: Int = 2

    /**
     * The length of the edge, l.
     */
    @JvmField
    val length = start.distance(end)

    /**
     * The square of the edge's length, equal to l^2.
     */
    @JvmField
    val lengthSq = length * length

    /**
     * A vector representation of this edge, equal to the end vertex minus the start vertex.
     */
    fun toVector(): Vec2 = end - start

    /**
     * The unit normal vector of this edge, a vector perpendicular to this edge with a length of 1. By default, points
     * 90 degrees counterclockwise from the vector from the start to the end.
     *
     * @return the unit normal vector
     */
    fun unitNormal(): Vec2 = toVector().normal().unit()

    /**
     * The unit normal vector of this edge that points toward a certain direction.
     *
     * @param direction the direction the normal should point toward
     * @return the unit normal vector towards given direction, or (0, 0) if the direction is parallel to this edge.
     */
    fun unitNormal(direction: Vec2): Vec2 = Vec2.tripleProduct(toVector(), direction, toVector()).unit()

    /**
     * The area of the edge, equal to 0 because it has only one dimension.
     */
    override fun area(): Float = 0f

    /**
     * The center of the edge, or the midpoint of the two vertices.
     */
    override fun center(): Vec2 = start.midpoint(end)

    // Physical Properties

    /**
     * The edge's centroidal moment of inertia, equal to 1/12*ml^2. Equivalent to that of a very thin rectangle.
     *
     * Second moment of area: I_z = 1/12*l^3 = 1/12*l*l^2
     */
    override fun angularMass(mass: Float): Float = mass / 12f * lengthSq

    // Collision Properties

    override fun boundingCircle(): Circle = Circle(center(), length * 0.5f)

    override fun boundingRectangle(): Rectangle = Rectangle(center(), Vec2(abs(end.x - start.x), abs(end.y - start.y)))

    override fun supportPoint(direction: Vec2): Vec2 = if (start.dot(direction) > end.dot(direction)) start else end

    // Geometric Transformations

    override fun translate(direction: Vec2): Edge = Edge(start + direction, end + direction)

    override fun rotate(angle: Float, origin: Vec2?): Edge {
        return Edge(start.rotate(angle, origin ?: center()), end.rotate(angle, origin ?: center()))
    }

    override fun scale(factor: Vec2, origin: Vec2?): Edge {
        return if (origin != null) Edge(start * factor, end * factor)
        else Edge(start.scale(factor, center()), end.scale(factor, center()))
    }

    // Overrides

    override fun contains(point: Vec2): Boolean {
        if (point == start || point == end) return true // point is endpoint
        val projLen = (point - start).component(end - start) // project v1->P onto v1->v2
        if (projLen < 0 || projLen > length) return false // point outside line
        return point == start + (end - start) * (projLen / length)
    }

    override fun equals(other: Any?): Boolean {
        return (other is Edge) && (this.start == other.start && this.end == other.end)
    }

    /**
     * A description of the edge in the form Edge (v1, v2)
     */
    override fun toString(): String = "Edge ($start, $end)"
}