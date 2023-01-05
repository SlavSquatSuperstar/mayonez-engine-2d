package mayonez.test.javatests;

import mayonez.*;
import org.junit.jupiter.api.Test;
import mayonez.math.Vec2;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.colliders.Collider;
import mayonez.physics.shapes.Circle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link java.lang.Class} class.
 *
 * @author SlavSquatSuperstar
 */
public class ClassTests {

    private static Circle shapeData = new Circle(new Vec2(0, 0), 1);

    @Test
    // <obj> instanceof <cls>: superclass, class
    public void instanceOfSucceedAndFail() {
        Component c = new Collider(shapeData) {};
        assertTrue(c instanceof Component);
        assertTrue(c instanceof Collider);
        assertFalse(c instanceof BoxCollider);
    }

    @Test
    // <cls>.isInstance(<obj>): superclass, class, and subclass
    public void isInstanceSucceedAndFail() {
        Component c = new Collider(shapeData) {};
        assertTrue(Component.class.isInstance(c));
        assertTrue(Collider.class.isInstance(c));
        assertFalse(BoxCollider.class.isInstance(c));
    }


    @Test
    // <cls>.isAssignableFrom(<cls>): class, subclass
    public void isAssignableFromSucceed() {
        assertTrue(Component.class.isAssignableFrom(Component.class));
        assertTrue(Component.class.isAssignableFrom(Collider.class));
        assertTrue(Component.class.isAssignableFrom(BoxCollider.class));
    }

    @Test
    public void isAssignableFromFail() {
        assertFalse(Collider.class.isAssignableFrom(Component.class));
        assertFalse(BoxCollider.class.isAssignableFrom(Component.class));
    }

    @Test
    // <obj>.getClass() == <cls>: same class only
    public void compareClassSucceedAndFail() {
        Component c = new TestCollider(); // can't be anonymous
        assertEquals(c.getClass(), TestCollider.class);
        assertNotSame(c.getClass(), Component.class);
        assertNotSame(c.getClass(), Collider.class);
        assertNotSame(c.getClass(), BoxCollider.class);
    }

    @Test
    public void getObjectsInSceneGeneric() {
        // TODO needs decoupling
        Mayonez.setUseGL(false);
        Scene scene = new Scene("ClassTests Scene") {
            @Override
            protected void init() {
                addObject(new GameObject("obj1", new Vec2()));
                addObject(new GameObject("obj2", new Vec2()));
                addObject(new GameObject("obj3", new Vec2()));
                addObject(new TestObject("obj4", new Vec2()));
                addObject(new TestObject("obj5", new Vec2()));
            }
        };
        SceneManager.setScene(scene);
        scene.start();
        assertNull(scene.getObject("Obj1"));
        assertEquals(scene.getObject("obj1").name, "obj1");
        assertEquals(scene.getObjects(GameObject.class).get(1).getClass(), GameObject.class);
        assertEquals(scene.getObjects(TestObject.class).get(0).getClass(), TestObject.class);
        assertEquals(scene.getObjects().get(4).getClass(), TestObject.class);

    }
    
    private static class TestObject extends GameObject {
        public TestObject(String name, Vec2 position) {
            super(name, position);
        }
    }

    private static class TestCollider extends Collider {
        public TestCollider() {
            super(shapeData);
        }
    }

}
