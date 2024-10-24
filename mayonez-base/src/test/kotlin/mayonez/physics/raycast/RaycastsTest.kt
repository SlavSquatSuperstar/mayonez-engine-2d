package mayonez.physics.raycast

import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.*

/**
 * Unit tests for the [mayonez.physics.raycast.Raycasts] class.
 *
 * @author SlavSquatSuperstar
 */
internal class RaycastsTest {

    companion object {
        private val circle = Circle(Vec2(5f, 0f), 2f) // 4x4 circle at (5, 0)
        private val rect = Rectangle(Vec2(0f, 0f), Vec2(4f, 4f)) // 4x4 box at (0, 0)
        private val root2 = sqrt(2f)
    }

    @Test
    fun rayVsCircleHitOutside() {
        val rc = Raycasts.raycast(circle, Ray(Vec2(0f, 0f), Vec2(1f, 0f)), 3f)
        assertRaycastHit(rc, Vec2(3f, 0f), Vec2(-1f, 0f), 3f)
    }

    @Test
    fun rayVsCircleHitInside() {
        val rc = Raycasts.raycast(circle, Ray(Vec2(5f, -1f), Vec2(0f, 1f)), 0f)
        assertRaycastHit(rc, Vec2(5f, 2f), Vec2(0f, 1f), 3f)
    }

    @Test
    fun rayVsCircleMiss() {
        assertRaycastMiss(circle, Vec2(-3f, 0f), Vec2(0f, 1f), 0f)
        assertRaycastMiss(circle, Vec2(-4f, 0f), Vec2(1f, 0f), 1f) // too far away
    }

    @Test
    fun rayVsEdgeHit() {
        val e = Edge(Vec2(0f, -2f), Vec2(0f, 2f))
        val rc = Raycasts.raycast(e, Ray(Vec2(-2f, 2f), Vec2(1f, -1f)), 0f)
        assertRaycastHit(rc, Vec2(0f, 0f), Vec2(-1f, 0f), 2f * root2)
    }

    @Test
    fun rayVsEdgeMiss() {
        val e = Edge(Vec2(0f, -2f), Vec2(0f, 2f))
        assertRaycastMiss(e, Vec2(-4f, 2f), Vec2(1f, -2f), 0f)
    }

    @Test
    fun rayVsRectangleHitFlat() {
        val rc = Raycasts.raycast(rect, Ray(Vec2(-4f, 0f), Vec2(1f, 0f)), 0f)
        assertRaycastHit(rc, Vec2(-2f, 0f), Vec2(-1f, 0f), 2f)
    }

    @Test
    fun rayVsRectangleHitDiagonal() {
        val rc = Raycasts.raycast(rect, Ray(Vec2(-2f, 4f), Vec2(1f, -1f)), 0f)
        assertRaycastHit(rc, Vec2(0f, 2f), Vec2(0f, 1f), 2 * root2)
    }

    @Test
    fun rayVsRotatedRectangleHit() {
        val rc = Raycasts.raycast(rect.rotate(45f), Ray(Vec2(-3f, 3f), Vec2(1f, -1f)), 0f)
        val dist = (3f - root2) * root2
        assertRaycastHit(rc, Vec2(-root2, root2), Vec2(-1f, 1f).unit(), dist)
    }

    @Test
    fun rayVsRectangleMiss() {
        assertRaycastMiss(rect, Vec2(2f, 4f), Vec2(1f, -1f), 0f)
    }

    @Test
    fun rayVsPolygonHit() {
        val hexagon = Polygon(
            Vec2(-1f, -2f), Vec2(1f, -2f), Vec2(2f, 0f),
            Vec2(1f, 2f), Vec2(-1f, 2f), Vec2(-2f, 0f)
        )
        val rc = Raycasts.raycast(hexagon, Ray(Vec2(-3f, 1f), Vec2(1f, 0f)), 0f)
        assertRaycastHit(rc, Vec2(-1.5f, 1f), Vec2(-2f, 1f).unit(), 1.5f)
    }

    private fun assertRaycastHit(rc: RaycastInfo?, contact: Vec2, normal: Vec2, distance: Float) {
        Assertions.assertNotNull(rc)
        Assertions.assertEquals(contact, rc!!.contact)
        Assertions.assertEquals(normal, rc.normal)
        CollisionTestUtils.assertFloatEquals(distance, rc.distance)
    }

    private fun assertRaycastMiss(s: Shape, start: Vec2, direction: Vec2, limit: Float) {
        Assertions.assertNull(Raycasts.raycast(s, Ray(start, direction), limit))
    }

}