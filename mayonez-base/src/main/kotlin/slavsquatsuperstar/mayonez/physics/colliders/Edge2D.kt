package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Range
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.RaycastResult
import slavsquatsuperstar.mayonez.physics.shapes.Ray
import kotlin.math.sign

/**
 * A 2D line segment with start and end points.
 *
 * @author SlavSquatSuperstar
 */
class Edge2D(@JvmField val start: Vec2, @JvmField val end: Vec2) {

    // Properties
    private val slope: Float
        get() = (end.y - start.y) / (end.x - start.x)

    val length: Float
        @JvmName("length") get() = start.distance(end)

    fun toVector(): Vec2 = end - start

    // Line vs Point

    /**
     * Calculates whether the given point is on this line segment.
     *
     * @param point a point in 2D space
     * @return if the point is on the line
     */
    operator fun contains(point: Vec2): Boolean {
        return if (point == start || point == end) true
        else nearestPoint(point) == point
    }

    fun nearestPoint(position: Vec2): Vec2 {
        val projLength = position.sub(start).dot(toVector()) / length // find point shadow on line
        if (projLength > length) // pat line end
            return end else if (projLength < 0) // behind line start
            return start
        return start.add(toVector().mul(projLength / length)) // inside line
    }

    /**
     * Calculates the distance from a point to this line segment.
     *
     * @param point a 2D point
     * @return the absolute distance the point to this line
     */
    fun distance(point: Vec2): Float {
        return point.distance(nearestPoint(point))
    }

    /**
     * Returns the distance from a point to this line relative to that of other points, and which side the point is on.
     *
     * @param point a point
     * @return A negative value if the point is to the right of this line, a positive value if the point is to the left
     * of this line, and zero if the point is on the line (if extended infinitely).
     */
    fun pseudoDistance(point: Vec2): Float {
        return point.sub(start).dot(toVector().rotate(90f))
    }

    // Line vs Line

    // SAT intersection test: https://www.youtube.com/watch?v=RBya4M6SWwk
    fun intersects(edge: Edge2D): Boolean {
        // If parallel, must be overlapping (co-linear)
        return if (MathUtils.equals(slope, edge.slope)) {
            edge.start in this || edge.end in this ||
                    this.start in edge || this.end in edge
        } else raycast(Ray(edge), edge.length) != null
    }

    fun raycast(ray: Ray, limit: Float): RaycastResult? {
        val start1 = start
        val start2 = ray.origin

        // Calculate point of intersection using cross product
        val line1 = toVector() / length
        val line2 = ray.direction
        val cross = line1.cross(line2)
        if (MathUtils.equals(cross, 0f))
            return null // if parallel

        // Parametric lengths along rays
        val dist1 = (start2 - start1).cross(line2) / cross
        val dist2 = (start1 - start2).cross(line1) / -cross
        if (!MathUtils.inRange(dist1, 0f, length)) // Contact point outside line
            return null
        if (dist2 < 0 || limit > 0 && dist2 > limit) // Contact point outside allowed ray
            return null
        val contact = start1.add(line1.mul(dist1))
        // rotate left or right depending on which side ray started form
        val normal = line1.rotate(sign(pseudoDistance(start2)) * 90)
        return RaycastResult(contact, normal, dist1)
    }

    // Clip Line

    // should both be parallel
    fun clipToPlanes(plane1: Ray, plane2: Ray): Edge2D {
        val ray = Ray(this)
        val contact1 = ray.getIntersection(plane1)
        val contact2 = ray.getIntersection(plane2)

        if (contact1 == null || contact2 == null)
            return Edge2D(start, end)
        // get distances
        val distances = Range((contact1 - start).dot(ray.direction), (contact2 - start).dot(ray.direction))
        val min = 0f.coerceAtLeast(distances.min)
        val max = length.coerceAtMost(distances.max)

        return Edge2D(ray.getPoint(min), ray.getPoint(max))
    }

    fun clipToSegment(segment: Edge2D): Edge2D {
        val rayDir = segment.toVector().normal().unit()
        return clipToPlanes(Ray(segment.start, rayDir), Ray(segment.end, rayDir))
    }

    // Overrides

    override fun equals(other: Any?): Boolean =
        (other is Edge2D) && (this.start == other.start) && (this.end == other.end)

    override fun toString(): String = String.format("Start: %s, End: %s", start, end)
}