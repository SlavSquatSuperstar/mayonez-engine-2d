package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import kotlin.math.abs

class Triangle(v1: Vec2, v2: Vec2, v3: Vec2) : Shape() {

    val vertices: Array<Vec2>
    private val edges: Array<Vec2>

    init {
        vertices = arrayOf(v1, v2, v3)
        edges = Array(vertices.size) { vertices[(it + 1) % vertices.size] - vertices[it] }
    }

    /**
     * The base length of the triangle, b.
     */
    fun base(): Float = edges[0].len()

    /**
     * The height of the triangle, h.
     */
    fun height(): Float {
        val altitude = edges[0].getNormal()
        return abs(edges[1].component(altitude))
    }

    /**
     * The area of the triangle, equal to 1/2*bh.
     */
    override fun area(): Float {
        return abs(0.5f * (vertices[0].x * (edges[1].y) + vertices[1].x * (edges[2].y) + vertices[2].x * (edges[0].y)))
    }

    override fun perimeter(): Float {
        var perimeter = 0f
        for (v in vertices)
            perimeter += v.len()
        return perimeter
    }

    /**
     * The center of the triangle, equal to 2/3 along any median.
     */
    override fun center(): Vec2 {
        val midpoint = vertices[0] + edges[0] * 0.5f // midpoint of base
        val median = midpoint - edges[2] // vertex opposite base to midpoint
        return edges[2] + median * (2 / 3f) // 2/3 way along median
    }

    /**
     * The centroidal moment of inertia of the triangle, equal to 1/18*m(b^2 + h^2).
     *
     * Second moment of area: I_z = 1/36*(hb^3 + bh^3) = 1/18*A(b^2 + h^2)
     */
    override fun angMass(mass: Float): Float = 1 / 18f * mass * MathUtils.hypotSq(base(), height())

    /**
     * Whether the point is in the triangle.
     *
     * Source: https://www.youtube.com/watch?v=HYAgJN3x4GA
     */
    override fun contains(point: Vec2): Boolean {
        if (point in vertices) return true

        // divide triangle into three triangles
        // sum area and compare to original area
        val areas = FloatArray(3) { Triangle(vertices[it], vertices[(it + 1) % 3], point).area() }
        return MathUtils.sum(*areas) <= this.area()

        /*
         * Triangle = A, B, C
         * Point = P
         * Ray 1: R1 = B - A
         * Ray 2: R2 = C - A
         * Dist: D = P - A
         *
         * w1 = (Dy*R2x - Dx*R2y)/(R1y*R2x - R1x*R2y)
         * w1 = det(D, R2) / det(R1, R2)
         * w2 = (Dy - w1*R1y)/R2y
         *
         * w1 >= 0, w2 >= 0, w1 + w2 <= 0
         */
//        val start = vertices[0] // pick a vertex
//        val edge1 = vertices[1] - start // first edge
//        val edge2 = vertices[2] - start // second edge
//        val dist = point - start
//        val weight1 = Mat22(dist, edge2).determinant() / Mat22(edge1, edge2).determinant()
//        val weight2 = (dist.y - weight1 * edge1.y) / edge2.y
//        // problem if edge2.y = 0
//
//        return (weight1 >= 0) && (weight2 >= 0) && (weight1 + weight2 <= 1)
    }

    override fun equals(other: Any?): Boolean {
        return (other is Triangle) && this.vertices.contentEquals(other.vertices)
    }

    /**
     * A description of the circle in the form Circle (v1, v2, v3)
     */
    override fun toString(): String = "Triangle ${vertices[0]}, ${vertices[1]}, ${vertices[2]}"

}