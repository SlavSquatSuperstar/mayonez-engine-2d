package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Mat22
import slavsquatsuperstar.math.MathUtils.inRange
import slavsquatsuperstar.math.MathUtils.max
import slavsquatsuperstar.math.MathUtils.maxIndex
import slavsquatsuperstar.math.MathUtils.min
import slavsquatsuperstar.math.MathUtils.minIndex
import slavsquatsuperstar.math.Range
import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo
import slavsquatsuperstar.mayonez.physics.shapes.Edge
import slavsquatsuperstar.mayonez.physics.shapes.Polygon
import slavsquatsuperstar.mayonez.physics.shapes.Rectangle

/**
 * A convex polygon with an arbitrary number of vertices connected by straight edges.
 *
 * @constructor Constructs a polygon from a [Polygon] object
 * @param shapeData the shape object that stores the vertices and the shape's properties
 *
 * @author SlavSquatSuperstar
 */
open class PolygonCollider protected constructor(shapeData: Polygon) :
    Collider(shapeData.translate(-shapeData.center())) {

    /**
     * Constructs a convex polygon from an array of vertices in clockwise order. The vertices will then
     * be translated so that the geometric center becomes the object's position.
     *
     * @param vertices the vertices in clockwise order
     */
    constructor(vararg vertices: Vec2) : this(Polygon(*vertices))

    /**
     * Constructs a regular polygon with the specified number of vertices and radius.
     *
     * @param sides the number of sides/vertices
     * @param radius the distance from the center to each vertex
     */
    constructor(sides: Int, radius: Float) : this(Polygon(Vec2(), sides, radius))

    // Shape Data

    // points in local space, relative to center
    private val vertexData: Array<Vec2> = shapeData.vertices

    val numVertices: Int
        get() = vertexData.size

    // vertices transformed to world space
    open fun getVertices(): Array<Vec2> = (transformToWorld() as Polygon).vertices

    override fun getMinBounds(): Rectangle {
        val vertices = getVertices()
        // Get the min and max coordinates of any point on the box
        val verticesX = FloatArray(numVertices) { vertices[it].x }
        val verticesY = FloatArray(numVertices) { vertices[it].y }

        val newMin = Vec2(min(*verticesX), min(*verticesY))
        val newMax = Vec2(max(*verticesX), max(*verticesY))
        val aabbSize = (newMax - newMin) / (transform!!.scale)
        return Rectangle(center(), aabbSize)
    }

    // Intersection Helper Methods

    open fun getEdges(): Array<Edge> { // in world
        val vertices = getVertices()
        return Array(numVertices) { Edge(vertices[it], vertices[(it + 1) % numVertices]) }
    }

    open fun getNormals(): Array<Vec2> { // in world
        val rot = Mat22(0f, 1f, -1f, 0f) // Rotate 90 degrees counterclockwise to point outward
        return Array(numVertices) { (rot * getEdges()[it].toVector()).unit() }
    }

    // Separating-Axis Theorem Methods

    /*
     * Projected min and max
     * Positive is in axis direction
     * (-) ---> (+)
     */
    private fun getIntervalOnAxis(axis: Vec2): Range {
        val vertices = getVertices()
        val projections = FloatArray(numVertices) { vertices[it].dot(axis) }
        return Range(min(*projections), max(*projections))
    }

    // Do the intervals intersect
    private fun hasOverlapOnAxis(polygon: PolygonCollider, axis: Vec2): Boolean {
        val thisInterval = this.getIntervalOnAxis(axis)
        val otherInterval = polygon.getIntervalOnAxis(axis)
        return (thisInterval.min <= otherInterval.max) && (otherInterval.min <= thisInterval.max)
    }

    // How much separation on the axis
    // Positive means separation, and negative means overlap
    // TODO combine separation and overlap into one function (differentiate with positive and negative)
    private fun getSeparationOnAxis(polygon: PolygonCollider, axis: Vec2): Float {
        val normalFace = this.getIntervalOnAxis(axis).max
        val nearestVertex = polygon.getIntervalOnAxis(axis).min
        return nearestVertex - normalFace
    }

    // Polygon vs Point

    override fun contains(point: Vec2): Boolean {
        val edges = getEdges()
        for (edge in edges) {
            val edgeLine = edge.toVector()
            val projLength = point.sub(edge.start).component(edgeLine)
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

    // Polygon vs Shape

    override fun detectCollision(collider: Collider?): Boolean {
        return if (collider is PolygonCollider) detectCollision(collider)
        else getCollisionInfo(collider) != null
    }

    // Separating-Axis Theorem

    private fun detectCollision(polygon: PolygonCollider): Boolean {
        val axes = getNormals().plus(polygon.getNormals())
        axes.forEach { axis -> if (!hasOverlapOnAxis(polygon, axis)) return false }
        return true
    }

    override fun getCollisionInfo(collider: Collider?): CollisionInfo? {
        return when (collider) {
            is CircleCollider -> getCollisionInfo(collider)
            is PolygonCollider -> getCollisionInfo(collider)
            else -> null
        }
    }

    // Polygon vs Circle: 1 contact point
    private fun getCollisionInfo(circle: CircleCollider): CollisionInfo? {
        val closestToCircle = nearestPoint(circle.center())
        if (closestToCircle!! !in circle) return null

        val depth = circle.radius - closestToCircle.distance(circle.center())
        val normal = (circle.center() - closestToCircle).unit()
        val result = CollisionInfo(this, circle, normal, depth)
        result.addContact(closestToCircle - (normal * depth))
        return result
    }

    // Polygon vs Polygon: 1-2 contact points
    // SAT method, convex only
    private fun getCollisionInfo(poly: PolygonCollider): CollisionInfo? {
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

        // 5. Calculate contact points
        val collision = CollisionInfo(reference.collider, incident.collider, colNormal, -overlap)
        val clippedEdge = incEdge.clipToSegment(refEdge)
        val normalFace = refEdge.start.dot(colNormal)

//        DebugDraw.drawVector(reference.collider.center(), reference.normals[minOverlapIdx], Colors.BLUE);
//        DebugDraw.drawVector(incident.collider.center(), incident.normals[minDotIdx], Colors.RED);
//        DebugDraw.drawLine(refEdge, Colors.BLUE)
//        DebugDraw.drawLine(clippedEdge, Colors.RED)

        for (pt in arrayOf(clippedEdge.start, clippedEdge.end))
            if (pt.dot(colNormal) <= normalFace) collision.addContact(pt)
        return collision
    }

    // SAT Helper Class
    private class SATPolygon(val collider: PolygonCollider, other: PolygonCollider) {
        val edges = collider.getEdges()
        val normals = collider.getNormals()

        // Find axis with greatest separation (least negative overlap)
        val overlaps = FloatArray(normals.size) { collider.getSeparationOnAxis(other, normals[it]) }
        val minIdx = maxIndex(*overlaps)
        val minOverlap = overlaps[minIdx]
    }

}