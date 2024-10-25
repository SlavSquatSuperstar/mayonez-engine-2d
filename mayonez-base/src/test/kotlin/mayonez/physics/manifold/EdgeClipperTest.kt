package mayonez.physics.manifold

import mayonez.math.*
import mayonez.math.shapes.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for the mayonez.physics.manifold.EdgeClipper file.
 *
 * @author SlavSquatSuperstar
 */
internal class EdgeClipperTest {

    // Clip Edge

    @Test
    fun clipEdgeBothEnds() {
        val edge = Edge(Vec2(1f, 1f), Vec2(5f, 1f))
        val segment = Edge(Vec2(2f, 0f), Vec2(4f, 0f))
        assertEquals(edge.clipToSegment(segment), Edge(Vec2(2f, 1f), Vec2(4f, 1f)))
    }

    @Test
    fun clipEdgeOneEnd() {
        val edge = Edge(Vec2(1f, 1f), Vec2(5f, 1f))
        val seg1 = Edge(Vec2(0f, 0f), Vec2(4f, 0f))
        assertEquals(edge.clipToSegment(seg1), Edge(Vec2(1f, 1f), Vec2(4f, 1f)))
        val seg2 = Edge(Vec2(2f, 0f), Vec2(6f, 0f))
        assertEquals(edge.clipToSegment(seg2), Edge(Vec2(2f, 1f), Vec2(5f, 1f)))
    }

    @Test
    fun clipEdgeNoEnds() {
        val edge = Edge(Vec2(1f, 1f), Vec2(5f, 1f))
        val segment = Edge(Vec2(0f, 0f), Vec2(6f, 0f))
        assertEquals(edge.clipToSegment(segment), edge)
    }

    @Test
    fun clipEdgeAngledBothEnds() {
        val edge = Edge(Vec2(0f, 1f), Vec2(5f, 1f))
        val segment = Edge(Vec2(4.5f, 0f), Vec2(2.1f, -1.2f))
        assertEquals(edge.clipToSegment(segment), Edge(Vec2(1f, 1f), Vec2(4f, 1f)))
    }

    @Test
    fun clipEdgeAngledOneEnd() {
        val edge = Edge(Vec2(0f, 1f), Vec2(5f, 1f))
        val segment = Edge(Vec2(4.5f, 0f), Vec2(0.5f, -2f))
        assertEquals(edge.clipToSegment(segment), Edge(Vec2(0f, 1f), Vec2(4f, 1f)))
    }

    @Test
    fun clipEdgeAngledNoEnds() {
        val edge = Edge(Vec2(0f, 1f), Vec2(5f, 1f))
        val segment = Edge(Vec2(6.5f, 1f), Vec2(0.5f, -2f))
        assertEquals(edge.clipToSegment(segment), Edge(Vec2(0f, 1f), Vec2(5f, 1f)))
    }
    
}