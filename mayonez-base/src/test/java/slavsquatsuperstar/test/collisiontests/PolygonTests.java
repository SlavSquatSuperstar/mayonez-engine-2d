package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics.colliders.PolygonCollider;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link PolygonCollider} class.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonTests {

    static Vec2[] vertices = new Vec2[]{new Vec2(1, 1), new Vec2(-1, 1), new Vec2(-1, -1), new Vec2(1, -1)};

    // Properties

    // Create 2x2 a box centered at (0, 0)
    @Test
    public void polygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices);
        assertEquals(4, shape.getNumVertices());
        assertTrue(Objects.deepEquals(vertices, shape.getVertices()));
    }

    // Create a 2x2 box centered at (0, 0) and translate it by (1, 1)
    @Test
    public void translatedPolygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices).setTransform(new Transform(new Vec2(1, 1)));
        Vec2[] worldVertices = new Vec2[]{new Vec2(2, 2), new Vec2(0, 2), new Vec2(0, 0), new Vec2(2, 0)};
        assertTrue(Objects.deepEquals(worldVertices, shape.getVertices()));
    }

    // Create a 2x2 box centered at (0, 0) and scale it by 2x
    @Test
    public void scaledPolygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices).setTransform(new Transform().scale(new Vec2(2, 2)));
        Vec2[] worldVertices = new Vec2[]{new Vec2(2, 2), new Vec2(-2, 2), new Vec2(-2, -2), new Vec2(2, -2)};
        assertTrue(Objects.deepEquals(worldVertices, shape.getVertices()));
    }

    // Create a 2x2 box centered at (0, 0) and rotate it by 45 degrees
    @Test
    public void rotatedPolygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices).setTransform(new Transform().rotate(45));
        Vec2[] worldVertices = new Vec2[vertices.length];
        for (int i = 0; i < worldVertices.length; i++)
            worldVertices[i] = vertices[i].rotate(45);
        assertTrue(Objects.deepEquals(worldVertices, shape.getVertices()));
    }

}
