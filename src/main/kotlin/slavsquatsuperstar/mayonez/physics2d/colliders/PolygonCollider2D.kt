package slavsquatsuperstar.mayonez.physics2d.colliders

import org.apache.commons.lang3.ArrayUtils
import slavsquatsuperstar.math.Mat22
import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.MathUtils.inRange
import slavsquatsuperstar.math.MathUtils.max
import slavsquatsuperstar.math.MathUtils.min
import slavsquatsuperstar.math.MathUtils.minIndex
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold
import slavsquatsuperstar.mayonez.physics2d.RaycastResult
import kotlin.math.abs

/**
 * A convex polygon with an arbitrary number of vertices connected by straight edges.
 *
 * @author SlavSquatSuperstar
 *
 * @constructor Constructs a convex polygon from an array of vertices in clockwise order.
 *
 * @param vertices the vertices in clockwise order
 */
abstract class PolygonCollider2D(vararg vertices: Vec2) : Collider2D() {

    // Shape Properties

    // points in local space (relative to center)
    private val vertices: Array<Vec2> = arrayOf(*vertices)

    // rotated in world
    open fun getVertices(): Array<Vec2> = Array(vertices.size) { toWorld(vertices[it]) }

    open fun countVertices(): Int = vertices.size

    open fun getRotation(): Float = if (transform != null) transform!!.rotation else 0f

    override fun getMinBounds(): AlignedBoxCollider2D { // TODO Support function
        val vertices = getVertices()
        val verticesX = FloatArray(vertices.size)
        val verticesY = FloatArray(vertices.size)

        // Get the min and max coordinates of any point on the box
        for (i in vertices.indices) {
            verticesX[i] = vertices[i].x
            verticesY[i] = vertices[i].y
        }
        val newMin = Vec2(min(*verticesX), min(*verticesY))
        val newMax = Vec2(max(*verticesX), max(*verticesY))
        val aabbSize = newMax.sub(newMin).div(transform!!.scale)
        return AlignedBoxCollider2D(aabbSize).setTransform<Collider2D>(transform).setRigidbody(rb)
    }

    // Intersection Helper Methods

    open fun getEdges(): Array<Edge2D> { // in world
        val vertices = getVertices()
        return Array(vertices.size) { Edge2D(vertices[it], vertices[(it + 1) % vertices.size]) }
    }

    open fun getNormals(): Array<Vec2> { // in world
        val edges = getEdges()
        val rot = Mat22(0f, 1f, -1f, 0f) // Rotate 90 degrees counterclockwise to point outward
        return Array(countVertices()) { (rot * edges[it].toVector()).unit() }
    }

    open fun getDiagonals(): Array<Edge2D> {
        val center = center()
        val vertices = getVertices()
        return Array(vertices.size) { Edge2D(vertices[it], center) }
    }

    // Separating-Axis Theorem Methods

    // Project vertices on the axis and find min and max
    protected fun getIntervalOnAxis(axis: Vec2?): MathUtils.Range {
        val vertices = getVertices()
        val projections = FloatArray(countVertices())
        for (i in projections.indices)
            projections[i] = vertices[i].dot(axis!!)
        return MathUtils.Range(min(*projections), max(*projections))
    }

    protected fun overlapOnAxis(polygon: PolygonCollider2D, axis: Vec2?): Boolean {
        val thisInterval = getIntervalOnAxis(axis)
        val otherInterval = polygon.getIntervalOnAxis(axis)
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max)
    }

    // Assume overlap on axis
    protected fun getAxisOverlap(polygon: PolygonCollider2D, axis: Vec2?): Float {
        val thisInterval = getIntervalOnAxis(axis)
        val otherInterval = polygon.getIntervalOnAxis(axis)
        return abs(thisInterval.min - otherInterval.max).coerceAtMost(abs(otherInterval.min - thisInterval.max))
    }

    // Polygon vs Point
    override fun contains(point: Vec2): Boolean {
        val edges = getEdges()
        for (edge in edges) {
            val edgeLine = edge.toVector()
            val projLength = point.sub(edge.start).projectedLength(edgeLine)
            if (!inRange(projLength, 0f, edge.length())) return false
        }
        return true
    }

    override fun nearestPoint(position: Vec2): Vec2? {
        if (contains(position))
            return position

        val edges = getEdges()
        val distances = FloatArray(edges.size)
        for (i in distances.indices)
            distances[i] = position.distance(edges[i].nearestPoint(position))

        val nearestEdge = edges[minIndex(*distances)]
        return nearestEdge.nearestPoint(position)
    }

    // Polygon vs Line
    override fun raycast(ray: Ray2D?, limit: Float): RaycastResult? {
        val edges = getEdges()
        val distances = FloatArray(edges.size)
        for (i in distances.indices) {
            val rc = edges[i].raycast(ray!!, limit)
            distances[i] = rc?.distance ?: Float.MAX_VALUE
        }

        val minIndex = minIndex(*distances)
        val minDist = distances[minIndex]
        if (minDist == Float.MAX_VALUE) // no successful raycasts
            return null

        val normal = edges[minIndex].toVector().rotate(-90f).unit()
        return RaycastResult(ray!!.getPoint(minDist), normal, minDist)
    }

    // Polygon vs Shape
    override fun detectCollision(collider: Collider2D?): Boolean {
        return if (collider is PolygonCollider2D) intersects(collider)
        else getCollisionInfo(collider) != null
    }

    // Separating-Axis Theorem
    private fun intersects(polygon: PolygonCollider2D): Boolean {
        val axes = ArrayUtils.addAll(getNormals(), *polygon.getNormals())
        for (axis in axes)
            if (!overlapOnAxis(polygon, axis))
                return false
        return true
    }

    override fun getCollisionInfo(collider: Collider2D?): CollisionManifold? {
        return when (collider) {
            is CircleCollider -> getCollisionInfo(collider)
//            is PolygonCollider2D -> getCollisionInfo(collider)
            else -> null
        }
    }

    private fun getCollisionInfo(circle: CircleCollider): CollisionManifold? {
        val closestToCircle = nearestPoint(circle.center())
        if (closestToCircle!! !in circle)
            return null

        val depth = circle.radius() - closestToCircle.distance(circle.center())
        val normal = (circle.center() - closestToCircle).unit()
        val result = CollisionManifold(this, circle, normal, depth)
        result.addContact(closestToCircle - (normal * depth))
        return result
    }

    // Diagonals method, convex only
//    private fun getCollisionInfo(polygon: PolygonCollider2D): CollisionManifold? {
//        val diagonals = arrayOf(getDiagonals(), polygon.getDiagonals())
//        val edges = arrayOf(polygon.getEdges(), getEdges())
//
//        var normalsAccum = Vec2()
//        var rc: RaycastResult?
//        for (i in diagonals.indices) {
//            for (diag in diagonals[i]) {
//                for (side in edges[i]) {
//                    rc = side!!.raycast(Ray2D(diag), diag!!.length())
//                    if (rc != null)
//                        normalsAccum = normalsAccum.add(rc.normal * rc.distance)
//                }
//            }
//        }
//        if (normalsAccum.lenSquared() == 0f)
//            return null
//
//        val depth = normalsAccum.len()
//        val result = CollisionManifold(this, polygon, normalsAccum / depth, depth)
//        result.addContact(this.center())
//        result.addContact(polygon.center())
//        return result
//    }

}