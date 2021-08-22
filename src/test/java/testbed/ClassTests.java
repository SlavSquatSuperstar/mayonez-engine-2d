package testbed;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Class} class.
 *
 * @author SlavSquatSuperstar
 */
public class ClassTests {

    @Test
    // <obj> instanceof <cls>: superclass, class
    public void instanceOfSucceedAndFail() {
        Component c = new Collider();
        assertTrue(c instanceof Component);
        assertTrue(c instanceof Collider);
        assertFalse(c instanceof Box);
    }

    @Test
    // <cls>.isInstance(<obj>): superclass, class, and subclass
    public void isInstanceSucceedAndFail() {
        Component c = new Collider();
        assertTrue(Component.class.isInstance(c));
        assertTrue(Collider.class.isInstance(c));
        assertFalse(Box.class.isInstance(c));
    }


    @Test
    // <cls>.isAssignableFrom(<cls>): class, subclass
    public void isAssignableFromSucceed() {
        assertTrue(Component.class.isAssignableFrom(Component.class));
        assertTrue(Component.class.isAssignableFrom(Collider.class));
        assertTrue(Component.class.isAssignableFrom(Box.class));
    }

    @Test
    public void isAssignableFromFail() {
        assertFalse(Collider.class.isAssignableFrom(Component.class));
        assertFalse(Box.class.isAssignableFrom(Component.class));
    }

    @Test
    // <obj>.getClass() == <cls>: same class only
    public void compareClassSucceedAndFail() {
        Component c = new Collider();
        assertEquals(c.getClass(), Collider.class);
        assertNotSame(c.getClass(), Component.class);
        assertNotSame(c.getClass(), Box.class);
    }

    @Test
    public void getObjectsInSceneGeneric() {
        Scene scene = new Scene("ClassTests Scene") {
            @Override
            protected void init() {
                addObject(new GameObject("obj1", new Vec2()));
                addObject(new GameObject("obj2", new Vec2()));
                addObject(new GameObject("obj3", new Vec2()));
                addObject(new A("obj4", new Vec2()));
                addObject(new A("obj5", new Vec2()));
            }
        };
        scene.start();
        assertEquals(scene.getObject("Obj1").name, "obj1");
        assertEquals(scene.getObjects(GameObject.class).get(1).getClass(), GameObject.class);
        assertEquals(scene.getObjects(A.class).get(0).getClass(), A.class);
        assertEquals(scene.getObjects(null).get(4).getClass(), A.class);

    }

    static class Component {
    }

    static class Collider extends Component {
    }

    static class Box extends Collider {
    }

    static class A extends GameObject {
        public A(String name, Vec2 position) {
            super(name, position);
        }
    }

}
