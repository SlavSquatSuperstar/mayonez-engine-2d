package mayonez;

import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.GameObject} class.
 *
 * @author SlavSquatSuperstar
 */
class GameObjectTest {

    private GameObject obj1, obj2;
    private Component comp1, comp2;

    @BeforeEach
    void setUp() {
        obj1 = new GameObject("Test Object");
        obj2 = new GameObject("Test Object");
        comp1 = new ComponentA();
        comp2 = new ComponentB();
    }

    @Test
    void objectIDsAreUnique() {
        assertNotEquals(obj1.objectID, obj2.objectID);
        assertNotEquals(obj1, obj2);
    }

    @Test
    void addComponentChangesNumComponents() {
        assertEquals(0, obj1.numComponents());

        obj1.addComponent(comp1);
        assertEquals(1, obj1.numComponents());

        obj1.addComponent(comp2);
        assertEquals(2, obj1.numComponents());
    }

    @Test
    void cannotAddNullComponent() {
        obj1.addComponent(null);
        assertEquals(0, obj1.numComponents());
    }

    @Test
    void cannotAddComponentTwice() {
        obj1.addComponent(comp1);
        obj1.addComponent(comp1);

        assertSame(obj1, comp1.getGameObject());
        assertEquals(1, obj1.numComponents());
    }

    @Test
    void cannotAddComponentToDifferentObjects() {
        obj1.addComponent(comp1);
        obj2.addComponent(comp1);

        assertSame(obj1, comp1.getGameObject());
        assertEquals(1, obj1.numComponents());
        assertEquals(0, obj2.numComponents());
    }

    @Test
    void getOneComponentSameClass() {
        obj1.addComponent(comp1);
        obj1.addComponent(comp2);

        assertSame(comp1, obj1.getComponent(ComponentA.class));
        assertSame(comp2, obj1.getComponent(ComponentB.class));

        assertNull(obj1.getComponent(ComponentC.class));
    }

    @Test
    void getAllComponentsSameClass() {
        var comp1 = new ComponentC();
        obj1.addComponent(comp1);

        var comp2 = new ComponentC();
        obj1.addComponent(comp2);

        var components = obj1.getComponents(ComponentC.class);
        assertNotNull(components);
        assertEquals(2, components.size());

        assertTrue(components.contains(comp1));
        assertTrue(components.contains(comp2));
    }

    @Test
    void getOneComponentSuperclass() {
        obj1.addComponent(comp1);
        obj1.addComponent(comp2);

        assertSame(comp1, obj1.getComponent(Component.class));
    }

    @Test
    void getAllComponentsSuperClass() {
        obj1.addComponent(comp1);
        obj1.addComponent(comp2);

        var components = obj1.getComponents(Component.class);
        assertNotNull(components);
        assertEquals(2, components.size());

        assertTrue(components.contains(comp1));
        assertTrue(components.contains(comp2));
    }

    @Test
    void getComponentNullClassIsNone() {
        obj1.addComponent(comp1);
        obj1.addComponent(comp2);

        assertNull(obj1.getComponent((Class<? extends Component>) null));
        assertTrue(obj1.getComponents(null).isEmpty());
    }

    @Test
    void getComponentByNameSuccess() {
        comp1.setName("Test Component");
        comp2.setName("Test Component");

        obj1.addComponent(comp1);
        obj1.addComponent(comp2);

        assertSame(comp1, obj1.getComponent("Test Component"));
    }

    // Subclasses

    private static class ComponentA extends Component {
    }

    private static class ComponentB extends Component {
    }

    private static class ComponentC extends Component {
    }

}
