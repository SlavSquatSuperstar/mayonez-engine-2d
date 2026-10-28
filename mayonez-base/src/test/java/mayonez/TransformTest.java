package mayonez;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import java.awt.geom.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Transform} class.
 *
 * @author SlavSquatSuperstar
 */
class TransformTest {

    private static final float FLOAT_DELTA = 1e-4f;

    // Transform Equality

    @Test
    void equivalentTransformEquals() {
        var t1 = new Transform(
                new Vec2(1, 0), 45, new Vec2(2, 2)
        );
        var t2 = new Transform(
                new Vec2(1, 0), 45, new Vec2(2, 2)
        );
        assertEquals(t1, t2);
    }

    @Test
    void differentTransformNotEquals() {
        var t1 = new Transform(
                new Vec2(1, 0), 45, new Vec2(2, 2)
        );
        var t2 = new Transform(
                new Vec2(0, -1), 45, new Vec2(2, 2)
        );
        var t3 = new Transform(
                new Vec2(1, 0), 30, new Vec2(2, 2)
        );
        var t4 = new Transform(
                new Vec2(1, 0), 45, new Vec2(3, 4)
        );
        assertNotEquals(t1, t2); // Position differs
        assertNotEquals(t1, t3); // Rotation differs
        assertNotEquals(t1, t4); // Scale differs
    }

    // Transform Operations

    @Test
    void applyTransformSuccess() {
        /*
         * [  0   2  1   • [ 3   = [ -7
         *   -0.5 0 -1 ]     4        0.5 ]
         *                   0 ]
         */
        var t = new Transform(new Vec2(1f, -1f), 90f, new Vec2(0.5f, 2f));
        var x = new Vec2(3f, 4f);
        var y = new Vec2(-7f, 0.5f);
        assertVec2Equals(y, t.apply(x));
        assertVec2Equals(x, t.applyInverse(y));
    }

    @Test
    void applyTransformSuccessRandom() {
        // Use Java AffineTransform as oracle
        var t = randomTransform();
        var x = randomVector();
        var y = getTransformedPoint(t, x);
        assertVec2Equals(y, t.apply(x));
        assertVec2Equals(x, t.applyInverse(y));
    }

    @Test
    void combineTransformSuccess() {
        // Need to use uniform scale
        var t1 = new Transform(
                new Vec2(1, -1),
                90,
                new Vec2(0.5f, 0.5f)
        );
        var t2 = new Transform(
                new Vec2(-4, 2),
                -45,
                new Vec2(1.5f, 1.5f)
        );
        var combined = t1.combine(t2);

        // Check determinant
        assertEquals(
                t1.getDeterminant() * t2.getDeterminant(),
                combined.getDeterminant(), FLOAT_DELTA
        );

        // Check transform correct
        var x = new Vec2(3, 4);
        var yHat = combined.apply(x);
        var y = getTransformedPoint(combined, x);
        assertVec2Equals(y, yHat);

        // Check associativity
        assertVec2Equals(t1.apply(t2.apply(x)), yHat);
    }

    @Test
    void combineTransformSuccessRandom() {
        var t1 = randomTransform();
        var t2 = randomTransform();
        var combined = t1.combine(t2);

        // Check determinant
        assertEquals(
                t1.getDeterminant() * t2.getDeterminant(),
                combined.getDeterminant(), FLOAT_DELTA
        );

        // Check transform correct
        var x = randomVector();
        var y = getTransformedPoint(combined, x);
        var yHat = combined.apply(x);
        assertVec2Equals(y, yHat);

        // Check associativity
        assertVec2Equals(t1.apply(t2.apply(x)), yHat);
    }

    // Transform Space Tests

    @Test
    void transformBoundingRectangleCorrect() {
        var xf = randomTransform();
        var rect = xf.getBoundingRectangle();
        var det = xf.getScale().x * xf.getScale().y;
        assertEquals(det, rect.area());
    }

    // Helper Methods

    private Transform randomTransform() {
        // Scale must be uniform
        return new Transform(
                randomVector(),
                randomRange(-180, 180, 5f),
                new Vec2(randomRange(-5, 5, 0.25f))
        );
    }

    private Vec2 randomVector() {
        return new Vec2(
                randomRange(-10, 10, 0.25f),
                randomRange(-10, 10, 0.25f)
        );
    }

    private float randomRange(float min, float max, float inc) {
        var range = max - min;
        var numVals = (int) (range / inc);
        var bucket = Random.randomInt(0, numVals);
        return min + bucket * inc;
    }

    private static Vec2 getTransformedPoint(Transform t, Vec2 x) {
        var affXf = getAffineTransform(t);
        var xPt = new Point2D.Float(x.x, x.y);
        var yPt = affXf.transform(xPt, null);
        return new Vec2((float) yPt.getX(), (float) yPt.getY());
    }

    private static AffineTransform getAffineTransform(Transform t) {
        // Call order backwards due to matrix multiplication right-to-left
        var affXf = AffineTransform
                .getTranslateInstance(t.getPosition().x, t.getPosition().y);
        affXf.rotate(MathUtils.toRadians(t.getRotation()));
        affXf.scale(t.getScale().x, t.getScale().y);
        return affXf;
    }

    private static void assertVec2Equals(Vec2 expected, Vec2 actual) {
        assertEquals(expected.x, actual.x, FLOAT_DELTA);
        assertEquals(expected.y, actual.y, FLOAT_DELTA);
    }

}
