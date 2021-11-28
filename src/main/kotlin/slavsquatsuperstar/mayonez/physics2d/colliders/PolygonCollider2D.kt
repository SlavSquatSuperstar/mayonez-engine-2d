package slavsquatsuperstar.mayonez.physics2d.colliders

import org.apache.commons.lang3.ArrayUtils
import slavsquatsuperstar.math.Mat22
import slavsquatsuperstar.math.MathUtils.inRange
import slavsquatsuperstar.math.MathUtils.max
import slavsquatsuperstar.math.MathUtils.maxIndex
import slavsquatsuperstar.math.MathUtils.min
import slavsquatsuperstar.math.MathUtils.minIndex
import slavsquatsuperstar.math.Range
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.Colors
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold
import slavsquatsuperstar.mayonez.physics2d.RaycastResult
import slavsquatsuperstar.mayonez.renderer.DebugDraw
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
open class PolygonCollider2D(vararg vertices: Vec2) : Collider2D() {

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

    /*
     * Projected min and max
     * Positive is in axis direction
     * (-) ---> (+)
     */
    protected fun getIntervalOnAxis(axis: Vec2): Range {
        val vertices = getVertices()
        val projections = FloatArray(vertices.size) { vertices[it].dot(axis) }
        return Range(min(*projections), max(*projections))
    }

