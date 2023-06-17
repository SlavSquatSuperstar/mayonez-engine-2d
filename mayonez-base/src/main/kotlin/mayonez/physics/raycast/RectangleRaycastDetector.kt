package mayonez.physics.raycast

import mayonez.math.*
import mayonez.math.shapes.*
import kotlin.math.*

/**
 * Raycasts onto rectangles.
 *
 * Source: [YouTube](https://youtu.be/8JJ-4JgR7Dg)
 *
 * @author SlavSquatSuperstar
 */
internal object RectangleRaycastDetector : RaycastDetector<Rectangle> {

    override fun raycast(rect: Rectangle, ray: Ray, limit: Float): RaycastInfo? {
        val localRay = ray.transformToRectangle(rect)

        // Distance to min/max box axes
        val nearDist = localRay.getParametricDistance(rect.min())
        val farDist = localRay.getParametricDistance(rect.max())

        swapComponentsIfOutOfOrder(nearDist, farDist)
        if (doesRayNotHit(nearDist, farDist)) return null

        // Parametric distances to near and far contact along ray
        val nearHitDist = (nearDist.x).coerceAtLeast(nearDist.y)
        val farHitDist = (farDist.x).coerceAtMost(farDist.y)
        if (isRayPointingAway(farHitDist, nearHitDist)) return null

        // If ray starts inside shape, use far for contact
        val distToRect = if (nearHitDist < 0) farHitDist else nearHitDist
        if (isContactPastRayLimit(limit, distToRect)) return null
        return calculateRaycastInfo(ray, localRay, distToRect, nearDist)
    }

    // Raycast helper methods

    private fun Ray.transformToRectangle(rect: Rectangle): Ray {
        return if (rect.isAxisAligned) this
        else this.rotate(rect.angle, rect.center())
    }

    private fun Ray.getParametricDistance(axes: Vec2): Vec2 {
        return (axes - (this.origin)).unsafeDivide(this.direction)
    }

    private fun Vec2.unsafeDivide(v: Vec2): Vec2 {
        return Vec2(this.x / v.x, this.y / v.y)
    }

    private fun swapComponentsIfOutOfOrder(tNear: Vec2, tFar: Vec2) {
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
    }

    private fun doesRayNotHit(nearDist: Vec2, farDist: Vec2): Boolean {
        return (nearDist.x > farDist.y) || (nearDist.y > farDist.x)
    }

    private fun isRayPointingAway(tHitFar: Float, tHitNear: Float): Boolean {
        return (tHitFar < 0) || (tHitNear > tHitFar)
    }

    private fun isContactPastRayLimit(limit: Float, distToRect: Float): Boolean {
        return (limit > 0) && (distToRect > limit)
    }

    private fun calculateRaycastInfo(
        ray: Ray, localRay: Ray, distToRect: Float, nearDist: Vec2,
    ): RaycastInfo {
        val contact = localRay.getPoint(distToRect)
        val normal = localRay.calculateHitNormal(nearDist)
        return RaycastInfo(contact, normal, contact.distance(ray.origin))
    }

    private fun Ray.calculateHitNormal(nearDist: Vec2): Vec2 {
        return when {
            (nearDist.x > nearDist.y) -> Vec2(-sign(direction.x), 0f) // Horizontal collision
            (nearDist.x < nearDist.y) -> Vec2(0f, -sign(direction.y)) // Vertical collision
            else -> Vec2() // Diagonal collision
        }
    }

}