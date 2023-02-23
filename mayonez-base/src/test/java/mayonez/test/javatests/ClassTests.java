package mayonez.test.javatests;

import mayonez.Component;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.colliders.Collider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link java.lang.Class} class.
 *
 * @author SlavSquatSuperstar
 */
public class ClassTests {

    private Collider collider;

    @BeforeEach
    public void getCollider() {
        collider = mock(Collider.class);
    }

    @Test
    public void objectInstanceOfClass() {
        assertTrue(collider instanceof Component); // super class
        assertTrue(collider instanceof Collider); // class
        assertFalse(collider instanceof BoxCollider); // subclass (fails)
    }

    @Test
    public void classIsInstanceObject() {
        assertTrue(Component.class.isInstance(collider)); // super class
        assertTrue(Collider.class.isInstance(collider)); // class
        assertFalse(BoxCollider.class.isInstance(collider)); // subclass (fails)
    }

    @Test
    public void superclassAssignableFromSubclass() {
        assertTrue(Component.class.isAssignableFrom(Component.class));
        assertTrue(Component.class.isAssignableFrom(Collider.class));
        assertTrue(Component.class.isAssignableFrom(BoxCollider.class));
    }

    @Test
    public void subclassNotAssignableFromSuperclass() {
        assertFalse(Collider.class.isAssignableFrom(Component.class));
        assertFalse(BoxCollider.class.isAssignableFrom(Component.class));
    }

    @Test
    public void getClassReturnsActualClass() {
        Component c = mock(BallCollider.class); // declare as superclass

        assertEquals(c.getClass(), BallCollider.class);
        assertNotEquals(c.getClass(), Component.class);
        assertNotEquals(c.getClass(), Collider.class);
        assertNotEquals(c.getClass(), BoxCollider.class);
    }

}
