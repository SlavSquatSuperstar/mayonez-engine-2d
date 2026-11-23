package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.CollisionTestUtils.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Unit Tests for the [mayonez.physics.detection.GJKDetector] class.
 *
 * @author SlavSquatSuperstar
 */
internal class GJKTest {

    private lateinit var gjk: GJKDetector

    @BeforeEach
    fun createGJK() {
        gjk = GJKDetector()
    }

    // Circle-Circle Tests

    @Test
    fun circlesIntersectingOrthogonalHasSimplex() {
        val c1 = Circle(Vec2(0f, 0f), 5f)
        val c2 = Circle(Vec2(8.9f, 0f), 4f)

        testSimplex(c1, c2)
        testPenetration(c1, c2, 0.1f)
    }

    @Test
    fun circlesIntersectingObliqueHasSimplex() {
        val c1 = Circle(Vec2(0f, 0f), 5f)
        val c2 = Circle(Vec2(4.9f, 4.9f) * MathUtils.SQRT_2, 5f)

        testSimplex(c1, c2)
        testPenetration(c1, c2, 10f - 2f * 4.9f)
    }

    @Test
    fun circlesNotIntersectingHasNoSimplex() {
        val c1 = Circle(Vec2(0f, 0f), 5f)
        val c2 = Circle(Vec2(9.1f, 0f), 4f)
        val c3 = Circle(Vec2(5.1f, 5.1f) * MathUtils.SQRT_2, 5f)

        testNoSimplex(c1, c2)
        testNoSimplex(c1, c3)
    }

    // Rectangle-Rectangle Tests

    @Test
    fun rectanglesIntersectingOrthogonalHasSimplex() {
        val r1 = Rectangle.fromMinAndMax(Vec2(0f, 0f), Vec2(4f, 3f))
        val r2 = r1.translate(Vec2(3.9f, 1f))

        testSimplex(r1, r2)
        testPenetration(r1, r2, 0.1f)
    }

    @Test
    fun rectanglesIntersectingObliqueHasSimplex() {
        val r1 = Rectangle.fromMinAndMax(Vec2(0f, 0f), Vec2(4f, 3f))
        val r2 = Rectangle(Vec2(5f, 1f), Vec2(2f, 2f), 45f)

        testSimplex(r1, r2)
        testPenetration(r1, r2, MathUtils.SQRT_2 - 1f)
    }

    @Test
    fun rectanglesNotIntersectingHasNoSimplex() {
        val r1 = Rectangle.fromMinAndMax(Vec2(0f, 0f), Vec2(4f, 3f))
        val r2 = r1.translate(Vec2(4.1f, 1f))
        val r3 = Rectangle(Vec2(5.5f, 1f), Vec2(2f, 2f), 45f)

        testNoSimplex(r1, r2)
        testNoSimplex(r1, r3)
    }

    // Circle-Rectangle Tests

    @Test
    fun circleRectangleIntersectingOrthogonalHasSimplex() {
        val r1 = Rectangle.fromMinAndMax(Vec2(0f, 0f), Vec2(4f, 3f))
        val c1 = Circle(Vec2(5.9f, 0f), 2f)

        testSimplex(r1, c1)
        testPenetration(r1, c1, 0.1f)
    }

    @Test
    fun circleRectangleIntersectingObliqueHasSimplex() {
        val r1 = Rectangle.fromMinAndMax(Vec2(0f, 0f), Vec2(4f, 3f))
        val c1 = Circle(Vec2(3.9f, 2.9f) * MathUtils.SQRT_2, 2f)

        testSimplex(r1, c1)
    }

    @Test
    fun circleRectangleNotIntersectingHasNotSimplex() {
        val r1 = Rectangle.fromMinAndMax(Vec2(0f, 0f), Vec2(4f, 3f))
        val c1 = Circle(Vec2(6.1f, 0f), 2f)
        val c2 = Circle(Vec2(4.1f, 3.1f) * MathUtils.SQRT_2, 2f)

        testNoSimplex(r1, c1)
        testNoSimplex(r1, c2)
    }

    // Ellipse-Ellipse Tests

    @Test
    fun ellipsesIntersectingHasSimplex() {
        val e1 = Ellipse(Vec2(1.5f, 1f), Vec2(3f, 2f))
        val e2 = Ellipse(Vec2(3.5f, 2.5f), Vec2(2f, 3f))
        val e3 = Ellipse(Vec2(4f, 3f), Vec2(2f, 3f))

        testSimplex(e1, e2)
        testNoSimplex(e1, e3)
    }

    // Polygon-Polygon Tests

    @Test
    // Source: https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/#gjk-iteration
    fun polygonsIntersectingHasSimplex() {
        val p1 = Triangle(Vec2(4f, 5f), Vec2(9f, 9f), Vec2(4f, 11f))
        val p2 = Polygon(Vec2(5f, 7f), Vec2(7f, 3f), Vec2(10f, 2f), Vec2(12f, 7f))
        val simplex = gjk.getSimplex(p1, p2)

        assertNotNull(simplex)
        testPenetration(p1, p2, 0.937044f)

        val simplexPoly = simplex.toPolygon()
        assertVerticesEqual(simplexPoly.vertices, arrayOf(Vec2(4f, 2f), Vec2(-8f, -2f), Vec2(-1f, -2f)))
        assertTrue(simplexPoly.contains(Vec2(0f, 0f)))
    }

    private fun testSimplex(s1: Shape, s2: Shape) {
        val simplex = gjk.getSimplex(s1, s2)
        assertNotNull(simplex)
        assertEquals(3, simplex.size) // Simplex is triangle
        assertTrue(simplex.toPolygon().contains(Vec2())) // Simplex contains origin
    }

    private fun testNoSimplex(s1: Shape, s2: Shape) {
        assertNull(gjk.getSimplex(s1, s2))
    }

    private fun testPenetration(shape1: Shape, shape2: Shape, depth: Float) {
        val pen = gjk.getPenetration(shape1, shape2)
        assertNotNull(pen)
        assertEquals(depth, pen.depth, 1e-4f)
    }

}