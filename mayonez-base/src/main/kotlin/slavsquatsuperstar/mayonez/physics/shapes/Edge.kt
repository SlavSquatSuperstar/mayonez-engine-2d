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
class Edge(val v1: Vec2, val v2: Vec2) : Shape() {

    // Edge Properties

    /**
     * The length of the edge, l.
     */
    @JvmField
    val length = v1.distance(v2)

    /**
     * The square of the edge's length, equal to l^2.
     */
    @JvmField
    val lengthSq = length * length

    /**
     * The area of the edge, equal to 0 because it has only one dimension.
     */
    override fun area(): Float = 0f

    /**
     * The center of the edge, or the midpoint of the two vertices.
     */
    override fun center(): Vec2 = v1.midpoint(v2)

    // Physical Properties

    /**
     * The edge's centroidal moment of inertia, equal to 1/12*ml^2. Equivalent to that of a very thin rectangle.
     *
     * Second moment of area: I_z = 1/12*l^3 = 1/12*l*l^2
     */
    override fun angularMass(mass: Float): Float = mass / 12f * lengthSq

    // Collision Properties

    override fun boundingCircle(): Circle = Circle(center(), length * 0.5f)

    override fun boundingRectangle(): BoundingRectangle = BoundingRectangle(center(), Vec2(abs(v2.x - v1.x), abs(v2.y - v1.y)))

    override fun supportPoint(direction: Vec2): Vec2 = if (v1.dot(direction) > v2.dot(direction)) v1 else v2

    // Geometric Transformations

    override fun translate(direction: Vec2): Edge = Edge(v1 + direction, v2 + direction)

    override fun rotate(angle: Float, origin: Vec2?): Edge {
        return Edge(v1.rotate(angle, origin ?: center()), v2.rotate(angle, origin ?: center()))
    }

    override fun scale(factor: Vec2, origin: Vec2?): Edge {
        return if (origin != null) Edge(v1 * factor, v2 * factor)
        else Edge(v1.scale(factor, center()), v2.scale(factor, center()))
    }

    // Overrides

    override fun contains(point: Vec2): Boolean {
        if (point == v1 || point == v2) return true
        val projLen = (point - v1).component(v2 - v1) // project v1->P onto v1->v2
        if (projLen < 0 || projLen > length) return false // point outside line
        return point == v1 + (v2 - v1) * (projLen / length)
    }

    override fun equals(other: Any?): Boolean {
        return (other is Edge) && (this.v1 == other.v1 && this.v2 == other.v2)
    }

    /**
     * A description of the edge in the form Edge (v1, v2)
     */
    override fun toString(): String = "Edge ($v1, $v2)"
}