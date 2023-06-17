package mayonez.physics.detection

import mayonez.math.*
import mayonez.math.shapes.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

/**
 * Unit Tests for the [mayonez.physics.detection.CircleDetector] class.
 *
 * @author SlavSquatSuperstar
 */
internal class CircleDetectorTest {

    private lateinit var c1: Circle

    @BeforeEach
    fun createCircle() {
        c1 = Circle(Vec2(0f, 0f), 5f)
    }

    @Test
    fun circlesTouchOneContact() { // touch but don't intersect
        val c2 = c1.translate(Vec2(10f, 0f))

        assertTrue(CircleDetector.checkIntersection(c1, c2))
        testCircleContact(c1, c2, Vec2(5f, 0f))
    }

    @Test
    fun circlesOverlapOneContact() {
        val c2 = c1.translate(Vec2(9.9f, 0f))

        assertTrue(CircleDetector.checkIntersection(c1, c2))
        testCircleContact(c1, c2, Vec2(4.9f, 0f))
    }

    @Test
    fun circlesSeparatedNoContacts() {
        val c2 = c1.translate(Vec2(10.1f, 0f))
        assertFalse(CircleDetector.checkIntersection(c1, c2))
        assertNull(CircleDetector.getContacts(c1, c2))
    }

    private fun testCircleContact(c1: Circle?, c2: Circle, contact: Vec2) {
        val contacts = CircleDetector.getContacts(c1, c2)

        assertNotNull(contacts)
        assertEquals(1, contacts!!.numContacts())
        assertEquals(contact, contacts.getContact(0))
    }

}
