package slavsquatsuperstar.test.shapetests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;
import slavsquatsuperstar.mayonez.physics.shapes.BoundingRectangle;
import slavsquatsuperstar.mayonez.physics.shapes.Circle;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;

import static slavsquatsuperstar.test.TestUtils.assertShapesCollide;
import static slavsquatsuperstar.test.TestUtils.assertShapesNoCollide;

/**
 * Unit tests for shape vs shape detection in {@link Collisions} class.
 *
 * @author SlavSquatSuperstar
 */
public class ShapeIntersectionTests {

    // Circle vs Circle

    @Test
    public void circleVsCircleHitInterior() {
        Circle c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        Circle c2 = new Circle(new Vec2(2, 2), 2); // 4x4 circle at (2, 2)
        assertShapesCollide(c1, c2);
    }

    @Test
    public void circleVsCircleHitTangent() {
        Circle c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        Circle c2 = new Circle(new Vec2(4, 0), 2); // 4x4 circle at (4, 0)
        assertShapesCollide(c1, c2);
    }

    @Test
    public void circleVsCircleMiss() {
        Circle c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        Circle c2 = new Circle(new Vec2(5, 5), 2); // 4x4 circle at (5, 5)
        assertShapesNoCollide(c1, c2);
    }

    // AABB vs AABB

    @Test
    public void aabbVsAABBHit() {
        BoundingRectangle r1 = new BoundingRectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        BoundingRectangle r2 = new BoundingRectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapesCollide(r1, r2);
    }

    @Test
    public void aabbVsAABBMiss() {
        BoundingRectangle r1 = new BoundingRectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        BoundingRectangle r2 = new BoundingRectangle(new Vec2(1, 4), new Vec2(3, 3)); // 3x3 rect at (1, 4)
        assertShapesNoCollide(r1, r2); // one axis

        BoundingRectangle r3 = new BoundingRectangle(new Vec2(4, 4), new Vec2(3, 3)); // 3x3 rect at (4, 4)
        assertShapesNoCollide(r1, r2); // both axes
    }

    // Rectangle vs Rectangle (Non-Rotated)

    @Test
    public void rectVsRectHitInterior() {
        Polygon r1 = Polygon.rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        Polygon r2 = Polygon.rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapesCollide(r1, r2);
    }

    @Test
    public void rectVsRectHitBoundary() {
        Polygon r1 = Polygon.rectangle(new Vec2(1, 1), new Vec2(2, 2)); // 2x2 rect at (1, 1)
        Polygon r2 = Polygon.rectangle(new Vec2(3, 2), new Vec2(2, 2)); // 2x2 rect at (3, 2)
        assertShapesCollide(r1, r2); // edge

        Polygon r3 = Polygon.rectangle(new Vec2(3, 3), new Vec2(2, 2)); // 2x2 rect at (3, 3)
        assertShapesCollide(r1, r3); // vertex
    }

    @Test
    public void rectVsRectHitSelf() {
        Polygon r1 = Polygon.rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        Polygon r2 = Polygon.rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapesCollide(r1, r2);
    }

    @Test
    public void rectVsRectMiss() {
        Polygon r1 = Polygon.rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        Polygon r2 = Polygon.rectangle(new Vec2(5, 5), new Vec2(3, 3)); // 3x3 rect at (5, 5)
        assertShapesNoCollide(r1, r2);
    }

    // Rectangle vs Rectangle (Rotated)

    @Test
    public void boxVsBoxHit() {
        Polygon r1 = Polygon.rectangle(new Vec2(0, 0), new Vec2(3, 3), 45); // 3x3 box at (0, 0) rotated 45º
        Polygon r2 = Polygon.rectangle(new Vec2(2, 2), new Vec2(3, 3), 45); // 3x3 box at (2, 2) rotated 45º
        assertShapesCollide(r1, r2);

        Polygon r3 = Polygon.rectangle(new Vec2(1, 0), new Vec2(3, 3)); // 3x3 box at (1, 0) rotated 45º
        Polygon r4 = Polygon.rectangle(new Vec2(0, 2), new Vec2(3, 3)); // 3x3 box at (0, 2) rotated 45º
        assertShapesCollide(r3, r4);
    }

    @Test
    public void boxVsBoxMiss() {
        Polygon r1 = Polygon.rectangle(new Vec2(0, 0), new Vec2(3, 3), 45); // 3x3 box at (0, 0) rotated 45º
        Polygon r2 = Polygon.rectangle(new Vec2(4, 4), new Vec2(3, 3), 45); // 3x3 box at (4, 4) rotated 45º
        assertShapesNoCollide(r1, r2);
    }

    // Triangle vs Triangle

    @Test
    public void triangleVsTriangleHitInterior() {
        Triangle t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Triangle t2 = new Triangle(new Vec2(1, 1), new Vec2(2, 4), new Vec2(3, 1)); // 2x3 triangle from (1, 1)
        assertShapesCollide(t1, t2);
    }

    @Test
    public void triangleVsTriangleHitEdge() {
        Triangle t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Triangle t2 = new Triangle(new Vec2(1, 0), new Vec2(2, -3), new Vec2(3, 0)); // 2x3 triangle down from (1, 0)
        assertShapesCollide(t1, t2); // bases

        Triangle t3 = new Triangle(new Vec2(1, 3), new Vec2(3, 1), new Vec2(5, 3)); // 4x2 triangle down from (3, 1)
        assertShapesCollide(t1, t3); // legs
    }

    @Test
    public void triangleVsTriangleHitVertex() {
        Triangle t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Triangle t2 = new Triangle(new Vec2(0, 3), new Vec2(1, 6), new Vec2(2, 3)); // 2x3 triangle from (0, 3)
        assertShapesCollide(t1, t2); // vertex vs edge

        Triangle t3 = new Triangle(new Vec2(2, 0), new Vec2(3, 3), new Vec2(4, 0)); // 2x3 triangle from (2, 0)
        assertShapesCollide(t1, t3); // vertex vs vertex
    }

    // Shape vs Shape (Mixed)

    @Test
    public void boxVsCircleHitInterior() {
        Polygon box = Polygon.rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Circle c = new Circle(new Vec2(3, 1), 2); // 4x4 circle at (3, 1)
        assertShapesCollide(box, c);
    }

    @Test
    public void boxVsCircleHitTangent() {
        Polygon box = Polygon.rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Circle c = new Circle(new Vec2(4, 1), 2); // 4x4 circle at (4, 1)
        assertShapesCollide(box, c);
    }

    @Test
    public void boxVsCircleMiss() {
        Polygon box = Polygon.rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Circle c = new Circle(new Vec2(5, 1), 2); // 4x4 circle at (5, 1)
        assertShapesNoCollide(box, c);
    }

    @Test
    public void triangleVsCircleHit() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Circle c = new Circle(new Vec2(1, 4), 1.5f); // 3x3 circle at (1, 4)
        assertShapesCollide(tri, c);
    }

    @Test
    public void triangleVsCircleMiss() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Circle c = new Circle(new Vec2(5, 4), 1.5f); // 3x3 circle at (5, 4)
        assertShapesNoCollide(tri, c);
    }

    @Test
    public void boxVsTriangleHit() {
        Polygon box = Polygon.rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        assertShapesCollide(box, tri);
    }

    @Test
    public void boxVsTriangleMiss() {
        Polygon box = Polygon.rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Triangle tri = new Triangle(new Vec2(3, 1), new Vec2(4, 4), new Vec2(5, 1)); // 2x3 triangle from (3, 1)
        assertShapesNoCollide(box, tri);
    }

}
