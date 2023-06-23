package mayonez.math.shapes

import mayonez.math.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.math.shapes.PolygonVertices] class.
 *
 * @author SlavSquatSuperstar
 */
internal class PolygonVerticesTest {

    // Ordered Vertices Tests

    @Test
    fun oneVertexOrderedIsSame() {
        val verts = arrayOf(Vec2(2f, 2f))
        val sorted = PolygonVertices.orderedVertices(verts)

        assertArrayEquals(verts, sorted)
    }

    @Test
    fun twoVerticesOrderedIsSame() {
        val verts = arrayOf(Vec2(2f, 2f), Vec2(-1f, -1f))
        val sorted = PolygonVertices.orderedVertices(verts)

        assertArrayEquals(verts, sorted)
    }

    @Test
    fun threeVerticesAcuteTriangleOrderedCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(-1f, 0f), Vec2(2f, 1f)
        )
        val expectedSorted = arrayOf(
            Vec2(-1f, 0f), Vec2(2f, 1f), Vec2(0f, 3f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun threeVerticesObtuseTriangleOrderedCorrect() {
        val verts = arrayOf(
            Vec2(1f, 3f), Vec2(0f, 1f), Vec2(-1f, -1f)
        )
        val expectedSorted = arrayOf(
            Vec2(-1f, -1f), Vec2(0f, 1f), Vec2(1f, 3f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun threeVerticesCollinearOrderedCorrect() {
        val verts = arrayOf(
            Vec2(2f, 2f), Vec2(0f, 0f), Vec2(-1f, -1f)
        )
        val expectedSorted = arrayOf(
            Vec2(-1f, -1f), Vec2(0f, 0f), Vec2(2f, 2f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun fourVerticesIrregularOrderedCorrect() {
        val verts = arrayOf(
            Vec2(2f, 3f), Vec2(3f, 1f),
            Vec2(0f, 0f), Vec2(0f, 4f)
        )
        val expectedSorted = arrayOf(
            Vec2(0f, 0f), Vec2(3f, 1f),
            Vec2(2f, 3f), Vec2(0f, 4f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun fourVerticesTwoSameAngleOrderedCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(-1f, 0f),
            Vec2(2f, 1f), Vec2(4f, 1f)
        )
        val expectedSorted = arrayOf(
            Vec2(-1f, 0f), Vec2(2f, 1f),
            Vec2(4f, 1f), Vec2(0f, 3f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun fiveVerticesOrderedCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(-2f, 2f), Vec2(-2f, 0f),
            Vec2(1f, -1f), Vec2(2f, 1f)
        )
        val expectedSorted = arrayOf(
            Vec2(-2f, 0f), Vec2(1f, -1f), Vec2(2f, 1f),
            Vec2(0f, 3f), Vec2(-2f, 2f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

}