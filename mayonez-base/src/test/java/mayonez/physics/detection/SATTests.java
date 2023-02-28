package mayonez.physics.detection;

import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.resolution.*;
import org.junit.jupiter.api.*;

import static mayonez.test.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the {@link mayonez.physics.detection.SATDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class SATTests {

    private static final Polygon p1 = new Triangle(new Vec2(4, 5), new Vec2(9, 9), new Vec2(4, 11));
    private static final Polygon p2 = new Polygon(new Vec2(5, 7), new Vec2(7, 3), new Vec2(10, 2), new Vec2(12, 7));

    private SATDetector sat;

    @BeforeEach
    public void getSAT() {
        sat = new SATDetector();
    }

    // Rectangle Tests
    @Test
    public void touchingRectanglesTwoContacts() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(4, 1), new Vec2(4, 4));

        var pen = testPenetration(r1, r2, new Vec2(1, 0));
        assertFloatEquals(0f, pen.getDepth());

        var man = testContacts(pen, r1, r2, 2);
        var pts1 = new Vec2[]{man.getContact(0), man.getContact(1)};
        var pts2 = new Vec2[]{new Vec2(2, -1), new Vec2(2, 2)};
        assertArrayEquals(pts1, pts2);
    }

    @Test
    public void overlappingRectanglesTwoContacts() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(3.9f, 0.9f), new Vec2(4, 4));

        var pen = testPenetration(r1, r2, new Vec2(1, 0));
        assertFloatEquals(0.1f, pen.getDepth());

        var man = testContacts(pen, r1, r2, 2);
        var pts1 = new Vec2[]{man.getContact(0), man.getContact(1)};
        var pts2 = new Vec2[]{new Vec2(1.9f, -1.1f), new Vec2(1.9f, 2)};
        assertArrayEquals(pts1, pts2);
    }

    @Test
    public void cornerIntersectionOneContact() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(4.5f, 0), new Vec2(4, 4), 45);

        var pen = testPenetration(r1, r2, new Vec2(1, 0));

        testContacts(pen, r1, r2, 1);
    }

    @Test
    public void obliqueIntersectionTwoContacts() {
        var r1 = new Rectangle(new Vec2(0, 0), new Vec2(4, 4));
        var r2 = new Rectangle(new Vec2(2.5f, 0), new Vec2(4, 4), 10);

        var pen = testPenetration(r1, r2, new Vec2(1, 0));

        testContacts(pen, r1, r2, 2);
    }

    private Penetration testPenetration(Shape shape1, Shape shape2, Vec2 normal) {
        var pen = sat.getPenetration(shape1, shape2);
        assertNotNull(pen);
        assertEquals(pen.getNormal(), normal);
        return pen;
    }

    private Manifold testContacts(Penetration pen, Shape shape1, Shape shape2, int count) {
        var man = pen.getContacts(shape1, shape2);
        assertNotNull(man);
        assertEquals(count, man.numContacts());
        return man;
    }

}
