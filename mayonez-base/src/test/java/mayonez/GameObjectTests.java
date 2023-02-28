package mayonez;

import mayonez.graphics.sprites.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link mayonez.GameObject} class.
 *
 * @author SlavSquatSuperstar
 */
public class GameObjectTests {

    @Test
    public void objectIDsAreUnique() {
        var obj1 = new GameObject("Test Object");
        var obj2 = new GameObject("Test Object");
        assertNotEquals(obj1.objectID, obj2.objectID);
    }

    @Test
    public void addComponentChangesComponentCount() {
        var obj = new GameObject("Test Object");
        assertEquals(0, obj.numComponents());

        var comp1 = mock(Collider.class);
        obj.addComponent(comp1);

        var comp2 = mock(Rigidbody.class);
        obj.addComponent(comp2);

        assertEquals(2, obj.numComponents());
    }

    @Test
    public void getComponentsSameClass() {
        var obj = new GameObject("Test Object");

        var comp1 = mock(Collider.class);
        obj.addComponent(comp1);

        var comp2 = mock(Rigidbody.class);
        obj.addComponent(comp2);

        var comp3 = mock(Counter.class);
        obj.addComponent(comp3);

        assertSame(comp1, obj.getComponent(Collider.class));
        assertSame(comp2, obj.getComponent(Rigidbody.class));
        assertSame(comp3, obj.getComponent(Counter.class));
        assertNull(obj.getComponent(Sprite.class));
    }

    @Test
    public void getComponentsSuperclass() {
        var obj = new GameObject("Test Object");

        var comp1 = mock(BoxCollider.class);
        obj.addComponent(comp1);

        var comp2 = mock(GLSprite.class);
        obj.addComponent(comp2);

        var comp3 = mock(Counter.class);
        obj.addComponent(comp3);

        assertSame(comp1, obj.getComponent(Collider.class));
        assertSame(comp2, obj.getComponent(Sprite.class));
        assertSame(comp3, obj.getComponent(Script.class));
    }

}
