package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.MathUtils
import slavsquatsuperstar.math.Vec2
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * A three-sided polygon capable of performing additional operations. A triangle is the simplest polynomial
 * (contains the least vertices).
 *
 * @author SlavSquatSuperstar
 */
class Triangle(v1: Vec2, v2: Vec2, v3: Vec2) : Polygon(v1, v2, v3) {

    constructor(vararg vertices: Vec2) : this(vertices[0], vertices[1], vertices[2])

    // Triangle Properties

    /**
     * The base length of the triangle, b.
     */
    @JvmField
    val base: Float = edges[0].len()

    /**
     * The height of the triangle, h.
     */
    @JvmField
    val height: Float = abs(edges[1].component(edges[0].getNormal())) // get altitude length

    /**
     * The area of the triangle using the shoelace formula, equal to 1/2*bh.
     */
    override fun area(): Float {
        return 0.5f * abs(
            vertices[0].cross(vertices[1]) + vertices[1].cross(vertices[2]) +
                    vertices[2].cross(vertices[0])
        )
    }

    /**
     * The center of the triangle, equal to the average position of its vertices, and two-thirds along any median.
     */
    override fun center(): Vec2 = (vertices[0] + vertices[1] + vertices[2]) * (1 / 3f)

    /**
     * The centroidal moment of inertia of the triangle, equal to 1/18*m(b^2 + h^2 + a^2 - ab).
     *
     * Second moment of area: I_x = 1/36*bh^3, I_y = 1/36(b^3*h - b^2*ha + bha^2)
     * Polar moment of area: J_z = 1/36*(b^3*h - b^2*ha + bha^2 + bh^3) = 1/18*(1/2*bh)(b^2 + h^2 + a^2 - ab)
     * = 1/18*A(b^2 + h^2 + a^2 - ab), where a is the offset of the vertex along the base from either end.
     */
    override fun angularMass(mass: Float): Float {
        val offset = base - sqrt(edges[2].lenSq() - height * height)
        return mass / 18f * (MathUtils.hypotSq(base, height) + offset * offset - offset * base)
    }

    // Transformations
    override fun translate(direction: Vec2): Triangle = super.translate(direction) as Triangle

    override fun rotate(angle: Float, origin: Vec2?): Triangle = super.rotate(angle, origin) as Triangle

    override fun scale(factor: Vec2, centered: Boolean): Triangle = super.scale(factor, centered) as Triangle

    // Overrides

    /**
     * Whether a point is in this triangle.
     */
    override fun contains(point: Vec2): Boolean {
        if (point in vertices) return true

        // divide triangle into three triangles
        // sum area and compare to original area
        val areas = FloatArray(3) { Triangle(vertices[it], vertices[(it + 1) % 3], point).area() }
        return MathUtils.sum(*areas) <= this.area()
    }

    override fun equals(other: Any?): Boolean {
        return (other is Triangle) && this.vertices.contentEquals(other.vertices)
    }

    /**
     * A description of the triangle in the form Triangle (v1, v2, v3)
     */
    override fun toString(): String = "Triangle ${vertices[0]}, ${vertices[1]}, ${vertices[2]}"

}