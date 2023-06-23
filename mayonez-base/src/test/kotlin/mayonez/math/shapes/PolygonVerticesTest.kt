package mayonez.math.shapes

import mayonez.math.*
import mayonez.test.TestUtils.*
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
    fun oneVertexOrdered() {
        val verts = arrayOf(Vec2(2f, 2f))
        val sorted = PolygonVertices.orderedVertices(verts)

        assertArrayEquals(verts, sorted)
    }

    @Test
    fun twoVerticesOrdered() {
        val verts = arrayOf(Vec2(2f, 2f), Vec2(-1f, -1f))
        val sorted = PolygonVertices.orderedVertices(verts)

        assertArrayEquals(verts, sorted)
    }

    @Test
    fun threeVerticesOrdered() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(-1f, 0f), Vec2(2f, 1f)
        )
        val expectedSorted = arrayOf(
            Vec2(-1f, 0f),
            Vec2(2f, 1f),
            Vec2(0f, 3f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun threeVerticesHorizontalFlatOrdered() {
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
    fun threeVerticesVerticalFlatOrdered() {
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
    fun fourVerticesOrdered() {
        val verts = arrayOf(
            Vec2(2f, 3f), Vec2(2f, 0f),
            Vec2(0f, 0f), Vec2(0f, 3f)
        )
        val expectedSorted = arrayOf(
            Vec2(0f, 0f), Vec2(2f, 0f), Vec2(2f, 3f), Vec2(0f, 3f)
        )

        val sorted = PolygonVertices.orderedVertices(verts)
        assertArrayEquals(expectedSorted, sorted)
    }

    @Test
    fun fiveVerticesOrdered() {
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

    // Convex Hull Tests

    @Test
    fun threeVerticesHullCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(-1f, 0f),
            Vec2(0f, 1f), Vec2(2f, 1f)
        )
        val expectedHull = arrayOf(
            Vec2(-1f, 0f), Vec2(2f, 1f), Vec2(0f, 3f)
        )

        val hull = PolygonVertices.orderedConvexHull(verts)
        assertVerticesEqual(expectedHull, hull)
    }

    @Test
    fun fiveVerticesHullCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(0f, 0f), Vec2(-2f, 2f),
            Vec2(-2f, 0f), Vec2(-1f, 1f), Vec2(1f, -1f),
            Vec2(2f, 1f), Vec2(1f, 2f)
        )
        val expectedHull = arrayOf(
            Vec2(-2f, 0f), Vec2(1f, -1f), Vec2(2f, 1f),
            Vec2(0f, 3f), Vec2(-2f, 2f)
        )

        val hull = PolygonVertices.orderedConvexHull(verts)
        assertVerticesEqual(expectedHull, hull)
    }

}