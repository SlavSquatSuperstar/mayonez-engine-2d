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

    private GameObject obj;

    @BeforeEach
    void getObject() {
        obj = new GameObject("Test Object");
    }

    @Test
    void objectIDsAreUnique() {
        var obj2 = new GameObject("Test Object");
        assertNotEquals(obj.objectID, obj2.objectID);
    }

    @Test
    void addComponentChangesNumComponents() {
        assertEquals(0, obj.numComponents());

        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);
        assertEquals(1, obj.numComponents());

        var comp2 = new Rigidbody(1f);
        obj.addComponent(comp2);
        assertEquals(2, obj.numComponents());
    }

    @Test
    void cannotAddObjectTwice() {
        var comp = new Script() {
        };
        obj.addComponent(comp);
        obj.addComponent(comp);

        assertEquals(1, obj.numComponents());
    }

    @Test
    void cannotAddComponentToDifferentObjects() {
        var obj2 = new GameObject("Test Object");
        var comp = new Script() {
        };

        obj2.addComponent(comp);
        obj.addComponent(comp);

        assertEquals(0, obj.numComponents());
    }

    @Test
    void getOneComponentSameClass() {
        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);

        var comp2 = new Rigidbody(1f);
        obj.addComponent(comp2);

        assertSame(comp1, obj.getComponent(BoxCollider.class));
        assertSame(comp2, obj.getComponent(Rigidbody.class));

        assertNull(obj.getComponent(Sprite.class));
    }

    @Test
    void getAllComponentsSameClass() {
        var comp1 = new Script() {
        };
        obj.addComponent(comp1);

        var comp2 = new Script() {
        };
        obj.addComponent(comp2);

        var comp3 = new Rigidbody(1f);
        obj.addComponent(comp3);

        var scripts = obj.getComponents(Script.class);
        assertNotNull(scripts);

        assertEquals(2, scripts.size());
        assertTrue(scripts.contains(comp1));
        assertTrue(scripts.contains(comp2));
    }

    @Test
    void getOneComponentSuperclass() {
        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);

        var comp2 = new TimerScript(1f);
        obj.addComponent(comp2);

        assertSame(comp1, obj.getComponent(Collider.class));
        assertSame(comp2, obj.getComponent(Script.class));
    }

    @Test
    void getAllComponentsSuperClass() {
        var comp1 = new BoxCollider(new Vec2(1f));
        obj.addComponent(comp1);

        var comp2 = new BallCollider(1f);
        obj.addComponent(comp2);

        var comp3 = new Rigidbody(1f);
        obj.addComponent(comp3);

        var colliders = obj.getComponents(Collider.class);
        assertNotNull(colliders);

        assertEquals(2, colliders.size());
        assertTrue(colliders.contains(comp1));
        assertTrue(colliders.contains(comp2));
    }

    @Test
    void getComponentNullClassIsNull() {
        obj.addComponent(new BoxCollider(new Vec2(1f)));
        obj.addComponent(new Rigidbody(1f));
        obj.addComponent(new TimerScript(1f));

        assertNull(obj.getComponent(null));
        assertNull(obj.getComponents(null));
    }

}
