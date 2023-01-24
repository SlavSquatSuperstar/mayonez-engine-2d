package mayonez.physics.detection;

import mayonez.math.Vec2;
import mayonez.physics.shapes.Circle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the {@link mayonez.physics.detection.CircleDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class CircleDetectorTests {

    @Test
    public void circlesTouchOneContact() { // touch but don't intersect
        var c1 = new Circle(new Vec2(0, 0), 5);
        var c2 = new Circle(new Vec2(10, 0), 5);
        assertTrue(CircleDetector.checkIntersection(c1, c2));
        var contacts = CircleDetector.getContacts(c1, c2);
        assertEquals(new Vec2(5, 0), contacts.getContact(0));
    }

    @Test
    public void circlesOverlapOneContact() {
        var c1 = new Circle(new Vec2(0, 0), 5);
        var c2 = new Circle(new Vec2(9.9f, 0), 5);
        assertTrue(CircleDetector.checkIntersection(c1, c2));
        var contacts = CircleDetector.getContacts(c1, c2);
        assertEquals(new Vec2(4.9f, 0), contacts.getContact(0));
    }

    @Test
    public void circlesNoIntersectNoContacts() {
        var c1 = new Circle(new Vec2(0, 0), 5);
        var c2 = new Circle(new Vec2(10.1f, 0), 5);
        assertFalse(CircleDetector.checkIntersection(c1, c2));
        assertNull(CircleDetector.getContacts(c1, c2));
    }
}
