package mayonez.physics.colliders

import mayonez.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.physics.colliders.PolygonCollider] class.
 *
 * @author SlavSquatSuperstar
 */
internal class PolygonColliderTest {

    private val vertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(2f), 0f)
    private lateinit var box: PolygonCollider

    @BeforeEach
    fun createBox() {
        box = PolygonCollider(*vertices) // Create 2x2 box centered at (0, 0)
    }

    // Properties
    @Test
    fun polygonVerticesReturnsWorld() {
        Assertions.assertEquals(4, box.numVertices)
        CollisionTestUtils.assertVerticesEqual(vertices, box.getVertices())
    }

    // Translate box by (1, 1)
    @Test
    fun translatedPolygonVerticesReturnsWorld() {
        box.transform = Transform(Vec2(1f, 1f))
        val worldVertices = Rectangle.rectangleVertices(Vec2(1f), Vec2(2f), 0f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, box.getVertices())
    }

    // Scale box by 2x
    @Test
    fun scaledPolygonVerticesReturnsWorld() {
        box.transform = Transform.scaleInstance(Vec2(2f))
        val worldVertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(4f), 0f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, box.getVertices())
    }

    // Rotate box by 45 degrees
    @Test
    fun rotatedPolygonVerticesReturnsWorld() {
        box.transform = Transform.rotateInstance(45f)
        val worldVertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(2f), 45f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, box.getVertices())
    }

    // Apply all transforms
    @Test
    fun allTransformsPolygonVerticesReturnsWorld() {
        box.transform = Transform(Vec2(1f), 45f, Vec2(2f))
        val worldVertices = Rectangle.rectangleVertices(Vec2(1f), Vec2(4f), 45f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, box.getVertices())
    }

}