    // Do the intervals intersect
    private fun hasOverlapOnAxis(polygon: PolygonCollider2D, axis: Vec2): Boolean {
        val thisInterval = this.getIntervalOnAxis(axis)
        val otherInterval = polygon.getIntervalOnAxis(axis)
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max)
    }

    // How much overlap on the axis
    protected fun getOverlapOnAxis(polygon: PolygonCollider2D, axis: Vec2): Float {
        val thisInterval = this.getIntervalOnAxis(axis)
        val otherInterval = polygon.getIntervalOnAxis(axis)
        if (!hasOverlapOnAxis(polygon, axis)) return 0f
        return abs(thisInterval.min - otherInterval.max).coerceAtMost(abs(otherInterval.min - thisInterval.max))
    }

    // How much separation on the axis
    // Positive means separation, and negative means overlap
    // TODO combine separation and overlap into one function (differentiate with positive and negative)
    private fun getSeparationOnAxis(polygon: PolygonCollider2D, axis: Vec2): Float {
        val normalFace = this.getIntervalOnAxis(axis).max
        val nearestVertex = polygon.getIntervalOnAxis(axis).min
        return nearestVertex - normalFace
    }


    // Polygon vs Point
    override fun contains(point: Vec2): Boolean {
        val edges = getEdges()
        for (edge in edges) {
            val edgeLine = edge.toVector()
            val projLength = point.sub(edge.start).projectedLength(edgeLine)
            if (!inRange(projLength, 0f, edge.length)) return false
        }
        return true
    }

    override fun nearestPoint(position: Vec2): Vec2? {
        if (position in this) return position
        val edges = getEdges()
        val distances = FloatArray(edges.size) { edges[it].distance(position) }
        val nearestEdge = edges[minIndex(*distances)]
        return nearestEdge.nearestPoint(position)
    }

    // Polygon vs Line
    override fun raycast(ray: Ray2D, limit: Float): RaycastResult? {
        val edges = getEdges()
        val distances = FloatArray(edges.size)
        for (i in distances.indices) {
            val rc = edges[i].raycast(ray, limit)
            distances[i] = rc?.distance ?: Float.MAX_VALUE
        }

        val minIndex = minIndex(*distances)
        val minDist = distances[minIndex]
        if (minDist == Float.MAX_VALUE) return null // no successful raycasts

        val normal = edges[minIndex].toVector().rotate(-90f).unit()
        return RaycastResult(ray.getPoint(minDist), normal, minDist)
    }

    // Polygon vs Shape

    override fun detectCollision(collider: Collider2D?): Boolean {
        return if (collider is PolygonCollider2D) detectCollision(collider)
        else getCollisionInfo(collider) != null
    }

    // Separating-Axis Theorem
    private fun detectCollision(polygon: PolygonCollider2D): Boolean {
        val axes = ArrayUtils.addAll(getNormals(), *polygon.getNormals())
        axes.forEach { axis -> if (!hasOverlapOnAxis(polygon, axis)) return false }
        return true
    }

    override fun getCollisionInfo(collider: Collider2D?): CollisionManifold? {
        return when (collider) {
            is CircleCollider -> getCollisionInfo(collider)
            is PolygonCollider2D -> getCollisionInfo(collider)
            else -> null
        }
    }

    // Polygon vs Circle: 1 contact point
    private fun getCollisionInfo(circle: CircleCollider): CollisionManifold? {
        val closestToCircle = nearestPoint(circle.center())
        if (closestToCircle!! !in circle) return null

        val depth = circle.radius() - closestToCircle.distance(circle.center())
        val normal = (circle.center() - closestToCircle).unit()
        val result = CollisionManifold(this, circle, normal, depth)
        result.addContact(closestToCircle - (normal * depth))
        return result
    }

    // Polygon vs Polygon: 1-2 contact points
    // SAT method, convex only
    private fun getCollisionInfo(poly: PolygonCollider2D): CollisionManifold? {
        // 1. Store edges and normals in structs and calculate overlaps
        val poly1 = SATPolygon(this, poly)
        val poly2 = SATPolygon(poly, this)

        // 2. Test for a separating axis
        if (poly1.minOverlap > 0 || poly2.minOverlap > 0) return null // positive separation = no intersection

        // 3. Set the polygon with lesser overlap as reference frame
        val reference: SATPolygon // the "stationary" shape
        val incident: SATPolygon // the "incoming" shape
        if (poly1.minOverlap >= poly2.minOverlap) {
            reference = poly1
            incident = poly2
        } else {
            incident = poly1
            reference = poly2
        }

        // 4. Calculate reference and incident edges
        val minOverlapIdx = reference.minIdx
//        val minIdxInc = incident.minIdx

        val overlap = reference.minOverlap
        val colNormal = reference.normals[minOverlapIdx]
        val refEdge = reference.edges[minOverlapIdx]
//        val incEdge = incident.edges[minIdxInc]

        // Get most negative dot product = edge normal facing towards collision normal the most
        // Sometimes overlap test isn't reliable
        val dotProds = FloatArray(incident.normals.size) { colNormal.dot(incident.normals[it]) }
        val minDotIdx = minIndex(*dotProds)
        val incEdge = incident.edges[minDotIdx]

        DebugDraw.drawVector(reference.collider.center(), reference.normals[minOverlapIdx], Colors.BLUE);
        DebugDraw.drawVector(incident.collider.center(), incident.normals[minDotIdx], Colors.RED);
        DebugDraw.drawLine(refEdge, Colors.BLUE)
        DebugDraw.drawLine(incEdge, Colors.RED)

        // 5. Calculate contact points
        val collision = CollisionManifold(reference.collider, incident.collider, colNormal, -overlap)
        val clippedEdge = incEdge.clipToSegment(refEdge)
        val normalFace = refEdge.start.dot(colNormal)
        for (pt in arrayOf(clippedEdge.start, clippedEdge.end))
            if (pt.dot(colNormal) <= normalFace) collision.addContact(pt)
        return collision
    }

    // Diagonals method, convex only
//    private fun getCollisionInfo2(polygon: PolygonCollider2D): CollisionManifold? {
//        val diagonals = arrayOf(getDiagonals(), polygon.getDiagonals())
//        val edges = arrayOf(polygon.getEdges(), getEdges())
//        var overlaps = FloatArray(this.countVertices() + polygon.countVertices())
//
//        var normalsAccum = Vec2()
//        var rc: RaycastResult?
//        // Raycast each shape's diagonals against other shape's edges
//        for (i in diagonals.indices) {
//            for (diag in diagonals[i]) {
//                for (side in edges[i]) {
//                    rc = side.raycast(Ray2D(diag), diag.length)
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

    // SAT Helper Class
    private class SATPolygon(val collider: PolygonCollider2D, other: PolygonCollider2D) {
        val edges = collider.getEdges()
        val normals = collider.getNormals()

        // Find axis with greatest separation (least negative overlap)
        val overlaps = FloatArray(normals.size) { collider.getSeparationOnAxis(other, normals[it]) }
        val minIdx = maxIndex(*overlaps)
        val minOverlap = overlaps[minIdx]
    }

}