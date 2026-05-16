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

    // Transform Application

    @Test
    void applyTransformSuccess() {
        // Generate random case
        // Use Java AffineTransform as oracle
        /*
         * Example
         * [ 0.5 0  1   • [ 3   = [ -7
         *   0   2 -1 ]     4 ]      0.5 ]
         */
        var t = randomTransform();
        var x = randomVector();
        var y = getTransformedPoint(t, x);
        assertVec2Equals(y, t.apply(x));
        assertVec2Equals(x, t.applyInverse(y));
    }

    @Disabled
    @Test
    void combineTransformSuccess1() {
        var t1 = new Transform(
                new Vec2(1, -1),
                90,
                new Vec2(0.5f, 2)
        );
        var t2 = new Transform(
                new Vec2(-10, 5),
                -45,
                new Vec2(1.5f, 0.75f)
        );
        var combined = t1.combine(t2);
        var affXfCombined = concatenate(t1, t2);
        var affXfCombined2 = getAffineTransform(combined);

        // Apply combined transforms
        var x = new Vec2(3, 4);
        var y = getTransformedPoint(combined, x);
        var y2 = getTransformedPoint(affXfCombined, x);
        var y3 = t1.apply(t2.apply(x));
        assertVec2Equals(y, combined.apply(x));

        // Check associativity
        assertVec2Equals(y, y3);
    }

    @Disabled
    @Test
    void combineTransformSuccess2() {
        var t1 = randomTransform();
        var t2 = randomTransform();
        var combined = t1.combine(t2);

        // Apply combined transforms
        var x = new Vec2(3, 4);
        var y = getTransformedPoint(combined, x);
        var yHat = combined.apply(x);
        assertVec2Equals(y, yHat);

        // Check associativity
        var foo = t2.apply(t1.apply(x));
        var bar = t1.apply(t2.apply(x));
        assertVec2Equals(t2.apply(t1.apply(x)), yHat);
        assertVec2Equals(t1.apply(t2.apply(x)), yHat);
    }

    @Disabled
    @Test
    void invertTransformSuccess() {
        // check invert correct
        // check invert combine original equals identity
        // check original combine invert equals identity
        // check combine inverse associativity
    }

    // Helper Methods

    private Transform randomTransform() {
        return new Transform(
                randomVector(),
                randomRange(-360, 360, 15f),
                randomVector()
        );
    }

    private Vec2 randomVector() {
        return new Vec2(
                randomRange(-10, 10, 0.5f),
                randomRange(-10, 10, 0.5f)
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

    private static Vec2 getTransformedPoint(AffineTransform affXf, Vec2 x) {
        var xPt = new Point2D.Float(x.x, x.y);
        var yPt = affXf.transform(xPt, null);
        return new Vec2((float) yPt.getX(), (float) yPt.getY());
    }

    private static AffineTransform concatenate(Transform t1, Transform t2) {
        var affXf1 = getAffineTransform(t1);
        var affXf2 = getAffineTransform(t2);
        var affXfCombined = new AffineTransform(affXf1);
        affXfCombined.concatenate(affXf2);
        return affXfCombined;
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
