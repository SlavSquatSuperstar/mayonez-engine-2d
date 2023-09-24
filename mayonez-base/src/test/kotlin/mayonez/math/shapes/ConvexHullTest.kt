package mayonez.math.shapes

import mayonez.math.*
import mayonez.test.TestUtils.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for the mayonez.math.shapes.ConvexHull file.
 *
 * @author SlavSquatSuperstar
 */
internal class ConvexHullTest {

    // Convex Hull Tests

    @Test
    fun fourVerticesThreeInHullCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(-1f, 0f),
            Vec2(0f, 1f), Vec2(2f, 1f)
        )
        val expectedHull = arrayOf(
            Vec2(-1f, 0f), Vec2(2f, 1f), Vec2(0f, 3f)
        )

        val hull = orderedConvexHull(verts)
        assertVerticesEqual(expectedHull, hull)
    }

    @Test
    fun eightVerticesFiveInHullCorrect() {
        val verts = arrayOf(
            Vec2(0f, 3f), Vec2(0f, 0f), Vec2(-2f, 2f),
            Vec2(-2f, 0f), Vec2(-1f, 1f), Vec2(1f, -1f),
            Vec2(2f, 1f), Vec2(1f, 2f)
        )
        val expectedHull = arrayOf(
            Vec2(-2f, 0f), Vec2(1f, -1f), Vec2(2f, 1f),
            Vec2(0f, 3f), Vec2(-2f, 2f)
        )

        val hull = orderedConvexHull(verts)
        assertVerticesEqual(expectedHull, hull)
    }

}