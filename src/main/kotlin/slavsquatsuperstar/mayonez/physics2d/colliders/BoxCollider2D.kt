package slavsquatsuperstar.mayonez.physics2d.colliders

import org.apache.commons.lang3.ArrayUtils
import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold
import slavsquatsuperstar.mayonez.physics2d.RaycastResult
import kotlin.math.sign

/**
 * An oriented bounding box (OBB), a rectangle that can be rotated. The sides will align with the object's getRotation()
 * angle.
 *
 * @author SlavSquatSuperstar
 */
open class BoxCollider2D private constructor(min: Vec2, max: Vec2) :
    PolygonCollider2D(Vec2(min), Vec2(max.x, min.y), Vec2(max), Vec2(min.x, max.y)) {

    private val size: Vec2 = max - min

    constructor(size: Vec2) : this(size * -0.5f, size * 0.5f)

    // Shape Properties

    /**
     * Calculates the dimensions of this box factoring in the object's scale.
     *
     * @return the size in world space
     */
    fun size(): Vec2 = if (transform != null) size * transform!!.scale else Vec2(1f, 1f)

    fun width(): Float = size().x

    fun height(): Float = size().y

    /**
     * Returns the unscaled size of this box.
     *
     * @return the size in local space
     */
    // TODO parent send scale event to modify size directly
    protected fun localSize(): Vec2 = size

    // unrotated top left in local space
    protected fun localMin(): Vec2 = size * -0.5f

    // unrotated bottom right in local space
    protected fun localMax(): Vec2 = size * 0.5f

    override fun getNormals(): Array<Vec2> =
        arrayOf(Vec2(1f, 0f).rotate(getRotation()), Vec2(0f, 1f).rotate(getRotation()))

    // OBB vs Point

    override fun contains(point: Vec2): Boolean {
        val pointLocal = toLocal(point) // Rotate the point into the box's local space
        return MathUtils.inRange(pointLocal.x, localMin().x, localMax().x)
                && MathUtils.inRange(pointLocal.y, localMin().y, localMax().y)
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        // Transform the point into local space, clamp it, and then transform it back
        return if (position in this) position
        else toWorld(toLocal(position).clampInbounds(localMin(), localMax()))
    }

    // OBB vs Line

    override fun raycast(ray: Ray2D?, limit: Float): RaycastResult? {
        // Transform the ray to local space (but just rotate direction)
        val localRay = Ray2D(toLocal(ray!!.origin), ray.direction.rotate(-getRotation()))
        val localLimit = ((ray.direction * limit) / transform!!.scale).len()

        // Parametric distance to min/max x and y axes of box
        val tNear = (localMin() - (localRay.origin)) / localRay.direction
        val tFar = (localMax() - (localRay.origin)) / localRay.direction

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

        // Parametric distances to near and far contact
        val tHitNear = (tNear.x).coerceAtLeast(tNear.y)
        val tHitFar = (tFar.x).coerceAtMost(tFar.y)
//        val tHitNear = Math.max(Math.min(tNear.x, tFar.x), Math.min(tNear.y, tFar.y));
//        val tHitFar = Math.min(Math.max(tNear.x, tFar.x), Math.max(tNear.y, tFar.y));
        if (tHitFar < 0 || tHitNear > tHitFar) // Ray is pointing away
            return null

        // If ray starts inside shape, use far for contact
        val distToBox = if (tHitNear < 0) tHitFar else tHitNear

        // Is the contact point past the ray limit?
        if (localLimit > 0 && distToBox > localLimit) return null
        var normal = Vec2() // Use (0, 0) for diagonal collision
        if (tNear.x > tNear.y) // Horizontal collision
            normal = Vec2(-sign(localRay.direction.x), 0f)
        else if (tNear.x < tNear.y) // Vertical collision
            normal = Vec2(0f, -sign(localRay.direction.y))

        val contact = toWorld(localRay.getPoint(distToBox))
        return RaycastResult(contact, normal.rotate(getRotation()), (contact - ray.origin).len())
    }

    // OBB vs Shape

    override fun getCollisionInfo(collider: Collider2D?): CollisionManifold? {
        return when (collider) {
            is BoxCollider2D -> getCollisionInfo(collider)
            else -> super.getCollisionInfo(collider)
        }
    }

    private fun getCollisionInfo(box: BoxCollider2D): CollisionManifold? {
        val normalsA = this.getNormals()
        val normalsB = box.getNormals()
        if (!detectCollision(box))
            return null

        // SAT: Find axis with minimum overlap
        val axes = ArrayUtils.addAll(normalsA, *normalsB)
        val overlaps = FloatArray(axes.size)
        for (i in overlaps.indices) overlaps[i] = getAxisOverlap(box, axes[i])
        val minOverlapIndex = MathUtils.minIndex(*overlaps)

        val overlap = overlaps[minOverlapIndex]
        val axis = axes[minOverlapIndex]
        val dist = box.center() - this.center()
        val normal = dist.project(axis).unit()
        val side = normal.rotate(90f)
        val penetration = MathUtils.Range(box.getIntervalOnAxis(normal).min, getIntervalOnAxis(normal).max)

        val collision = CollisionManifold(this, box, normal, overlap)

        if (MathUtils.equals(
                this.getRotation() % 90,
                box.getRotation() % 90
            )
        ) { // Orthogonal intersection = 2 contact points
            val sideA = getIntervalOnAxis(side) // vertices projected onto side normal
            val sideB = box.getIntervalOnAxis(side)
            val collisionFace = MathUtils.Range(sideA.min.coerceAtLeast(sideB.min), sideA.max.coerceAtMost(sideB.max))

            val faceA = normal * penetration.min
            collision.addContact(faceA + (side * collisionFace.max))
            collision.addContact(faceA + (side * collisionFace.min))
        } else { // Diagonal intersection = 1 contact point
            val dotWithAxis = normal.dot(Vec2(1f, 0f).rotate(getRotation()))
            // Other box is inside this, find the furthest vertex inside this box
            if (MathUtils.equals(dotWithAxis, 0f) || MathUtils.equals(dotWithAxis, 1f) ||
                MathUtils.equals(dotWithAxis, -1f)) {
//            if (ArrayUtils.contains(normalsA, normal)) {
                val vertices = box.getVertices()
                val projections = FloatArray(vertices.size)
                for (i in projections.indices)
                    projections[i] = vertices[i].dot(normal)
                val minProjIndex = MathUtils.minIndex(*projections)
                val height = vertices[minProjIndex].projectedLength(side)

                collision.addContact((normal * penetration.min) + (side * height))
            } else { // This box inside other box, find vertex furthest inside other box
                val vertices = getVertices()
                val projections = FloatArray(vertices.size)
                for (i in projections.indices)
                    projections[i] = vertices[i].dot(normal)
                val maxProjIndex = MathUtils.maxIndex(*projections)
                val height = vertices[maxProjIndex].projectedLength(side)

                collision.addContact((normal * penetration.max) + (side * height))
            }
        }
        return collision
    }

}