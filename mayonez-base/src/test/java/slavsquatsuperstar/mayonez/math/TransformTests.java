package slavsquatsuperstar.mayonez.math;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.Transform;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link slavsquatsuperstar.mayonez.Transform} class.
 *
 * @author SlavSquatSuperstar
 */
public class TransformTests {

    @Test
    public void equivalentTransformEquals() {
        Transform t1 = new Transform(new Vec2(2, 2));
        Transform t2 = new Transform(new Vec2(2, 2));
        assertEquals(t1, t2);
    }

    @Test
    public void sameTransformEquals() {
        Transform t = new Transform(new Vec2(2, 2));
        assertSame(t, t);
        assertEquals(t, t);
    }

    @Test
    public void differentTransformNotEquals() {
        Transform t1 = new Transform(new Vec2(2, 2));
        Transform t2 = new Transform(new Vec2(3, 4));
        assertNotEquals(t1, t2);
    }

    @Test
    public void nullTransformNotEquals() {
        Transform t = new Transform(new Vec2(2, 2));
        assertNotEquals(t, null);
        assertNotEquals(null, t);
    }

}
