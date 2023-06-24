package mayonez.math.shapes

import mayonez.math.*
import mayonez.math.shapes.PolygonVertices.rotate
import mayonez.math.shapes.PolygonVertices.scale
import mayonez.math.shapes.PolygonVertices.translate
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
    @JvmField
    val base: Float = edges[0].length

    /** The height of the triangle, h. */
    @JvmField
    val height: Float = abs(edges[1].toVector().component(edges[0].unitNormal())) // get altitude length

    /**
     * The area of the triangle using the shoelace formula, equal
     * to 1/2*bh. The shoelace formula specifies the area as
     * 1/2 * [(v1 × v2) + (v2 × v3) + (v3 × v1)]. Expansion and simplification
     * gives the determinant as (x1 - x3)(y2 - y3) - (x2 - x3)(y1 - y3).
     */
    override fun area(): Float {
        try {
            // more efficient because fewer multiplications
            return 0.5f * abs((v1.x - v3.x) * (v2.y - v3.y) - (v2.x - v3.x) * (v1.y - v3.y))
//        return 0.5f * abs(
//            vertices[0].cross(vertices[1]) + vertices[1].cross(vertices[2]) +
//                    vertices[2].cross(vertices[0])
//        )
        } catch (e: ArrayIndexOutOfBoundsException) {
//            println(vertices.contentToString())
            throw e
        }
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
            val offset = base - FloatMath.invHypot(edges[2].length, height)
            polarMoment = (FloatMath.hypotSq(base, height) + offset * (offset - base)) / 18f
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

    override fun contains(point: Vec2): Boolean {
        if (point in vertices) return true

        // divide triangle into three triangles
        // sum area and compare to original area
        val areas = FloatArray(3) {
            Triangle(vertices[it], vertices[(it + 1) % 3], point).area()
        }
        return FloatMath.sum(*areas) <= this.area()
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
    return PolygonVertices.orderedVertices(arrayOf(v1, v2, v3))
}