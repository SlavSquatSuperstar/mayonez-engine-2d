package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.RaycastResult
import kotlin.math.sign

/**
 * A 2D line segment with start and end points.
 *
 * @author SlavSquatSuperstar
 */
class Edge2D(@JvmField val start: Vec2, @JvmField val end: Vec2) {

    // Properties
    fun slope(): Float = (end.y - start.y) / (end.x - start.x)

    fun length(): Float = start.distance(end)

    fun toVector(): Vec2 = end.sub(start)

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
        val length = length()
        val projLength = position.sub(start).dot(toVector()) / length // find point shadow on line
        if (projLength > length) // pat line end
            return end else if (projLength < 0) // behind line start
            return start
        return start.add(toVector().mul(projLength / length)) // inside line
    }

    // right (clockwise) is positive
    fun distance(point: Vec2): Float {
        return point.sub(start).projectedLength(toVector().rotate(90f))
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
    fun intersects(edge: Edge2D): Boolean {
        // If parallel, must be overlapping (co-linear)
        return if (MathUtils.equals(slope(), edge.slope())) {
            this.contains(edge.start) || this.contains(edge.end) ||
                    edge.contains(start) || edge.contains(end)
        } else raycast(Ray2D(edge), edge.length()) != null
    }

    fun raycast(ray: Ray2D, limit: Float): RaycastResult? {
        val length = length()
        val start1 = start
        val start2 = ray.origin

        // Calculate point of intersection using cross product
        val line1 = toVector().div(length)
        val line2 = ray.direction
        val cross = line1.cross(line2)

        // Parametric lengths along rays
        val dist1 = start2.sub(start1).cross(line2) / cross
        val dist2 = start1.sub(start2).cross(line1) / -cross
        if (!MathUtils.inRange(dist1, 0f, length)) // Contact point outside line
            return null
        if (dist2 < 0 || limit > 0 && dist2 > limit) // Contact point outside allowed ray
            return null
        val contact = start1.add(line1.mul(dist1))
        // rotate left or right depending on which side ray started form
        val normal = line1.rotate(sign(pseudoDistance(start2)) * 90)
        return RaycastResult(contact, normal, dist1)
    }

    override fun toString(): String = String.format("Start: %s, End: %s", start, end)
}