package slavsquatsuperstar.mayonez.physics.colliders

import slavsquatsuperstar.math.Mat22
import slavsquatsuperstar.math.MathUtils.inRange
import slavsquatsuperstar.math.MathUtils.max
import slavsquatsuperstar.math.MathUtils.min
import slavsquatsuperstar.math.MathUtils.minIndex
import slavsquatsuperstar.math.Vec2
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

}