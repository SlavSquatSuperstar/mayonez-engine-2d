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
    void applyTransformSuccess1() {
        /*
         * [[ 0.5 0  1 ]  [[ 3 ]
         *  [ 0   2 -1 ]   [ 4 ]
         *  [ 0   0  1 ]]  [ 1 ]]
         */
        var t = new Transform(
                new Vec2(1, -1),
                90,
                new Vec2(0.5f, 2)
        );
        var x = new Vec2(3, 4); // Should result in (-7, 0.5f)
        testTransform(t, x);
    }

    @Test
    void applyTransformSuccess2() {
        var t = new Transform(
                new Vec2(1, -1),
                -30,
                new Vec2(0.5f, 2)
        );
        var x = new Vec2(3, 4);
        testTransform(t, x);
    }

    // Helper Methods

    private static void testTransform(Transform t, Vec2 x) {
        var y = getTransformed(t, x);
        assertEquals(y, t.apply(x));
        assertEquals(x, t.applyInverse(y));
    }

    private static Vec2 getTransformed(Transform t, Vec2 x) {
        // Call order backwards due to matrix multiplication right-to-left
        var affXf = new AffineTransform();
        affXf.translate(t.getPosition().x, t.getPosition().y);
        affXf.rotate(MathUtils.toRadians(t.getRotation()));
        affXf.scale(t.getScale().x, t.getScale().y);

        var xPt = new Point2D.Float(x.x, x.y);
        var yPt = affXf.transform(xPt, null);
        return new Vec2((float) yPt.getX(), (float) yPt.getY());
    }

}
