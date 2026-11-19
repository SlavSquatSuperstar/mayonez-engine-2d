package mayonez.physics.colliders

import mayonez.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.physics.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [mayonez.physics.colliders.Collider] class.
 *
 * @author SlavSquatSuperstar
 */
internal class ColliderTest {

    private val polygon = Rectangle(Vec2(0f), Vec2(2f), 0f)
    private val vertices = polygon.vertices
    private lateinit var collider: Collider

    @BeforeEach
    fun createBox() {
        collider = Collider(polygon) // Create 2x2 box centered at (0, 0)
    }

    // Properties
    @Test
    fun polygonVerticesReturnsWorld() {
        Assertions.assertEquals(4, collider.numVertices())
        CollisionTestUtils.assertVerticesEqual(vertices, collider.getVertices())
    }

    // Translate box by (1, 1)
    @Test
    fun translatedPolygonVerticesReturnsWorld() {
        collider.setTransform(Transform(Vec2(1f)))
        val worldVertices = Rectangle.rectangleVertices(Vec2(1f), Vec2(2f), 0f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, collider.getVertices())
    }

    // Scale box by 2x
    @Test
    fun scaledPolygonVerticesReturnsWorld() {
        collider.setTransform(Transform.scaleInstance(Vec2(2f)))
        val worldVertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(4f), 0f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, collider.getVertices())
    }

    // Rotate box by 45 degrees
    @Test
    fun rotatedPolygonVerticesReturnsWorld() {
        collider.setTransform(Transform.rotateInstance(45f))
        val worldVertices = Rectangle.rectangleVertices(Vec2(0f), Vec2(2f), 45f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, collider.getVertices())
    }

    // Apply all transforms
    @Test
    fun allTransformsPolygonVerticesReturnsWorld() {
        collider.setTransform(Transform(Vec2(1f), 45f, Vec2(2f)))
        val worldVertices = Rectangle.rectangleVertices(Vec2(1f), Vec2(4f), 45f)
        CollisionTestUtils.assertVerticesEqual(worldVertices, collider.getVertices())
    }

}

// Shape Vertex Methods

private fun Collider.numVertices(): Int {
    return when (val shape = this.getShape()) {
        is Polygon -> shape.numVertices
        else -> 0
    }
}

private fun Collider.getVertices(): Array<Vec2> {
    // Vertices transformed to world space
    return when (val shape = this.getShape()) {
        is Polygon -> shape.vertices
        else -> emptyArray()
    }
}