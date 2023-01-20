package mayonez.physics.shapes

import mayonez.math.Interval
import mayonez.math.Vec2
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * A 2D line segment defined by two endpoints. An edge is a one-dimensional simplex formed when two plane figures
 * intersect.
 *
 * @author SlavSquatSuperstar
 */
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

//    /**
//     * Clips an edge along a single plane and keeps one of the sub-segments.
//     *
//     * @param plane     a ray pointing towards this edge
//     * @param direction in which direction to keep the points
//     * @return the clipped edge
//     */
//    fun clipToPlane(plane: Ray, direction: Vec2): Edge {
//        // Find edge intersection with plane
//        val edge = Ray(start, toVector() / length)
//        val startDist = (plane.origin - this.start).dot(direction)
//        val endDist = (plane.origin - this.end).dot(direction)
//
//        // plane behind or in front of edge
//        if (startDist <= 0 && endDist <= 0) return Edge(start, end)
//        else if (startDist >= 0 && endDist >= 0) return Edge(start, end)
//
//        val contact = edge.getPoint(abs(startDist))
//        return if (startDist < 0) Edge(start, contact)
//        else Edge(contact, end)
//    }

    // Clip Methods
    /**
     * Clips this to segment to two planes extending perpendicular from another segment's endpoints. The resulting edge
     * is this edge's projection on the other.
     */
    fun clipToSegment(segment: Edge): Edge {
        val planeDir = segment.unitNormal()
        val plane1 = Ray(segment.start, planeDir)
        val plane2 = Ray(segment.end, planeDir)

        // Find edge intersections with planes
        val edge = Ray(start, toVector() / length)
        val contact1 = edge.getIntersection(plane1)
        val contact2 = edge.getIntersection(plane2)

        if (contact1 == null || contact2 == null) return Edge(start, end) // plane is parallel to line

        // Clip edge to new endpoints
        val distances = Interval((contact1 - start).dot(edge.direction), (contact2 - start).dot(edge.direction))
        val minDist = max(0f, distances.min)
        val maxDist = min(length, distances.max)
        return Edge(edge.getPoint(minDist), edge.getPoint(maxDist))
    }
//    fun clipToSegment(segment: Edge): Edge {
//        // Find edge intersections with planes
//        val edge = Ray(start, toVector() / length)
//        val planeDist1 = (segment.start - this.start).dot(edge.direction)
//        val planeDist2 = (segment.end - this.start).dot(edge.direction)
//
//        // Clip edge to new endpoints
//        val dists = Range(planeDist1, planeDist2)
//        val minDist = max(dists.min, 0f)
//        val maxDist = min(dists.max, length)
//        return Edge(edge.getPoint(minDist), edge.getPoint(maxDist))
//    }

    // Lerp

    /**
     * Linearly interpolates (lerps) between the two endpoints, or finds the point on this line
     * that is the given percentage of the distance between the start to the end.
     *
     * @param distance what percent to travel from the start
     * @return the interpolated point
     */
    fun lerp(distance: Float): Vec2 = start + toVector() * distance

    /**
     * Takes a point on this line and calculates its percent distance along this line. If the point is
     * not on this, its projection is used instead.
     *
     * @param point a point along this line
     * @return the percent distance along the line
     */
    fun invLerp(point: Vec2): Float = (point - start).component(toVector()) / length
    // Bounds

    override fun boundingCircle(): Circle = Circle(center(), length * 0.5f)

    override fun boundingRectangle(): BoundingBox =
        BoundingBox(center(), Vec2(abs(end.x - start.x), abs(end.y - start.y)))

    // Edge vs Point

    /**
     * Calculates the distance from a point to this line segment.
     *
     * @param point a point
     * @return the absolute distance the point to this line
     */
    fun distance(point: Vec2): Float {
        return point.distance(nearestPoint(point))
    }

    override fun supportPoint(direction: Vec2): Vec2 = if (start.dot(direction) > end.dot(direction)) start else end

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
     * The edge's centroidal moment of inertia, equal to 1/12*ml^2. Equivalent to that of a very thin rectangle.
     *
     * Second moment of area: I_z = 1/12*l^3 = 1/12*l*l^2
     */
    override fun angularMass(mass: Float): Float = mass / 12f * lengthSq

    // Transformations

    override fun translate(direction: Vec2): Edge = Edge(start + direction, end + direction)

    override fun rotate(angle: Float, origin: Vec2?): Edge {
        return Edge(start.rotate(angle, origin ?: center()), end.rotate(angle, origin ?: center()))
    }

    override fun scale(factor: Vec2, origin: Vec2?): Edge {
        return if (origin != null) Edge(start * factor, end * factor)
        else Edge(start.scale(factor, center()), end.scale(factor, center()))
    }

    // Overrides

    override fun equals(other: Any?): Boolean {
        return (other is Edge) && (this.start == other.start && this.end == other.end)
    }

    /**
     * A description of the edge in the form Edge (v1, v2)
     */
    override fun toString(): String = "Edge ($start, $end)"
}