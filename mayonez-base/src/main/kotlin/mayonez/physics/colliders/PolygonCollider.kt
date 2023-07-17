package mayonez.physics.colliders

import mayonez.math.*
import mayonez.math.shapes.*

/**
 * A convex polygon made up of vertices connected by straight edges and
 * centered around the object's position.
 *
 * @author SlavSquatSuperstar
 */
open class PolygonCollider(shapeData: Polygon) :
    Collider(shapeData.translate(-shapeData.center())) {

    /**
     * Constructs a convex polygon from an array of vertices in clockwise
     * order. The vertices will then be translated so that the geometric center
     * becomes the object's position.
     *
     * @param vertices the vertices in clockwise order
     */
    constructor(vararg vertices: Vec2) : this(Polygon(*vertices))

    /**
     * Constructs a regular polygon with the specified number of vertices and
     * radius.
     *
     * @param sides the number of sides/vertices
     * @param radius the distance from the center to each vertex
     */
    constructor(sides: Int, radius: Float) : this(Polygon(Vec2(), sides, radius))

    // Shape Data

    val numVertices: Int = shapeData.numVertices

    // vertices transformed to world space
    open fun getVertices(): Array<Vec2> = (transformToWorld() as Polygon).vertices

}