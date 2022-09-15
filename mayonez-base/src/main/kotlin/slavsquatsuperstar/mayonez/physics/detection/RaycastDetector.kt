package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.RaycastInfo
import slavsquatsuperstar.mayonez.physics.shapes.*
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * A class that casts rays onto shapes and detects whether the ray hits a shape and where the contact is.
 *
 * @author SlavSquatSuperstar
 */
object RaycastDetector {

    @JvmStatic
    fun raycast(shape: Shape?, ray: Ray?, limit: Float): RaycastInfo? {
        return when {
            (shape == null) || (ray == null) -> null
            (shape is Edge) -> raycastEdge(shape, ray, limit)
            (shape is Circle) -> raycastCircle(shape, ray, limit)
            (shape is Rectangle) -> raycastRectangle(shape, ray, limit)
            (shape is Polygon) -> raycastPolygon(shape, ray, limit)
            else -> null
        }
    }

    // Source: https://youtu.be/23kTf-36Fcw
    private fun raycastCircle(circle: Circle, ray: Ray, limit: Float): RaycastInfo? {
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
        // Distance along ray to contact, depends if ray starts in circle
        val hitDist = if (ray.origin in circle) projLength + contactToNearest
        else projLength - contactToNearest

        if (limit > 0f && hitDist > limit) return null // Ray exceeds limit
        if (hitDist < 0f) return null // Contact point is behind ray

        val point = ray.getPoint(hitDist)
        return RaycastInfo(point, point - circle.center(), hitDist)
    }

    private fun raycastEdge(edge: Edge, ray: Ray, limit: Float): RaycastInfo? {
        // Find line directions
        val dir1 = edge.toVector() / edge.length
        val dir2 = ray.direction
        val cross = dir1.cross(dir2)

        // If ray is parallel, then raycast is undefined
        // Ray either misses or hits endpoint (no normal)
        if (MathUtils.equals(cross, 0f)) return null

        // Calculate intersection point
        val startToOrigin = ray.origin - edge.start
        val dist1 = startToOrigin.cross(dir2) / cross
        val dist2 = startToOrigin.cross(dir1) / cross

        // Contact must be inside edge and inside ray if limit is enabled
        if (!MathUtils.inRange(dist1, 0f, edge.length) || dist2 < 0
            || (limit > 0 && dist2 > limit)
        ) return null

        val contact = edge.start + (dir1 * dist1)
        return RaycastInfo(contact, edge.unitNormal(dir1), dist2)
    }

    private fun raycastGJK(poly: Polygon, ray: Ray, limit: Float): RaycastInfo? {
        val lambda = 0.0;
        val vecA = Vec2()
        val vecB = Vec2()

        val start = ray.origin
        val closest = start
        return null
    }

    private fun raycastPolygon(poly: Polygon, ray: Ray, limit: Float): RaycastInfo? {
        val edges = poly.edges
        // Find raycast distance to closest edge
        val distances = FloatArray(edges.size)
        for (i in distances.indices) {
            val rc = raycastEdge(edges[i], ray, limit)
            distances[i] = rc?.distance ?: Float.POSITIVE_INFINITY
        }

        val minIndex = MathUtils.minIndex(*distances)
        val minDist = distances[minIndex]
        if (minDist == Float.POSITIVE_INFINITY) return null // no successful raycasts

        val normal = edges[minIndex].unitNormal()
        return RaycastInfo(ray.getPoint(minDist), normal, minDist)
    }

    // Source: https://youtu.be/8JJ-4JgR7Dg
    private fun raycastRectangle(rect: Rectangle, ray: Ray, limit: Float): RaycastInfo? {
        val localRay = if (rect.isAxisAligned) ray else ray.rotate(rect.angle, rect.center())

        // Parametric distance to min/max x and y axes of box
        val tNear = (rect.min() - (localRay.origin)).unsafeDivide(localRay.direction)
        val tFar = (rect.max() - (localRay.origin)).unsafeDivide(localRay.direction)

        // Swap near and far components if they're out of order
        if (tNear.x > tFar.x) {
            val temp = tNear.x
            tNear.x = tFar.x
            tFar.x = temp
        }
        if (tNear.y > tFar.y) {
            val temp = tNear.y
            tNear.y = tFar.y
            tFar.y = temp
        }
        if (tNear.x > tFar.y || tNear.y > tFar.x) return null // No intersection

        // Parametric distances to near and far contact along ray
        val tHitNear = (tNear.x).coerceAtLeast(tNear.y)
        val tHitFar = (tFar.x).coerceAtMost(tFar.y)
        if (tHitFar < 0 || tHitNear > tHitFar) return null // Ray is pointing away

        // If ray starts inside shape, use far for contact
        val distToRect = if (tHitNear < 0) tHitFar else tHitNear

        // Contact point is past the ray limit
        if (limit > 0 && distToRect > limit) return null
        var normal = Vec2() // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = Vec2(-sign(localRay.direction.x), 0f)
        else if (tNear.x < tNear.y) // Vertical collision
            normal = Vec2(0f, -sign(localRay.direction.y))

        val contact = localRay.getPoint(distToRect)
        return RaycastInfo(contact, normal, contact.distance(ray.origin))
    }

    private fun Vec2.unsafeDivide(v: Vec2): Vec2 {
        return Vec2(this.x / v.x, this.y / v.y)
    }

}