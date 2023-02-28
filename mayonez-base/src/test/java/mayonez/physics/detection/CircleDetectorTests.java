package mayonez.physics.detection;

import mayonez.math.*;
import mayonez.math.shapes.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the {@link mayonez.physics.detection.CircleDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class CircleDetectorTests {

    private Circle c1;

    @BeforeEach
    public void getCircle() {
        c1 = new Circle(new Vec2(0, 0), 5);
    }

    @Test
    public void circlesTouchOneContact() { // touch but don't intersect
        var c2 = c1.translate(new Vec2(10, 0));

        assertTrue(CircleDetector.checkIntersection(c1, c2));
        testCircleContact(c1, c2, new Vec2(5, 0));
    }

    @Test
    public void circlesOverlapOneContact() {
        var c2 = c1.translate(new Vec2(9.9f, 0));

        assertTrue(CircleDetector.checkIntersection(c1, c2));
        testCircleContact(c1, c2, new Vec2(4.9f, 0));
    }

    @Test
    public void circlesSeparatedNoContacts() {
        var c2 = c1.translate(new Vec2(10.1f, 0));

        assertFalse(CircleDetector.checkIntersection(c1, c2));
        assertNull(CircleDetector.getContacts(c1, c2));
    }

    private void testCircleContact(Circle c1, Circle c2, Vec2 contact) {
        var contacts = CircleDetector.getContacts(c1, c2);
        assertNotNull(contacts);
        assertEquals(1, contacts.numContacts());
        assertEquals(contact, contacts.getContact(0));
    }

}
