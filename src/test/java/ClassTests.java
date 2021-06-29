import org.junit.Test;

import static junit.framework.TestCase.*;

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
        assertNotSame(c.getClass().getClass(), Component.class);
        assertNotSame(c.getClass().getClass(), Box.class);
    }

    static class Component {

    }

    static class Collider extends Component {

    }

    static class Box extends Collider {

    }

}
