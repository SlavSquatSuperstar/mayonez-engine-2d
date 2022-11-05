package slavsquatsuperstar.mayonez.physics.detection;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.collision.CollisionInfo;
import slavsquatsuperstar.mayonez.physics.shapes.Circle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the {@link slavsquatsuperstar.mayonez.physics.detection.CircleDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class CircleDetectorTests {

    @Test
    public void circlesTouchOneContact() { // touch but don't intersect
        Circle c1 = new Circle(new Vec2(0, 0), 5);
        Circle c2 = new Circle(new Vec2(10, 0), 5);
        assertTrue(CircleDetector.checkIntersection(c1, c2));
        CollisionInfo contacts = CircleDetector.getContacts(c1, c2);
        assertEquals(new Vec2(5, 0), contacts.getContact(0));
    }

    @Test
    public void circlesOverlapOneContact() {
        Circle c1 = new Circle(new Vec2(0, 0), 5);
        Circle c2 = new Circle(new Vec2(9.9f, 0), 5);
        assertTrue(CircleDetector.checkIntersection(c1, c2));
        CollisionInfo contacts = CircleDetector.getContacts(c1, c2);
        assertEquals(new Vec2(4.9f, 0), contacts.getContact(0));
    }

    @Test
    public void circlesNoIntersectNoContacts() {
        Circle c1 = new Circle(new Vec2(0, 0), 5);
        Circle c2 = new Circle(new Vec2(10.1f, 0), 5);
        assertFalse(CircleDetector.checkIntersection(c1, c2));
        assertNull(CircleDetector.getContacts(c1, c2));
    }
}
