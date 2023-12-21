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

    @Test
    void objectIDsAreUnique() {
        var obj1 = new GameObject("Test Object");
        var obj2 = new GameObject("Test Object");
        assertNotEquals(obj1.objectID, obj2.objectID);
    }

    @Test
    void addComponentChangesNumComponents() {
        var obj = new GameObject("Test Object");
        assertEquals(0, obj.numComponents());

        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);
        assertEquals(1, obj.numComponents());

        var comp2 = new Rigidbody(1f);
        obj.addComponent(comp2);
        assertEquals(2, obj.numComponents());
    }

    @Test
    void getComponentsSameClass() {
        var obj = new GameObject("Test Object");

        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);

        var comp2 = new Rigidbody(1f);
        obj.addComponent(comp2);

        var comp3 = new Timer(1f);
        obj.addComponent(comp3);

        assertSame(comp1, obj.getComponent(BoxCollider.class));
        assertSame(comp2, obj.getComponent(Rigidbody.class));
        assertSame(comp3, obj.getComponent(Timer.class));
        assertNull(obj.getComponent(Sprite.class));
    }

    @Test
    void getComponentsSuperclass() {
        var obj = new GameObject("Test Object");

        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);

        var comp2 = new Timer(1f);
        obj.addComponent(comp2);

        assertSame(comp1, obj.getComponent(Collider.class));
        assertSame(comp2, obj.getComponent(Script.class));
    }

}
