package mayonez;

import mayonez.math.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Transform} class.
 *
 * @author SlavSquatSuperstar
 */
class TransformTest {

    @Test
    void equivalentTransformEquals() {
        var t1 = new Transform(new Vec2(2, 2));
        var t2 = new Transform(new Vec2(2, 2));
        assertEquals(t1, t2);
    }

    @Test
    void sameTransformEquals() {
        var t = new Transform(new Vec2(2, 2));
        assertSame(t, t);
        assertEquals(t, t);
    }

    @Test
    void differentTransformNotEquals() {
        var t1 = new Transform(new Vec2(2, 2));
        var t2 = new Transform(new Vec2(3, 4));
        assertNotEquals(t1, t2);
    }

    @Test
    void nullTransformNotEquals() {
        var t = new Transform(new Vec2(2, 2));
        assertNotEquals(t, null);
        assertNotEquals(null, t);
    }

}
