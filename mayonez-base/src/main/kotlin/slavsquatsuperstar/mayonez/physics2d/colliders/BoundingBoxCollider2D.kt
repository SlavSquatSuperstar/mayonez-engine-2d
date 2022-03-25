package slavsquatsuperstar.mayonez.physics2d.colliders

import slavsquatsuperstar.math.Vec2


/**
 * An axis-aligned bounding box (AABB), a rectangle that is never rotated. The sides will always align with the x and y
 * axes.
 *
 * @author SlavSquatSuperstar
 */
class BoundingBoxCollider2D(size: Vec2) : BoxCollider2D(size) {

    // Shape Properties

    // min in world space
    fun min(): Vec2 = center() - (size() * 0.5f)

    // max in world space
    fun max(): Vec2 = center() + (size() * 0.5f)

    override fun getRotation(): Float = 0f

    override fun getVertices(): Array<Vec2> =
        arrayOf(Vec2(min()), Vec2(max().x, min().y), Vec2(max()), Vec2(min().x, max().y))

//    override fun getNormals(): Array<Vec2> = arrayOf(Vec2(1f, 0f), Vec2(0f, 1f))

    override fun getMinBounds(): BoundingBoxCollider2D =
        BoundingBoxCollider2D(localSize()).setTransform<Collider2D>(transform).setRigidbody(rb)

    override fun toLocal(world: Vec2): Vec2 = (world - center()) / (transform?.scale ?: Vec2(1f, 1f))

    override fun toWorld(local: Vec2): Vec2 = (local * (transform?.scale ?: Vec2(1f, 1f))) + center()

    // AABB vs Point

    override fun contains(point: Vec2): Boolean = point.inRange(min(), max())

    override fun nearestPoint(position: Vec2): Vec2 =
        if (position in this) position else position.clampInbounds(min(), max())

}