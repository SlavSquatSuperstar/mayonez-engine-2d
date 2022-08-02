package slavsquatsuperstar.mayonez.physics.shapes

import slavsquatsuperstar.math.Mat22
import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import kotlin.math.abs

/**
 * A shape made up of a finite number of vertices connected by straight edges. This class is designed to model convex
 * polygons, meaning all internal angles are less than 180 degrees, and any line between two vertices is in the shape's
 * interior or boundary.
 *
 * @author SlavSquatSuperstar
 */
// TODO correct self-intersecting and set convex hull
open class Polygon(vararg vertices: Vec2) : Shape() {

    /**
     * Constructs a regular polygon with the specified number of vertices and radius.
     *
     * @param sides  the number of sides/vertices
     * @param radius the distance from the center to each vertex
     */
    constructor(center: Vec2, sides: Int, radius: Float) : this(*regularPolygonVertices(center, sides, radius))

    // Polygon Components

    /**
     * The points that define the shape of this polygon.
     */
    val vertices: Array<Vec2> = arrayOf(*vertices)

    /**
     * The number of vertices and edges of this polygon, n.
     */
    final override val numVertices: Int = vertices.size

    /**
     * The edges that connect the vertices of this polygon.
     */
    val edges: Array<Edge> = Array(numVertices) {Edge(vertices[it], vertices[it.next(numVertices)]) }

    /**
     * The faces, or unit normal vectors of each edge in this polygon.
     */
    val normals: Array<Vec2> = Array(numVertices) { edges[it].unitNormal() }

    /**
     * Splits this polygon into n-2 triangular regions, which are guaranteed to be convex and non-overlapping.
     */
    private fun getTriangles(): Array<Triangle> {
        val start = vertices[0]
        return Array(numVertices - 2) { Triangle(start, vertices[it + 1], vertices[it + 2]) }
    }

    // Shape Methods
    // TODO use fields or methods?

    /**
     * The area of the polygon, equal to the combined areas of all the sub-triangles. This function specifically uses
     * the shoelace formula (Gauss's area formula), a special case of Green's theorem.
     */
    override fun area(): Float {
        val crosses = FloatArray(numVertices) { vertices[it].cross(vertices[it.next(numVertices)]) }
        return 0.5f * abs(MathUtils.sum(*crosses))
    }

    private lateinit var centroid: Vec2

    /**
     * Finds the centroid of this polygon, assuming the vertices are in sorted order.
     * equal to the average of the sub-triangle's centroids weighted by their proportional areas.
     *
     * Centroid formula: C = 1/6A*∑(v_i + v_i+1)(v_i × v_i+1), where A is the signed area using the shoelace formula.
     * Source: https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
     */
    override fun center(): Vec2 {
        if (!this::centroid.isInitialized) {
            var signedArea = 0f
            var center = Vec2()
            for (i in vertices.indices) {
                val next = i.next(numVertices)
                val cross = vertices[i].cross(vertices[next])
                signedArea += cross
                center += (vertices[i] + vertices[next]) * cross
            }
            centroid = center / (3f * signedArea)
        }
        return centroid
    }

    /**
     * Calculates the centroidal moment of inertia of this polygon using geometric decomposition. The polygon's MOI is
     * equal to the sum of the MOI of each sub-triangle around this shape's centroid.
     */
    // TODO write unit tests
    override fun angularMass(mass: Float): Float {
        var angMass = 0f
        for (tri in getTriangles()) {
            /*
             * Parallel axis theorem: I_new = I_cm + md^2
             * I_p = ∑(I_i + m_i*d_i^2)
             * I_i = m_i*kr^2
             * m_i = m_p/A_p * A_i
             * I_p = m_p/A_p * ∑(A_i*(kr^2 + d_i^2))
             */
            val triI = tri.angularMass(1f)
            val distSq = tri.center().distanceSq(this.center())
            val newI = tri.area() * (triI + distSq)
            angMass += newI
        }
        return angMass * mass / this.area()
//        return angMass
    }

    // Collision Properties

    override fun boundingCircle(): Circle {
        val distsSq = FloatArray(numVertices) { vertices[it].distanceSq(center()) }
        return Circle(center(), MathUtils.max(*distsSq))
    }

