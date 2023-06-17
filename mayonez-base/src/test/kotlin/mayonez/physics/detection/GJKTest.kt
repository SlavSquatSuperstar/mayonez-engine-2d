package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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


    // Simplex Tests
    @Test
    fun getCirclesSimplex() {
        val c1 = Circle(Vec2(0f, 0f), 5f)
        val c2 = Circle(Vec2(9.9f, 0f), 5f)
        val c3 = Circle(Vec2(10.1f, 0f), 5f)
        
        assertNotNull(gjk.getSimplex(c1, c2))
        assertNull(gjk.getSimplex(c1, c3))
        testPenetration(c1, c2, 0.100002f)
    }

    @Test
    fun getRectanglesSimplex() {
        val r1 = Rectangle(Vec2(2f, 2f), Vec2(4f, 4f))
        val r2 = Rectangle(Vec2(5f, 5f), Vec2(4f, 4f), 45f)
        val r3 = Rectangle(Vec2(5.5f, 5.5f), Vec2(4f, 4f), 45f)
        
        assertNotNull(gjk.getSimplex(r1, r2))
        assertNull(gjk.getSimplex(r1, r3))
        testPenetration(r1, r2, 0.585788f)
    }

    @Test
    fun getEllipsesSimplex() {
        val e1 = Ellipse(Vec2(1.5f, 1f), Vec2(3f, 2f))
        val e2 = Ellipse(Vec2(3.5f, 2.5f), Vec2(2f, 3f))
        val e3 = Ellipse(Vec2(4f, 3f), Vec2(2f, 3f))

        assertNotNull(gjk.getSimplex(e1, e2))
        assertNull(gjk.getSimplex(e1, e3))
        testPenetration(e1, e2, 0.045203f)
    }

    @Test
    // Source: https://dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/#gjk-iteration
    fun getPolygonsSimplex() {
        val p1 = Triangle(Vec2(4f, 5f), Vec2(9f, 9f), Vec2(4f, 11f))
        val p2 = Polygon(Vec2(5f, 7f), Vec2(7f, 3f), Vec2(10f, 2f), Vec2(12f, 7f))
        val simplex = gjk.getSimplex(p1, p2)

        assertNotNull(simplex)
        testPenetration(p1, p2, 0.937044f)

        val simplexPoly = simplex!!.toPolygon()
        TestUtils.assertVerticesEqual(simplexPoly.vertices, arrayOf(Vec2(4f, 2f), Vec2(-8f, -2f), Vec2(-1f, -2f)))
        assertTrue(simplexPoly.contains(Vec2(0f, 0f)))
    }

    private fun testPenetration(shape1: Shape, shape2: Shape, depth: Float) {
        val pen = gjk.getPenetration(shape1, shape2)
        assertNotNull(pen)
        TestUtils.assertFloatEquals(depth, pen!!.depth)
    }
    
}