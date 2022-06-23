package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.Mat22
import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2

/**
 * A shape made up of a finite number of vertices connected by straight edges.
 * This class is designed to model convex polygons, meaning all internal angles are less than 180 degrees,
 * and any line between two vertices is in the shape's interior or boundary.
 *
 * @author SlavSquatSuperstar
 */
// TODO correct self-intersecting
open class Polygon(vararg vertices: Vec2) : Shape() {

    /**
     * Constructs a regular polygon with the specified number of vertices and radius.
     *
     * @param sides the number of sides/vertices
     * @param radius the distance from the center to each vertex
     */
    constructor(center: Vec2, sides: Int, radius: Float) : this(*regularPolygonVertices(center, sides, radius))

    companion object {
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
         * @param angle the counterclockwise angle
         * @param center the center of rotation
         * @return the rotated vertex array
         */
        internal fun Array<Vec2>.rotate(angle: Float, center: Vec2): Array<Vec2> {
            val rot = Mat22(angle) // save rotation matrix
            return Array(size) { (rot * (this[it] - center)) + center }
        }

        /**
         * Rotates an array of vertices around a center.
         *
         * @param factor the scale factor
         * @param center the center for scaling
         * @return the scaled vertex array
         */
        internal fun Array<Vec2>.scale(factor: Vec2, center: Vec2): Array<Vec2> {
            return Array(size) { (factor * (this[it] - center)) + center }
        }

        private fun regularPolygonVertices(center: Vec2, sides: Int, radius: Float): Array<Vec2> {
            val start = Vec2(radius, 0f)
            val angle = 360f / sides
            return Array(sides) { center + start.rotate(angle * it) }
        }
    }

    /**
     * The points that define the edges of this polygon.
     */
    val vertices: Array<Vec2> = arrayOf(*vertices)

    /**
     * The edges that connect the vertices of this polygon.
     */
    protected val edges: Array<Vec2> = Array(numVertices) { vertices[(it + 1) % numVertices] - vertices[it] }

    // Polygon Properties

    /**
     * The number of vertices and edges of this polygon, n.
     */
    private val numVertices: Int
        get() = vertices.size

    /**
     * The least number of triangles this polygon can be divided into, equal to n–2.
     */
    private val numTriangles: Int
        get() = numVertices - 2

    /**
     * The sum of all the internal angles of this polygon, equal to (n-2)*180.
     */
    fun sumAngles(): Int = numTriangles * 180

    /**
     * Splits this polygon into n-2 triangular regions, which are guaranteed to be convex and non-overlapping.
     */
    fun getTriangles(): Array<Triangle> {
        val start = vertices[0]
        return Array(numTriangles) { Triangle(start, vertices[it + 1], vertices[it + 2]) }
    }

    // Shape Methods
    // TODO use fields or methods?

    override fun area(): Float = MathUtils.sum(*FloatArray(numVertices - 2) { getTriangles()[it].area() })

    override fun perimeter(): Float = MathUtils.sum(*FloatArray(numVertices) { edges[it].len() })

    /**
     * Finds the center of this polygon, equal to the average of the sub-triangle's centroids weighted by their proportional areas.
     *
     * Center formula: C_p = ∑A_i*C_i / A_p
     * Source: https://math.stackexchange.com/questions/90463/how-can-i-calculate-the-centroid-of-polygon
     */
    override fun center(): Vec2 {
        var center = Vec2()
        for (tri in getTriangles())
            center += tri.center() * tri.area()
        return center / this.area()
    }

    /**
     * Calculates the centroidal moment of inertia of this polygon,
     * equal to sum of the moment of inertia of each sub-triangle around this shape's centroid.
     */
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

    // Transformations

    override fun translate(direction: Vec2): Polygon = Polygon(*vertices.translate(direction))

    override fun rotate(angle: Float, origin: Vec2?): Polygon =
        Polygon(*vertices.rotate(angle, origin ?: this.center()))

    override fun scale(factor: Vec2, centered: Boolean): Polygon =
        Polygon(*vertices.scale(factor, if (centered) center() else Vec2()))

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
     * A description of the polygon in the form Polygon (x, y) vertices=[v1, v2, ..., vn]
     */
    override fun toString(): String = "Polygon ${center()} vertices=${vertices.contentToString()})"
}