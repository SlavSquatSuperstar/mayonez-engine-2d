package slavsquatsuperstar.mayonez.physics.shapes;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.collision.Collisions;

import static slavsquatsuperstar.test.TestUtils.assertNoShapeCollision;
import static slavsquatsuperstar.test.TestUtils.assertShapeCollision;

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
        assertShapeCollision(c1, c2);
    }

    @Test
    public void circleVsCircleHitTangent() {
        Circle c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        Circle c2 = new Circle(new Vec2(4, 0), 2); // 4x4 circle at (4, 0)
        assertShapeCollision(c1, c2);
    }

    @Test
    public void circleVsCircleMiss() {
        Circle c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        Circle c2 = new Circle(new Vec2(5, 5), 2); // 4x4 circle at (5, 5)
        assertNoShapeCollision(c1, c2);
    }

    // AABB vs AABB

    @Test
    public void aabbVsAABBHit() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        Rectangle r2 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapeCollision(r1, r2);
    }

    @Test
    public void aabbVsAABBMiss() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        Rectangle r2 = new Rectangle(new Vec2(1, 4), new Vec2(3, 3)); // 3x3 rect at (1, 4)
        assertNoShapeCollision(r1, r2); // one axis

        Rectangle r3 = new Rectangle(new Vec2(4, 4), new Vec2(3, 3)); // 3x3 rect at (4, 4)
        assertNoShapeCollision(r1, r2); // both axes
    }

    // Rectangle vs Rectangle (Non-Rotated)

    @Test
    public void rectVsRectHitInterior() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        Rectangle r2 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapeCollision(r1, r2);
    }

    @Test
    public void rectVsRectHitBoundary() {
        Rectangle r1 = new Rectangle(new Vec2(1, 1), new Vec2(2, 2)); // 2x2 rect at (1, 1)
        Rectangle r2 = new Rectangle(new Vec2(3, 2), new Vec2(2, 2)); // 2x2 rect at (3, 2)
        assertShapeCollision(r1, r2); // edge

        Rectangle r3 = new Rectangle(new Vec2(3, 3), new Vec2(2, 2)); // 2x2 rect at (3, 3)
        assertShapeCollision(r1, r3); // vertex
    }

    @Test
    public void rectVsRectHitInside() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        Rectangle r2 = new Rectangle(new Vec2(0, 0), new Vec2(2, 42)); // 2x2 rect at (0, 0)
        assertShapeCollision(r1, r2);
    }

    @Test
    public void rectVsRectHitSelf() {
        Rectangle r1 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        Rectangle r2 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapeCollision(r1, r2);
    }

    @Test
    public void rectVsRectMiss() {
        Rectangle r1 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        Rectangle r2 = new Rectangle(new Vec2(5, 5), new Vec2(3, 3)); // 3x3 rect at (5, 5)
        assertNoShapeCollision(r1, r2);
    }

    // Rectangle vs Rectangle (Rotated)

    @Test
    public void boxVsBoxHit() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3), 45); // 3x3 box at (0, 0) rotated 45º
        Rectangle r2 = new Rectangle(new Vec2(2, 2), new Vec2(3, 3), 45); // 3x3 box at (2, 2) rotated 45º
        assertShapeCollision(r1, r2);

        Rectangle r3 = new Rectangle(new Vec2(1, 0), new Vec2(3, 3)); // 3x3 box at (1, 0) rotated 45º
        Rectangle r4 = new Rectangle(new Vec2(0, 2), new Vec2(3, 3)); // 3x3 box at (0, 2) rotated 45º
        assertShapeCollision(r3, r4);
    }

    @Test
    public void boxVsBoxMiss() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3), 45); // 3x3 box at (0, 0) rotated 45º
        Rectangle r2 = new Rectangle(new Vec2(4, 4), new Vec2(3, 3), 45); // 3x3 box at (4, 4) rotated 45º
        assertNoShapeCollision(r1, r2);
    }

    // Triangle vs Triangle

    @Test
    public void triangleVsTriangleHitInterior() {
        Triangle t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Triangle t2 = new Triangle(new Vec2(1, 1), new Vec2(2, 4), new Vec2(3, 1)); // 2x3 triangle from (1, 1)
        assertShapeCollision(t1, t2);
    }

    @Test
    public void triangleVsTriangleHitEdge() {
        Triangle t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Triangle t2 = new Triangle(new Vec2(1, 0), new Vec2(2, -3), new Vec2(3, 0)); // 2x3 triangle down from (1, 0)
        assertShapeCollision(t1, t2); // bases

        Triangle t3 = new Triangle(new Vec2(1, 3), new Vec2(3, 1), new Vec2(5, 3)); // 4x2 triangle down from (3, 1)
        assertShapeCollision(t1, t3); // legs
    }

    @Test
    public void triangleVsTriangleHitVertex() {
        Triangle t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Triangle t2 = new Triangle(new Vec2(0, 3), new Vec2(1, 6), new Vec2(2, 3)); // 2x3 triangle from (0, 3)
        assertShapeCollision(t1, t2); // vertex vs edge

        Triangle t3 = new Triangle(new Vec2(2, 0), new Vec2(3, 3), new Vec2(4, 0)); // 2x3 triangle from (2, 0)
        assertShapeCollision(t1, t3); // vertex vs vertex
    }

    // Shape vs Shape (Mixed)

    @Test
    public void boxVsCircleHitInterior() {
        BoundingBox box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Circle c = new Circle(new Vec2(3, 1), 2); // 4x4 circle at (3, 1)
        assertShapeCollision(box, c);
    }

    @Test
    public void boxVsCircleHitTangent() {
        BoundingBox box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Circle c = new Circle(new Vec2(4, 1), 2); // 4x4 circle at (4, 1)
        assertShapeCollision(box, c);
    }

    @Test
    public void boxVsCircleMiss() {
        BoundingBox box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Circle c = new Circle(new Vec2(5, 1), 2); // 4x4 circle at (5, 1)
        assertNoShapeCollision(box, c);
    }

    @Test
    public void triangleVsCircleHit() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Circle c = new Circle(new Vec2(1, 4), 1.5f); // 3x3 circle at (1, 4)
        assertShapeCollision(tri, c);
    }

    @Test
    public void triangleVsCircleMiss() {
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        Circle c = new Circle(new Vec2(5, 4), 1.5f); // 3x3 circle at (5, 4)
        assertNoShapeCollision(tri, c);
    }

    @Test
    public void boxVsTriangleHit() {
        Polygon box = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Triangle tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        assertShapeCollision(box, tri);
    }

    @Test
    public void boxVsTriangleMiss() {
        BoundingBox box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        Triangle tri = new Triangle(new Vec2(3, 1), new Vec2(4, 4), new Vec2(5, 1)); // 2x3 triangle from (3, 1)
        assertNoShapeCollision(box, tri);
    }

}
