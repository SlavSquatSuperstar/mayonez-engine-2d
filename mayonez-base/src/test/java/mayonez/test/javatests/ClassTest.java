package mayonez.test.javatests;

import mayonez.*;
import mayonez.physics.colliders.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link java.lang.Class} class.
 *
 * @author SlavSquatSuperstar
 */
class ClassTest {

    private Collider collider;

    @BeforeEach
    void getCollider() {
        collider = mock(Collider.class);
    }

    @Test
    void objectInstanceOfClass() {
        assertTrue(collider instanceof Component); // super class
        assertTrue(collider instanceof Collider); // class
        assertFalse(collider instanceof BoxCollider); // subclass (fails)
    }

    @Test
    void classIsInstanceObject() {
        assertTrue(Component.class.isInstance(collider)); // super class
        assertTrue(Collider.class.isInstance(collider)); // class
        assertFalse(BoxCollider.class.isInstance(collider)); // subclass (fails)
    }

    @Test
    void superclassAssignableFromSubclass() {
        assertTrue(Component.class.isAssignableFrom(Component.class));
        assertTrue(Component.class.isAssignableFrom(Collider.class));
        assertTrue(Component.class.isAssignableFrom(BoxCollider.class));
    }

    @Test
    void subclassNotAssignableFromSuperclass() {
        assertFalse(Collider.class.isAssignableFrom(Component.class));
        assertFalse(BoxCollider.class.isAssignableFrom(Component.class));
    }

    @Test
    void getClassReturnsActualClass() {
        Component c = mock(BallCollider.class); // declare as superclass

        assertEquals(c.getClass(), BallCollider.class);
        assertNotEquals(c.getClass(), Component.class);
        assertNotEquals(c.getClass(), Collider.class);
        assertNotEquals(c.getClass(), BoxCollider.class);
    }

}
