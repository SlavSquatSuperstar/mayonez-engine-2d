package mayonez.physics.raycast

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

/**
 * Performs raycasts for primitive shapes.
 *
 * @author SlavSquatSuperstar
 */
object Raycasts {

    /**
     * Casts a ray onto a shape and calculates the contact point, distance, and
     * normal.
     *
     * @param shape the shape to raycast
     * @param ray the ray to cast
     * @param limit the max length the ray can travel
     * @return the contact information, or null if the ray misses
     */
    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): RaycastInfo? {
        return when {
            (shape == null) || (ray == null) -> null
            (shape is Circle) -> raycastCircles(shape, ray, limit)
            (shape is Edge) -> raycastEdge(shape, ray, limit)
            (shape is Rectangle) -> RectangleRaycastDetector.raycast(shape, ray, limit)
            (shape is Polygon) -> raycastPolygon(shape, ray, limit)
            else -> null
        }
    }

    // Source: https://youtu.be/23kTf-36Fcw
    private fun raycastCircles(circle: Circle, ray: Ray, limit: Float): RaycastInfo? {
        // Trace the ray's origin to the circle's center
        val originToCenter = circle.center() - ray.origin

        // Project originToCenter onto the ray and find length
        // Adjacent leg of right triangle -> inverse hypotenuse for opposite leg
        val projLength = originToCenter.component(ray.direction)
        val distNearestSq =
            originToCenter.lenSq() - projLength * projLength // Closest distance from center to extended ray
        if (distNearestSq > circle.radiusSq) return null // Nearest point on ray is outside the circle

        // Find length of projected vector inside circle
        val contactToNearest = sqrt(circle.radiusSq - distNearestSq)
        // Distance along ray to contact, check if ray starts in circle
        val hitDist = if (ray.origin in circle) projLength + contactToNearest
        else projLength - contactToNearest

        if (limit > 0f && hitDist > limit) return null // Ray exceeds limit
        if (hitDist < 0f) return null // Contact point is behind ray

        val point = ray.getPoint(hitDist)
        return RaycastInfo.createNormalized(point, point - circle.center(), hitDist)
    }

    private fun raycastEdge(edge: Edge, ray: Ray, limit: Float): RaycastInfo? {
        // Find line directions
        val dir1 = edge.toVector() / edge.length
        val dir2 = ray.direction
        val cross = dir1.cross(dir2)

        // If ray is parallel, then raycast is undefined
        // Ray either misses or hits endpoint (no normal)
        if (FloatMath.equals(cross, 0f)) return null

        // Calculate intersection point
        val startToOrigin = ray.origin - edge.start
        val dist1 = startToOrigin.cross(dir2) / cross
        val dist2 = startToOrigin.cross(dir1) / cross

        // Contact must be inside edge and inside ray if limit is enabled
        if (!FloatMath.inRange(dist1, 0f, edge.length) || dist2 < 0
            || (limit > 0 && dist2 > limit)
        ) return null

        val contact = edge.start + (dir1 * dist1)
        return RaycastInfo(contact, edge.unitNormal(-dir2), dist2)
    }

    private fun raycastPolygon(poly: Polygon, ray: Ray, limit: Float): RaycastInfo? {
        val edges = poly.edges
        // Find raycast distance to the closest edge
        val distances = edges.map { raycastEdge(it, ray, limit) }
            .map { it?.distance ?: Float.POSITIVE_INFINITY }
            .toFloatArray()

        val minIndex = FloatMath.minIndex(*distances)
        val minDist = distances[minIndex]
        if (minDist == Float.POSITIVE_INFINITY) return null // No successful raycasts

        return RaycastInfo(ray.getPoint(minDist), edges[minIndex].unitNormalRight(), minDist)
    }

}