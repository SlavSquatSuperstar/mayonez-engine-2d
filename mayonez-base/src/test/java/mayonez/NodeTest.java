package mayonez;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.Node} class.
 *
 * @author SlavSquatSuperstar
 */
class NodeTest {

    private Node node1, node2;

    @BeforeEach
    void setUp() {
        node1 = new NodeA("Test Node 1");
        node2 = new NodeA("Test Node 2");
    }

    // Node ID

    @Test
    void nodeIDsAreUnique() {
        assertNotEquals(node1.nodeID, node2.nodeID);
        assertNotEquals(node1, node2);
    }

    // Node Name

    @Test
    void nodeNullNameRevertsToDefault() {
        var node = new NodeA(null);
        assertEquals("NodeA", node.getName());
    }

    @Test
    void nodeBlankNameRevertsToDefault() {
        var node = new NodeA("");
        assertEquals("NodeA", node.getName());
    }

    private static class NodeA extends Node {
        public NodeA(String name) {
            super(name);
        }
    }

}