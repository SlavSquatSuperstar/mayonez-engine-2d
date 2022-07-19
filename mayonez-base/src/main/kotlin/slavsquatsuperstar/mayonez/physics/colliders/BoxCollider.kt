package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.RaycastResult
import kotlin.math.sign

/**
 * A rotatable rectangle with a width and height. The sides will align with the object's rotation angle.
 *
 * @author SlavSquatSuperstar
 */
class BoxCollider private constructor(min: Vec2, max: Vec2) :
    PolygonCollider(Vec2(min), Vec2(max.x, min.y), Vec2(max), Vec2(min.x, max.y)) {

    private val size: Vec2 = max - min

    constructor(size: Vec2) : this(size * -0.5f, size * 0.5f)

    // Shape Properties

    /**
     * Calculates the dimensions of this box factoring in the object's scale.
     *
     * @return the size in world space
     */
    fun size(): Vec2 = size * (transform.scale)

    val width: Float
        @JvmName("width")
        get() = size().x

    val height: Float
        @JvmName("height")
        get() = size().y

    override fun getAngMass(mass: Float): Float = mass * MathUtils.hypotSq(width, height) / 12f

    // unrotated top left in local space
    private fun localMin(): Vec2 = size * -0.5f

    // unrotated bottom right in local space
    private fun localMax(): Vec2 = size * 0.5f

//    override fun getNormals(): Array<Vec2> =
//        arrayOf(Vec2(1f, 0f).rotate(getRotation()), Vec2(0f, 1f).rotate(getRotation()))

    // OBB vs Point

    override fun contains(point: Vec2): Boolean {
        // Rotate the point into the box's local space then compare to local min and max
        return transform.toLocal(point).inRange(localMin(), localMax())
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        // Transform the point into local space, clamp it, and then transform it back
        return if (position in this) position
        else transform.toWorld(transform.toLocal(position).clampInbounds(localMin(), localMax()))
    }

    // OBB vs Line

    /*
     * Source: https://youtu.be/8JJ-4JgR7Dg
     */
    override fun raycast(ray: Ray, limit: Float): RaycastResult? {
        // Transform the ray to local space (but just rotate direction)
        val localRay = ray.transform(transform)
        val localLimit = limit * localRay.direction.len()

        // Parametric distance to min/max x and y axes of box
        val tNear = (localMin() - (localRay.origin)).unsafeDivide(localRay.direction)
        val tFar = (localMax() - (localRay.origin)).unsafeDivide(localRay.direction)

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
        if (tNear.x > tFar.y || tNear.y > tFar.x) // No intersection
            return null

        // Parametric distances to near and far contact along ray
        val tHitNear = (tNear.x).coerceAtLeast(tNear.y)
        val tHitFar = (tFar.x).coerceAtMost(tFar.y)
        // same functionality as above
//        val tHitNear = max(min(tNear.x, tFar.x), min(tNear.y, tFar.y))
//        val tHitFar = min(max(tNear.x, tFar.x), max(tNear.y, tFar.y))
        if (tHitFar < 0 || tHitNear > tHitFar) return null // Ray is pointing away

        // If ray starts inside shape, use far for contact
        val distToBox = if (tHitNear < 0) tHitFar else tHitNear

        // Contact point is past the ray limit
        if (localLimit > 0 && distToBox > localLimit) return null
        var normal = Vec2() // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = Vec2(-sign(localRay.direction.x), 0f)
        else if (tNear.x < tNear.y) // Vertical collision
            normal = Vec2(0f, -sign(localRay.direction.y))

        val contact = transform.toWorld(localRay.getPoint(distToBox))
        return RaycastResult(contact, normal.rotate(transform.rotation), (contact - ray.origin).len())
    }

    fun Vec2.unsafeDivide(v: Vec2): Vec2 {
        return Vec2(this.x / v.x, this.y / v.y)
    }

}