package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.manifold.*
import mayonez.test.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

/**
 * Unit Tests for the [mayonez.physics.detection.SATDetector] class.
 *
 * @author SlavSquatSuperstar
 */
internal class SATTest {

//    companion object {
//        private val p1: Polygon = Triangle(Vec2(4f, 5f), Vec2(9f, 9f), Vec2(4f, 11f))
//        private val p2 = Polygon(Vec2(5f, 7f), Vec2(7f, 3f), Vec2(10f, 2f), Vec2(12f, 7f))
//    }

    private lateinit var sat: SATDetector

    @BeforeEach
    fun createSAT() {
        sat = SATDetector()
    }

    // Rectangle Tests
    @Test
    fun touchingRectanglesTwoContacts() {
        val r1 = Rectangle(Vec2(0f, 0f), Vec2(4f, 4f))
        val r2 = Rectangle(Vec2(4f, 1f), Vec2(4f, 4f))
        val pen = testPenetration(r1, r2, Vec2(1f, 0f))
        TestUtils.assertFloatEquals(0f, pen!!.depth)

        val man = testContacts(pen, r1, r2, 2)
        val pts1 = arrayOf(man!!.getContact(0), man.getContact(1))
        val pts2 = arrayOf(Vec2(2f, -1f), Vec2(2f, 2f))
        assertArrayEquals(pts1, pts2)
    }

    @Test
    fun overlappingRectanglesTwoContacts() {
        val r1 = Rectangle(Vec2(0f, 0f), Vec2(4f, 4f))
        val r2 = Rectangle(Vec2(3.9f, 0.9f), Vec2(4f, 4f))
        val pen = testPenetration(r1, r2, Vec2(1f, 0f))
        TestUtils.assertFloatEquals(0.1f, pen!!.depth)

        val man = testContacts(pen, r1, r2, 2)
        val pts1 = arrayOf(man!!.getContact(0), man.getContact(1))
        val pts2 = arrayOf(Vec2(1.9f, -1.1f), Vec2(1.9f, 2f))
        assertArrayEquals(pts1, pts2)
    }

    @Test
    fun cornerIntersectionOneContact() {
        val r1 = Rectangle(Vec2(0f, 0f), Vec2(4f, 4f))
        val r2 = Rectangle(Vec2(4.5f, 0f), Vec2(4f, 4f), 45f)
        val pen = testPenetration(r1, r2, Vec2(1f, 0f))
        testContacts(pen, r1, r2, 1)
    }

    @Test
    fun obliqueIntersectionTwoContacts() {
        val r1 = Rectangle(Vec2(0f, 0f), Vec2(4f, 4f))
        val r2 = Rectangle(Vec2(2.5f, 0f), Vec2(4f, 4f), 10f)
        val pen = testPenetration(r1, r2, Vec2(1f, 0f))
        testContacts(pen, r1, r2, 2)
    }

    private fun testPenetration(shape1: Shape, shape2: Shape, normal: Vec2): Penetration? {
        val pen = sat.getPenetration(shape1, shape2)
        assertNotNull(pen)
        assertEquals(pen!!.normal, normal)
        return pen
    }

    private fun testContacts(pen: Penetration?, shape1: Shape, shape2: Shape, count: Int): Manifold? {
        val man = ClippingManifoldSolver().getContacts(shape1, shape2, pen)
        assertNotNull(man)
        assertEquals(count, man!!.numContacts())
        return man
    }

}
