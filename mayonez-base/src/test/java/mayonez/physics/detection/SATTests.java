package mayonez.physics.detection;

import org.junit.jupiter.api.Test;
import mayonez.math.Vec2;
import mayonez.physics.resolution.Manifold;
import mayonez.physics.shapes.Polygon;
import mayonez.physics.shapes.Rectangle;
import mayonez.physics.shapes.Triangle;

import static org.junit.jupiter.api.Assertions.*;
import static mayonez.test.TestUtils.assertFloatEquals;

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
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        Rectangle r2 = new Rectangle(new Vec2(4, 1), new Vec2(4, 4));
        Penetration pen = new SATDetector().getPenetration(r1, r2);
        assertFloatEquals(pen.getDepth(), 0f);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        Manifold man = pen.getContacts(r1, r2);
        Vec2[] pts1 = new Vec2[]{man.getContact(0), man.getContact(1)};
        Vec2[] pts2 = new Vec2[]{new Vec2(2, -1), new Vec2(2, 2)};
        assertArrayEquals(pts1, pts2);
    }

    @Test
    public void overlappingRectanglesTwoContacts() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        Rectangle r2 = new Rectangle(new Vec2(3.9f, 0.9f), new Vec2(4, 4));
        Penetration pen = new SATDetector().getPenetration(r1, r2);
        assertFloatEquals(pen.getDepth(), 0.1f);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        Manifold man = pen.getContacts(r1, r2);
        Vec2[] pts1 = new Vec2[]{man.getContact(0), man.getContact(1)};
        Vec2[] pts2 = new Vec2[]{new Vec2(1.9f, -1.1f), new Vec2(1.9f, 2)};
        assertArrayEquals(pts1, pts2);
    }

    @Test
    public void cornerIntersectionOneContact() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        Rectangle r2 = new Rectangle(new Vec2(4.5f, 0), new Vec2(4, 4), 45);
        Penetration pen = new SATDetector().getPenetration(r1, r2);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        Manifold man = pen.getContacts(r1, r2);
        assertEquals(1, man.numContacts());
    }

    @Test
    public void obliqueIntersectionTwoContacts() {
        Rectangle r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        Rectangle r2 = new Rectangle(new Vec2(2.5f, 0), new Vec2(4, 4), 10);
        Penetration pen = new SATDetector().getPenetration(r1, r2);
        assertEquals(pen.getNormal(), new Vec2(1, 0));

        Manifold man = pen.getContacts(r1, r2);
        assertEquals(2, man.numContacts());
    }

}
