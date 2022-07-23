package slavsquatsuperstar.test.collisiontests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.physics.shapes.Ray;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BoxCollider} class.
 *
 * @author SlavSquatSuperstar
 */
public class BoxTests {

    static BoxCollider box;

    // 4x4 centered at (0, 0) rotated by 45 degrees
    @BeforeAll
    public static void getBox() {
        box = new BoxCollider(new Vec2(2, 2)).setTransform(new Transform(new Vec2(), 45, new Vec2(2, 2)));
    }

    // Nearest Point

    @Test
    public void nearestPointInsideBox() {
        assertEquals(new Vec2(1, 1), box.nearestPoint(new Vec2(1, 1)));
    }

    @Test
    public void nearestPointOutsideBox() {
        assertEquals(new Vec2(2, 0).rotate(45), box.nearestPoint(new Vec2(2, 2)));
        assertEquals(new Vec2(2, 2).rotate(45), box.nearestPoint(new Vec2(3, 4).rotate(45)));
    }

    // Raycast

    @Test
    public void outsideRayHitsBox() {
        assertNotNull(box.raycast(new Ray(new Vec2(-10, 0), new Vec2(1, 0)), 0));
        assertNotNull(box.raycast(new Ray(new Vec2(-5, 5), new Vec2(1, -1)), 0));
    }

    @Test
    public void limitedOutsideRayHitsBox() {
        assertNotNull(box.raycast(new Ray(new Vec2(-4, 0), new Vec2(1, 0)), 5));
        assertNotNull(box.raycast(new Ray(new Vec2(-4, 4), new Vec2(1, -1)), 5));
    }

    @Test
    public void outsideRayMissesBox() {
        assertNull(box.raycast(new Ray(new Vec2(-10, 0), new Vec2(-1, 0)), 0));
        assertNull(box.raycast(new Ray(new Vec2(-5, 5), new Vec2(-1, 1)), 0));
    }

    @Test
    public void limitedOutsideRayMissesBox() {
        assertNull(box.raycast(new Ray(new Vec2(-10, 0), new Vec2(1, 0)), 2));
        assertNull(box.raycast(new Ray(new Vec2(-10, 10), new Vec2(1, -1)), 2));
    }

    @Test
    public void insideRayHitsBox() {
        assertNotNull(box.raycast(new Ray(new Vec2(-1.5f, 0), new Vec2(1, 0)), 0));
        assertNotNull(box.raycast(new Ray(new Vec2(-1, 1), new Vec2(1, -1)), 0));
    }

}
