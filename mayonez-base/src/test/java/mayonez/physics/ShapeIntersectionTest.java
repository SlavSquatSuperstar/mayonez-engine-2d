package mayonez.physics;

import mayonez.math.*;
import mayonez.math.shapes.*;
import org.junit.jupiter.api.*;

import static mayonez.physics.CollisionTestUtils.*;

/**
 * Unit tests for the {@link mayonez.math.shapes.Shape} and
 * {@link mayonez.physics.Collisions} classes.
 *
 * @author SlavSquatSuperstar
 */
class ShapeIntersectionTest {

    // Circle vs Circle

    @Test
    void circleVsCircleHitInterior() {
        var c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var c2 = new Circle(new Vec2(2, 2), 2); // 4x4 circle at (2, 2)
        assertShapeCollision(c1, c2);
    }

    @Test
    void circleVsCircleHitTangent() {
        var c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var c2 = new Circle(new Vec2(4, 0), 2); // 4x4 circle at (4, 0)
        assertShapeCollision(c1, c2);
    }

    @Test
    void circleVsCircleMiss() {
        var c1 = new Circle(new Vec2(0, 0), 2); // 4x4 circle at (0, 0)
        var c2 = new Circle(new Vec2(5, 5), 2); // 4x4 circle at (5, 5)
        assertNoShapeCollision(c1, c2);
    }

    // AABB vs AABB

    @Test
    void aabbVsAABBHit() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        var r2 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapeCollision(r1, r2);
    }

    @Test
    void aabbVsAABBMiss() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        var r2 = new Rectangle(new Vec2(1, 4), new Vec2(3, 3)); // 3x3 rect at (1, 4)
        assertNoShapeCollision(r1, r2); // one axis

        var r3 = new Rectangle(new Vec2(4, 4), new Vec2(3, 3)); // 3x3 rect at (4, 4)
        assertNoShapeCollision(r1, r2); // both axes
    }

    // Rectangle vs Rectangle (Non-Rotated)

    @Test
    void rectVsRectHitInterior() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3)); // 3x3 rect at (0, 0)
        var r2 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapeCollision(r1, r2);
    }

    @Test
    void rectVsRectHitBoundary() {
        var r1 = new Rectangle(new Vec2(1, 1), new Vec2(2, 2)); // 2x2 rect at (1, 1)
        var r2 = new Rectangle(new Vec2(3, 2), new Vec2(2, 2)); // 2x2 rect at (3, 2)
        assertShapeCollision(r1, r2); // edge

        var r3 = new Rectangle(new Vec2(3, 3), new Vec2(2, 2)); // 2x2 rect at (3, 3)
        assertShapeCollision(r1, r3); // vertex
    }

    @Test
    void rectVsRectHitInside() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rect at (0, 0)
        var r2 = new Rectangle(new Vec2(0, 0), new Vec2(2, 42)); // 2x2 rect at (0, 0)
        assertShapeCollision(r1, r2);
    }

    @Test
    void rectVsRectHitSelf() {
        var r1 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        var r2 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        assertShapeCollision(r1, r2);
    }

    @Test
    void rectVsRectMiss() {
        var r1 = new Rectangle(new Vec2(1, 1), new Vec2(3, 3)); // 3x3 rect at (1, 1)
        var r2 = new Rectangle(new Vec2(5, 5), new Vec2(3, 3)); // 3x3 rect at (5, 5)
        assertNoShapeCollision(r1, r2);
    }

    // Rectangle vs Rectangle (Rotated)

    @Test
    void boxVsBoxHit() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3), 45); // 3x3 box at (0, 0) rotated 45º
        var r2 = new Rectangle(new Vec2(2, 2), new Vec2(3, 3), 45); // 3x3 box at (2, 2) rotated 45º
        assertShapeCollision(r1, r2);

        var r3 = new Rectangle(new Vec2(1, 0), new Vec2(3, 3)); // 3x3 box at (1, 0) rotated 45º
        var r4 = new Rectangle(new Vec2(0, 2), new Vec2(3, 3)); // 3x3 box at (0, 2) rotated 45º
        assertShapeCollision(r3, r4);
    }

    @Test
    void boxVsBoxMiss() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(3, 3), 45); // 3x3 box at (0, 0) rotated 45º
        var r2 = new Rectangle(new Vec2(4, 4), new Vec2(3, 3), 45); // 3x3 box at (4, 4) rotated 45º
        assertNoShapeCollision(r1, r2);
    }

    // Triangle vs Triangle

    @Test
    void triangleVsTriangleHitInterior() {
        var t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var t2 = new Triangle(new Vec2(1, 1), new Vec2(2, 4), new Vec2(3, 1)); // 2x3 triangle from (1, 1)
        assertShapeCollision(t1, t2);
    }

    @Test
    void triangleVsTriangleHitEdge() {
        var t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var t2 = new Triangle(new Vec2(1, 0), new Vec2(2, -3), new Vec2(3, 0)); // 2x3 triangle down from (1, 0)
        assertShapeCollision(t1, t2); // bases

        var t3 = new Triangle(new Vec2(1, 3), new Vec2(3, 1), new Vec2(5, 3)); // 4x2 triangle down from (3, 1)
        assertShapeCollision(t1, t3); // legs
    }

    @Test
    void triangleVsTriangleHitVertex() {
        var t1 = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var t2 = new Triangle(new Vec2(0, 3), new Vec2(1, 6), new Vec2(2, 3)); // 2x3 triangle from (0, 3)
        assertShapeCollision(t1, t2); // vertex vs edge

        var t3 = new Triangle(new Vec2(2, 0), new Vec2(3, 3), new Vec2(4, 0)); // 2x3 triangle from (2, 0)
        assertShapeCollision(t1, t3); // vertex vs vertex
    }

    // Shape vs Shape (Mixed)

    @Test
    void boxVsCircleHitInterior() {
        var box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        var c = new Circle(new Vec2(3, 1), 2); // 4x4 circle at (3, 1)
        assertShapeCollision(box, c);
    }

    @Test
    void boxVsCircleHitTangent() {
        var box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        var c = new Circle(new Vec2(4, 1), 2); // 4x4 circle at (4, 1)
        assertShapeCollision(box, c);
    }

    @Test
    void boxVsCircleMiss() {
        var box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        var c = new Circle(new Vec2(5, 1), 2); // 4x4 circle at (5, 1)
        assertNoShapeCollision(box, c);
    }

    @Test
    void triangleVsCircleHit() {
        var tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var c = new Circle(new Vec2(1, 4), 1.5f); // 3x3 circle at (1, 4)
        assertShapeCollision(tri, c);
    }

    @Test
    void triangleVsCircleMiss() {
        var tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        var c = new Circle(new Vec2(5, 4), 1.5f); // 3x3 circle at (5, 4)
        assertNoShapeCollision(tri, c);
    }

    @Test
    void boxVsTriangleHit() {
        var box = new Rectangle(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        var tri = new Triangle(new Vec2(0, 0), new Vec2(1, 3), new Vec2(2, 0)); // 2x3 triangle from (0, 0)
        assertShapeCollision(box, tri);
    }

    @Test
    void boxVsTriangleMiss() {
        var box = new BoundingBox(new Vec2(0, 0), new Vec2(4, 4)); // 4x4 rectangle at (0, 0)
        var tri = new Triangle(new Vec2(3, 1), new Vec2(4, 4), new Vec2(5, 1)); // 2x3 triangle from (3, 1)
        assertNoShapeCollision(box, tri);
    }

}
