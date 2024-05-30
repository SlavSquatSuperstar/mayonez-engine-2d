package mayonez.math.shapes

import mayonez.math.*
import kotlin.math.*

/**
 * A shape made up of a finite number of vertices connected by straight
 * edges. This class is designed to model convex polygons, meaning all
 * internal angles are less than 180 degrees, and any line between two
 * vertices is in the shape's interior or boundary.
 *
 * @author SlavSquatSuperstar
 */
open class Polygon(sort: Boolean, vararg vertices: Vec2) : Shape() {

    // automatically sort the vertices
    constructor(vararg vertices: Vec2) : this(true, *vertices)

    /**
     * Constructs a regular polygon with the specified radius and number of
     * sides.
     *
     * @param center the center of the polygon
     * @param sides the number of sides and vertices
     * @param radius the distance from the center to each vertex
     */
    // don't sort the vertices
    constructor(center: Vec2, sides: Int, radius: Float) :
            this(false, *regularPolygonVertices(center, sides, radius))

    // Polygon Components

    /** The points that define the shape of this polygon. */
    val vertices: Array<Vec2> =
        if (sort) orderedConvexHull(arrayOf(*vertices))
        else arrayOf(*vertices)

    /** The number of vertices and edges of this polygon, n. */
    final override val numVertices: Int = this.vertices.size

    /** The edges that connect the vertices of this polygon. */
    val edges: Array<Edge> = this.vertices.toEdges()

    /** The faces, or outward unit normal vectors of each edge in this polygon. */
    val normals: Array<Vec2>
        get() = this.edges.map { it.unitNormalRight() }.toTypedArray()

    /**
     * Splits this polygon into n-2 triangular regions, which are guaranteed to
     * be convex and non-overlapping.
     */
    val triangles: Array<Triangle>
        get() {
            val start = vertices[0]
            return Array(numVertices - 2) {
                Triangle(start, vertices[it + 1], vertices[it + 2])
            }
        }

    // Shape Methods
    // TODO use fields or methods?

    /**
     * The area of the polygon, equal to the combined areas of all the
     * sub-triangles. This function specifically uses the shoelace
     * formula (Gauss's area formula), a special case of Green's theorem.
     */
    override fun area(): Float {
        val crosses = FloatArray(numVertices) {
            vertices[it].cross(vertices[(it + 1) % numVertices])
        }
        return 0.5f * abs(crosses.sum())
    }

    private lateinit var centroid: Vec2

    /**
     * Finds the centroid of this polygon, assuming the vertices are in sorted
     * order. equal to the average of the sub-triangle's centroids weighted by
     * their proportional areas.
     *
     * Centroid formula: C = 1/6A*∑(v_i + v_i+1)(v_i × v_i+1), where
     * A is the signed area using the shoelace formula. Source:
     * https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
     */
    override fun center(): Vec2 {
        if (!this::centroid.isInitialized) {
            var signedArea = 0f
            var center = Vec2()
            for (i in vertices.indices) {
                val next = (i + 1) % numVertices
                val cross = vertices[i].cross(vertices[next])
                signedArea += cross
                center += (vertices[i] + vertices[next]) * cross
            }
            centroid = center / (3f * signedArea)
        }
        return centroid
    }

    // Bounds

    override fun boundingCircle(): Circle {
        val distsSq = vertices.map { it.distanceSq(centroid) }.toFloatArray()
        return Circle(center(), MathUtils.max(*distsSq))
    }

    override fun boundingRectangle(): BoundingBox {
        val verticesX = vertices.map { it.x }.toFloatArray()
        val verticesY = vertices.map { it.y }.toFloatArray()
        val boxMin = Vec2(MathUtils.min(*verticesX), MathUtils.min(*verticesY))
        val boxMax = Vec2(MathUtils.max(*verticesX), MathUtils.max(*verticesY))
        return BoundingBox.fromMinAndMax(boxMin, boxMax)
    }

    // Polygon vs Point

    override fun supportPoint(direction: Vec2): Vec2 {
        val dotProds = vertices.map { it.dot(direction) }.toFloatArray()
        return vertices[MathUtils.maxIndex(*dotProds)]
    }

    override fun nearestPoint(position: Vec2): Vec2 {
        if (position in this) return position

        val distances = edges.map { it.distance(position) }.toFloatArray()
        val nearestEdge = edges[MathUtils.minIndex(*distances)]
        return nearestEdge.nearestPoint(position)
    }

    // Transformations

    override fun translate(direction: Vec2): Polygon {
        return Polygon(*vertices.translate(direction))
    }

    override fun rotate(angle: Float, origin: Vec2?): Polygon {
        return Polygon(*vertices.rotate(angle, origin ?: this.center()))
    }

    override fun scale(factor: Vec2, origin: Vec2?): Polygon {
        return Polygon(*vertices.scale(factor, origin ?: this.center()))
    }

    // Physical Properties

    /**
     * Calculates the centroidal moment of inertia of this polygon using
     * geometric decomposition. The polygon's MOI is equal to the sum
     * of the MOI of each sub-triangle around this shape's centroid.
     */
    // TODO write unit tests
    override fun angularMass(mass: Float): Float {
        var angMass = 0f
        for (tri in triangles) {
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

    // Overrides

    /**
     * Whether the point is inside the polygon. Results are only guaranteed for
     * convex polygons.
     *
     * @param point the point to check
     * @return the scaled shape
     */
    override fun contains(point: Vec2): Boolean {
        if (point in vertices) return true
        // Check if point is "inside" or on each edge
        for (edge in edges) {
            val vecAB = edge.toVector() // Start to end
            val vecAP = point - edge.start // Start to point
            val side = vecAP.cross(vecAB)
            if (side > 0) return false // Outside of edge
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        return (other is Polygon) && orderedVertices(this.vertices)
            .contentEquals(orderedVertices(other.vertices))
    }

    override fun hashCode(): Int = vertices.contentDeepHashCode()

    /**
     * A description of the polygon in the form Polygon (x, y) Vertices:
     * [v1, v2, ..., vn]
     */
    override fun toString(): String = "Polygon ${center()} Vertices: ${vertices.contentToString()})"

    // Helper Methods

    companion object {

        // Vertices Methods

        /**
         * Generate an array of vertices for a regular polygon, ordered
         * counterclockwise. If the number of sides is insufficient (under 3), then
         * it will be set to 3.
         *
         * @param center the center of the polygon
         * @param sides how many sides/vertices the polygon has
         * @param radius how far each vertex is from the center
         * @return the array of vertices, at least 3
         */
        @JvmStatic
        fun regularPolygonVertices(center: Vec2, sides: Int, radius: Float): Array<Vec2> {
            if (sides < MIN_VERTICES_COUNT) {
                return regularPolygonVertices(center, MIN_VERTICES_COUNT, radius)
            }
            val start = Vec2(radius, 0f)
            val angle = 360f / sides
            return Array(sides) { center + start.rotate(angle * it) }
        }

        private fun Array<Vec2>.toEdges(): Array<Edge> {
//            return Array(size) { Edge(this[(it + 1) % size], this[it]) }
            return Array(size) { Edge(this[it], this[(it + 1) % size]) }
        }

    }

}
