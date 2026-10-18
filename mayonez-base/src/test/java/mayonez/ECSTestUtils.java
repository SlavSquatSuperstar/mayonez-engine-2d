package mayonez;

/**
 * A class providing utility classes for unit tests.
 *
 * @author SlavSquatSuperstar
 */
final class ECSTestUtils {

    private ECSTestUtils() {}

    static class TestScene extends Scene {
        TestScene(String name) {
            super(name);
        }
    }

    static class NodeA extends Node {
        NodeA(String name) {
            super(name);
        }
    }

    static class NodeB extends NodeA {
        NodeB(String name) {
            super(name);
        }
    }

    static class NodeC extends NodeA {
        NodeC(String name) {
            super(name);
        }
    }

}
