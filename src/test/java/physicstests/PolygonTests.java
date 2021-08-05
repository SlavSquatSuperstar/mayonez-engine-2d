package physicstests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.MathUtils;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics2d.colliders.PolygonCollider2D;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link slavsquatsuperstar.mayonez.physics2d.colliders.PolygonCollider2D} class.
 *
 * @author SlavSquatSuperstar
 */
public class PolygonTests {

    static Vec2[] vertices = new Vec2[]{new Vec2(1, 1), new Vec2(-1, 1), new Vec2(-1, -1), new Vec2(1, -1)};

    // Create 2x2 a box centered at (0, 0) and rotate it by 45 degrees
    @Test
    public void pointIsInPolygon() {
        PolygonCollider2D shape = new PolygonCollider2D(vertices){}.setTransform(new Transform().rotate(45));
        assertTrue(shape.contains(new Vec2(0, 1)));
        assertTrue(shape.contains(new Vec2(1, 0)));
    }

    // Create 2x2 a box centered at (0, 0)
    @Test
    public void polygonVerticesReturnsWorld() {
        PolygonCollider2D shape = new PolygonCollider2D(vertices){}.setTransform(new Transform());
        assertEquals(4, shape.countVertices());
        assertTrue(Objects.deepEquals(vertices, shape.getVertices()));
    }

    // Create a 2x2 box centered at (0, 0) and translate it by (1, 1)
    @Test
    public void translatedPolygonVerticesReturnsWorld() {
        PolygonCollider2D shape = new PolygonCollider2D(vertices){}.setTransform(new Transform(new Vec2(1, 1)));
        Vec2[] worldVertices = new Vec2[]{new Vec2(2, 2), new Vec2(0, 2), new Vec2(0, 0), new Vec2(2, 0)};
        assertTrue(Objects.deepEquals(worldVertices, shape.getVertices()));
    }

    // Create a 2x2 box centered at (0, 0) and scale it by 2x
    @Test
    public void scaledPolygonVerticesReturnsWorld() {
        PolygonCollider2D shape = new PolygonCollider2D(vertices){}.setTransform(new Transform().resize(new Vec2(2, 2)));
        Vec2[] worldVertices = new Vec2[]{new Vec2(2, 2), new Vec2(-2, 2), new Vec2(-2, -2), new Vec2(2, -2)};
        assertTrue(Objects.deepEquals(worldVertices, shape.getVertices()));
    }

    // Create a 2x2 box centered at (0, 0) and rotate it by 45 degrees
    @Test
    public void rotatedPolygonVerticesReturnsWorld() {
        PolygonCollider2D shape = new PolygonCollider2D(vertices){}.setTransform(new Transform().rotate(45));
        Vec2[] worldVertices = new Vec2[vertices.length];
        for (int i = 0; i < worldVertices.length; i++)
            worldVertices[i] = vertices[i].rotate(45);
        assertTrue(Objects.deepEquals(worldVertices, shape.getVertices()));
    }

    @Test
    public void polygonNormalsPointOutward() {
        PolygonCollider2D shape = new PolygonCollider2D(vertices){}.setTransform(new Transform());
        Vec2[] normals = new Vec2[]{new Vec2(0, 1), new Vec2(-1, 0), new Vec2(0, -1), new Vec2(1, 0)};
        assertTrue(Objects.deepEquals(normals, shape.getNormals()));
    }

    // SAT
    @Test
    public void getIntervalSuccess() {
        PolygonCollider2D p1 = new PolygonCollider2D(vertices){}.setTransform(new Transform());
        PolygonCollider2D p2 = new PolygonCollider2D(vertices){}.setTransform(new Transform(new Vec2(0.5f, 0.5f)));
        assertEquals(1.5f, p1.getAxisOverlap(p2, new Vec2(1, 0)), MathUtils.EPSILON);
    }

}
