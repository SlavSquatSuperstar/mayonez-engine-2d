package mayonez.physics.detection;

import mayonez.math.Vec2;
import mayonez.math.shapes.Polygon;
import mayonez.math.shapes.Rectangle;
import mayonez.math.shapes.Triangle;
import org.junit.jupiter.api.Test;

import static mayonez.test.TestUtils.assertFloatEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit Tests for the {@link mayonez.physics.detection.SATDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class SATTests {

    private static final Polygon p1 = new Triangle(new Vec2(4, 5), new Vec2(9, 9), new Vec2(4, 11));
    private static final Polygon p2 = new Polygon(new Vec2(5, 7), new Vec2(7, 3), new Vec2(10, 2), new Vec2(12, 7));

    // Rectangle Tests
    @Test
    public void touchingRectanglesTwoContacts() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(4, 1), new Vec2(4, 4));
        var pen = new SATDetector().getPenetration(r1, r2);
        assertFloatEquals(pen.getDepth(), 0f);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        var man = pen.getContacts(r1, r2);
        var pts1 = new Vec2[]{man.getContact(0), man.getContact(1)};
        var pts2 = new Vec2[]{new Vec2(2, -1), new Vec2(2, 2)};
        assertArrayEquals(pts1, pts2);
    }

    @Test
    public void overlappingRectanglesTwoContacts() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(3.9f, 0.9f), new Vec2(4, 4));
        var pen = new SATDetector().getPenetration(r1, r2);
        assertFloatEquals(pen.getDepth(), 0.1f);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        var man = pen.getContacts(r1, r2);
        var pts1 = new Vec2[]{man.getContact(0), man.getContact(1)};
        var pts2 = new Vec2[]{new Vec2(1.9f, -1.1f), new Vec2(1.9f, 2)};
        assertArrayEquals(pts1, pts2);
    }

    @Test
    public void cornerIntersectionOneContact() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(4.5f, 0), new Vec2(4, 4), 45);
        var pen = new SATDetector().getPenetration(r1, r2);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        var man = pen.getContacts(r1, r2);
        assertEquals(1, man.numContacts());
    }

    @Test
    public void obliqueIntersectionTwoContacts() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(2.5f, 0), new Vec2(4, 4), 10);
        var pen = new SATDetector().getPenetration(r1, r2);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        var man = pen.getContacts(r1, r2);
        assertEquals(2, man.numContacts());
    }

}
