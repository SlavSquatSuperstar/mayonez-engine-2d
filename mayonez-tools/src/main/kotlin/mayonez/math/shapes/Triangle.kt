package mayonez.math.shapes

import mayonez.math.*
import java.util.*
import kotlin.math.*

/**
 * A three-sided polygon capable of performing additional operations. A
 * triangle is the simplest polygon, containing three vertices. It is also
 * the two-dimensional simplex, formed when two solid figures intersect.
 *
 * @author SlavSquatSuperstar
 */
class Triangle(private val v1: Vec2, private val v2: Vec2, private val v3: Vec2) :
    Polygon(false, *triangleVertices(v1, v2, v3)) {

    private constructor(vararg vertices: Vec2) : this(vertices[0], vertices[1], vertices[2])

    // Triangle Properties

    /** The base length of the triangle, b. */
    val base: Float = edges[0].length

    /** The height of the triangle, h. */
    val height: Float = abs(edges[1].toVector().component(edges[0].unitNormalLeft())) // get altitude length

    /**
     * The area of the triangle using the shoelace formula, also equal to
     * 1/2*bh. Using the shoelace formula, the area can be calculated as half
     * the absolute value of the cross product of any two edges.
     */
    override fun area(): Float {
        val vecAB = v2 - v1
        val vecAC = v3 - v1
        return 0.5f * abs(vecAB.cross(vecAC))
    }

    /**
     * The center of the triangle, equal to the average position of its
     * vertices, and two-thirds along any median.
     */
    override fun center(): Vec2 = (v1 + v2 + v3) / 3f

    // Backing field for angular mass function
    private var polarMoment: Float? = null

    /**
     * The centroidal moment of inertia of the triangle, equal to 1/18*m(b^2 +
     * h^2 + a(a - b)).
     *
     * Second moment of area: I_x = 1/36*bh^3, I_y = 1/36(b^3*h - b^2*ha +
     * bha^2) Polar moment of area: I_z = 1/36*(b^3*h - b^2*ha + bha^2 + bh^3)
     * = 1/18*(1/2*bh)(b^2 + h^2 + a(a - b)) = 1/18*A(b^2 + h^2 + a(a - b)),
     * where a is the offset of the vertex along the base from either end.
     */
    override fun angularMass(mass: Float): Float {
        if (polarMoment == null) {
            val center = center()
            polarMoment = 0f

            for (i in 0..1) {
                val ptA = vertices[i]
                val ptB = vertices[i + 1]
                val vecAC = center - ptA
                val vecBC = center - ptB
                val cross = vecAC.cross(vecBC)
                polarMoment = polarMoment!! + cross
            }
//            polarMoment = polarMoment!! * area()

            val offset = base - MathUtils.invHypot(edges[2].length, height)
            polarMoment = (MathUtils.hypotSq(base, height) + offset * (offset - base)) / 18f
        }
        return mass * polarMoment!!
    }

    // Transformations
    override fun translate(direction: Vec2): Triangle {
        return Triangle(*vertices.translate(direction))
    }

    override fun rotate(angle: Float, origin: Vec2?): Triangle {
        return Triangle(*vertices.rotate(angle, origin ?: this.center()))
    }

    override fun scale(factor: Vec2, origin: Vec2?): Triangle {
        return Triangle(*vertices.scale(factor, origin ?: this.center()))
    }

    // Overrides

    /**
     * Computes efficiently whether the point is inside the triangle.
     *
     * We can parameterize a point in relation to the origin and two axes as P
     * = 0 + w1 * V1 + w2 * V2, where w1 and w2 are the distances along each
     * axis.
     *
     * If we use the vertex A as the origin, and the edges AB and AC as axes,
     * then we can establish P = A + w1 * AB + w2 * AC, or AP = w1 * AB + w2 *
     * AC, where AP is the vector from A to P.
     *
     * We can solve for the weights by w1 = (AC x AP)/(AC x AB) and w2 = -(AB
     * x AP)/(AC x AB). If both w1 ≥ 0 and w2 ≥ 0 and w1 + w2 ≤ 1, then P is
     * inside the triangle.
     *
     * Sources:
     * - [Sebastian Lague](https://www.youtube.com/watch?v=HYAgJN3x4GA)
     * - org.dyn4j.geometry.Triangle#contains
     *
     * @param point the point to check
     * @return the scaled shape
     */
    override fun contains(point: Vec2): Boolean {
        if (point in vertices) return true

        val vecAB = v2 - v1
        val vecAC = v3 - v1
        val vecAP = point - v1

        val denom = vecAC.cross(vecAB)
        val invDenom = 1f / denom
        val w1 = vecAC.cross(vecAP) * invDenom
        if (w1 < 0f) return false // Fail early if w1 < 0

        val w2 = -vecAB.cross(vecAP) * invDenom
        if (w2 < 0f) return false // Fail early if w2 < 0

        return w1 + w2 <= 1f
    }

    override fun equals(other: Any?): Boolean {
        return (other is Triangle) && this.vertices.contentEquals(other.vertices)
    }

    override fun hashCode(): Int = Objects.hash(v1, v2, v3)

    /** A description of the triangle in the form Triangle (v1, v2, v3). */
    override fun toString(): String = "Triangle ($v1, $v2, $v3)"

}

// Helper Methods

private fun triangleVertices(v1: Vec2, v2: Vec2, v3: Vec2): Array<Vec2> {
    return orderedVertices(arrayOf(v1, v2, v3))
}