    override fun boundingRectangle(): Rectangle {
        // TODO support function, or too expensive?
        val verticesX = FloatArray(numVertices) { vertices[it].x }
        val verticesY = FloatArray(numVertices) { vertices[it].y }

        val boxMin = Vec2(MathUtils.min(*verticesX), MathUtils.min(*verticesY))
        val boxMax = Vec2(MathUtils.max(*verticesX), MathUtils.max(*verticesY))
        return Rectangle(boxMin.midpoint(boxMax), boxMax - boxMin)
    }

    override fun supportPoint(direction: Vec2): Vec2 {
        val dotProds = FloatArray(numVertices) { vertices[it].dot(direction) }
        return vertices[MathUtils.maxIndex(*dotProds)]
    }

    // Transformations

    override fun translate(direction: Vec2): Polygon = Polygon(*vertices.translate(direction))

    override fun rotate(angle: Float, origin: Vec2?): Polygon =
        Polygon(*vertices.rotate(angle, origin ?: this.center()))

    override fun scale(factor: Vec2, origin: Vec2?): Polygon =
        Polygon(*vertices.scale(factor, origin ?: this.center()))

    // Overrides

    /**
     * Whether the point is inside the polygon. Results only guaranteed for convex polygons.
     */
    override fun contains(point: Vec2): Boolean {
        if (point in vertices) return true
        // Split polygon into triangles and test each
        for (tri in getTriangles())
            if (tri.contains(point)) return true
        return false
    }

    override fun equals(other: Any?): Boolean {
        return (other is Polygon) && this.vertices.contentEquals(other.vertices)
    }

    /**
     * A description of the polygon in the form Polygon (x, y) Vertices: [v1, v2, ..., vn]
     */
    override fun toString(): String = "Polygon ${center()} Vertices: ${vertices.contentToString()})"

    // Helper Methods

    /**
     * Finds the next index in an array, looping back to zero if the length is exceeded.
     *
     * @param length the length of the array
     * @return the next index
     */
    private fun Int.next(length: Int): Int = if (this < length - 1) this + 1 else (this + 1) % length

    companion object {
        // Polygon Constructors
        /**
         * Rotates an array of vertices around a center.
         *
         * @param direction the direction of translation
         * @return the translated vertex array
         */
        internal fun Array<Vec2>.translate(direction: Vec2): Array<Vec2> {
            return Array(size) { this[it] + direction }
        }

        /**
         * Rotates an array of vertices around a center.
         *
         * @param angle  the counterclockwise angle
         * @param center the center of rotation
         * @return the rotated vertex array
         */
        internal fun Array<Vec2>.rotate(angle: Float, center: Vec2): Array<Vec2> {
            val rot = Mat22(angle) // save rotation matrix
            return Array(size) { (rot * (this[it] - center)) + center }
        }

        /**
         * Scales an array of vertices from a center.
         *
         * @param factor the scale factor
         * @param center the center for scaling
         * @return the scaled vertex array
         */
        internal fun Array<Vec2>.scale(factor: Vec2, center: Vec2): Array<Vec2> {
            return Array(size) { this[it].scale(factor, center) }
        }

        private fun regularPolygonVertices(center: Vec2, sides: Int, radius: Float): Array<Vec2> {
            val start = Vec2(radius, 0f)
            val angle = 360f / sides
            return Array(sides) { center + start.rotate(angle * it) }
        }

        // Templates

        /**
         * Constructs a polygon with the shape of a rotatable axis-aligned rectangle.
         *
         * @param center the rectangle's center
         * @param size   the rectangle's dimensions
         * @return the rectangle
         */
        @JvmStatic
        fun rectangle(center: Vec2, size: Vec2): Polygon = rectangle(center, size, 0f)

        /**
         * Constructs a polygon with the shape of a rotated rectangle.
         *
         * @param center the rectangle's center
         * @param size   the rectangle's dimensions
         * @param angle  the rectangle's rotated
         * @return the rotated rectangle
         */
        @JvmStatic
        fun rectangle(center: Vec2, size: Vec2, angle: Float): Polygon =
            Polygon(*Rectangle.rectangleVertices(center, size, angle = angle))
    }
}