package mayonez.physics.colliders;

import org.junit.jupiter.api.Test;
import mayonez.math.Vec2;
import mayonez.Transform;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static mayonez.test.TestUtils.assertVerticesEqual;

/**
 * Unit tests for the {@link mayonez.physics.colliders.PolygonCollider} class.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonColliderTests {

    private static Vec2[] vertices = new Vec2[]{new Vec2(1, 1), new Vec2(-1, 1), new Vec2(-1, -1), new Vec2(1, -1)};

    // Properties

    // Create 2x2 a box centered at (0, 0)
    @Test
    public void polygonVerticesReturnsWorld() {
        var shape = new PolygonCollider(vertices);
        assertEquals(4, shape.getNumVertices());
        assertVerticesEqual(vertices, shape.getVertices());
    }

    // Create a 2x2 box centered at (0, 0) and translate it by (1, 1)
    @Test
    public void translatedPolygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices).setTransform(new Transform(new Vec2(1, 1)));
        System.out.println(Arrays.toString(shape.getVertices()));
        Vec2[] worldVertices = new Vec2[]{new Vec2(2, 2), new Vec2(0, 2), new Vec2(0, 0), new Vec2(2, 0)};
        assertVerticesEqual(worldVertices, shape.getVertices());
    }

    // Create a 2x2 box centered at (0, 0) and scale it by 2x
    @Test
    public void scaledPolygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices).setTransform(Transform.scaleInstance(new Vec2(2)));
        Vec2[] worldVertices = new Vec2[]{new Vec2(2, 2), new Vec2(-2, 2), new Vec2(-2, -2), new Vec2(2, -2)};
        assertVerticesEqual(worldVertices, shape.getVertices());
    }

    // Create a 2x2 box centered at (0, 0) and rotate it by 45 degrees
    @Test
    public void rotatedPolygonVerticesReturnsWorld() {
        PolygonCollider shape = new PolygonCollider(vertices).setTransform(Transform.rotateInstance(45));
        Vec2[] worldVertices = new Vec2[vertices.length];
        for (var i = 0; i < worldVertices.length; i++)
            worldVertices[i] = vertices[i].rotate(45);
        assertVerticesEqual(worldVertices, shape.getVertices());
    }

}
