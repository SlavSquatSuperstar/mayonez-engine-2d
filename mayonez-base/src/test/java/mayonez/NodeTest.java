package mayonez;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.Node} class.
 *
 * @author SlavSquatSuperstar
 */
class NodeTest {

    private static final String TAG1 = "Tag 1";
    private static final String TAG2 = "Tag 2";
    private static final String TAG3 = "Tag 3";

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

    // Node Tags

    @Test
    void nodeHasTagSuccess() {
        node1.addTag(TAG1);
        node1.addTag(TAG2);

        assertTrue(node1.hasTag(TAG1));
        assertTrue(node1.hasTag(TAG2));
        assertFalse(node1.hasTag(TAG3));
    }

    @Test
    void nodeAddTagSuccess() {
        assertEquals(0, node1.getTags().size());

        node1.addTag(TAG1);
        assertEquals(Set.of(TAG1), node1.getTags());

        node1.addTag(TAG2);
        assertEquals(Set.of(TAG1, TAG2), node1.getTags());
    }

    @Test
    void nodeCannotAddNullTag() {
        node1.addTag(TAG1);
        assertEquals(Set.of(TAG1), node1.getTags());

        node1.addTag(null);
        assertEquals(Set.of(TAG1), node1.getTags());
    }

    @Test
    void nodeCannotAddDuplicateTag() {
        node1.addTag(TAG1);
        assertEquals(1, node1.getTags().size());

        node1.addTag(TAG1);
        assertEquals(1, node1.getTags().size());
    }

    @Test
    void nodeRemoveTagSuccess() {
        node1.addTag(TAG1);
        node1.addTag(TAG2);
        assertEquals(Set.of(TAG1, TAG2), node1.getTags());

        node1.removeTag(TAG1);
        assertEquals(Set.of(TAG2), node1.getTags());

        node1.removeTag(TAG2);
        assertEquals(0, node1.getTags().size());
    }

    @Test
    void nodeClearTagsSuccess() {
        node1.addTag(TAG1);
        node1.addTag(TAG2);

        node1.clearTags();
        assertEquals(0, node1.getTags().size());
    }

    // Helper Classes

    private static class NodeA extends Node {
        public NodeA(String name) {
            super(name);
        }
    }

}