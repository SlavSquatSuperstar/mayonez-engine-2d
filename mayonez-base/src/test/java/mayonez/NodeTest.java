package mayonez;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static mayonez.ECSTestUtils.*;
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

    private Scene scene;
    private Node node1, node2;
    private Node child1, child2, child3;

    @BeforeEach
    void setUp() {
        scene = new TestScene("Test Scene");
        scene.createRootNode();

        node1 = new NodeA("Test Node 1");
        node2 = new NodeA("Test Node 2");

        child1 = new NodeA("Child Node 1");
        child2 = new NodeB("Child Node 2");
        child3 = new NodeB("Child Node 3");
    }

    @AfterEach
    void tearDown() {
        scene.stop();
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

    // Add/Remove Child

    @Test
    void addingChildChangesParent() {
        assertNull(child1.getParent());

        node1.addChild(child1);
        assertSame(node1, child1.getParent());
    }

    @Test
    void addingChildChangesNumChildren() {
        assertEquals(0, node1.numChildren());

        node1.addChild(child1);
        assertEquals(1, node1.numChildren());

        node1.addChild(child2);
        assertEquals(2, node1.numChildren());

        assertEquals(List.of(child1, child2), node1.getChildren());
    }

    @Test
    void cannotAddNullChild() {
        assertEquals(0, node1.numChildren());

        node1.addChild(null);
        assertEquals(0, node1.numChildren());
    }

    @Test
    void cannotAddChildTwice() {
        node1.addChild(child1);
        assertSame(node1, child1.parent);
        assertEquals(1, node1.numChildren());

        node1.addChild(child1);
        assertSame(node1, child1.parent);
        assertEquals(1, node1.numChildren());
    }

    @Test
    void cannotAddChildToDifferentParents() {
        node1.addChild(child1);
        node2.addChild(child1);

        assertSame(node1, child1.parent);
        assertEquals(1, node1.numChildren());
        assertEquals(0, node2.numChildren());
    }

    // TODO do not add node to descendant

    @Test
    void removingChildClearsParent() {
        node1.addChild(child1);
        assertSame(node1, child1.getParent());

        node1.removeChild(child1);
        assertNull(child1.getParent());
    }

    @Test
    void removingChildChangesNumChildren() {
        node1.addChild(child1);
        node1.addChild(child2);
        assertEquals(2, node1.numChildren());

        node1.removeChild(child1);
        assertEquals(1, node1.numChildren());

        node1.removeChild(child2);
        assertEquals(0, node1.numChildren());
    }

    @Test
    void cannotRemoveNullChild() {
        node1.addChild(child1);
        assertEquals(1, node1.numChildren());

        node1.removeChild(null);
        assertEquals(1, node1.numChildren());
    }

    // Get Child

    @Test
    void getChildByName() {
        node1.addChild(child1);
        node1.addChild(child2);

        assertSame(child1, node1.getChild("Child Node 1"));
        assertSame(child2, node1.getChild("Child Node 2"));

        assertNull(node1.getChild("Child Node 3"));
        assertNull(node1.getChild((String) null));
    }

    @Test
    void getChildrenWithSuperclass() {
        node1.addChild(child1);
        node1.addChild(child2);
        node1.addChild(child3);

        assertSame(child1, node1.getChild(NodeA.class));
        assertEquals(
                List.of(child1, child2, child3),
                node1.getChildren(NodeA.class)
        );

        assertNull(node1.getChild(NodeC.class));
        assertTrue(node1.getChildren(NodeC.class).isEmpty());
    }

    @Test
    void getChildrenWithSameClass() {
        node1.addChild(child1);
        node1.addChild(child2);
        node1.addChild(child3);

        assertSame(child2, node1.getChild(NodeB.class));
        assertEquals(
                List.of(child2, child3),
                node1.getChildren(NodeB.class)
        );

        assertNull(node1.getChild(NodeC.class));
        assertTrue(node1.getChildren(NodeC.class).isEmpty());
    }

    @Test
    void getChildrenWithNullClassIsNone() {
        node1.addChild(child1);
        node1.addChild(child2);

        assertNull(node1.getChild((Class<? extends Node>) null));
        assertTrue(node1.getChildren(null).isEmpty());
    }

    // Node Scene Depth

    @Test
    void nodeGetSceneDepthCorrect() {
        scene.addNode(node1);
        node1.addChild(child1);
        child1.addChild(child2);

        assertEquals(1, node1.getSceneDepth());
        assertEquals(2, child1.getSceneDepth());
        assertEquals(3, child2.getSceneDepth());

        assertEquals(0, node2.getSceneDepth());
    }

    @Test
    void nodeIsTopLevelCorrect() {
        scene.addNode(node1);
        node1.addChild(child1);
        child1.addChild(child2);

        assertTrue(node1.isTopLevel());
        assertFalse(child1.isTopLevel());
        assertFalse(child2.isTopLevel());

        assertFalse(node2.isTopLevel());
    }

    // Node Enabled/Visible

    @Test
    void nodeShouldUpdateAndRenderCorrect() {
        node1.addChild(child1);
        child1.addChild(child2);
        child1.addChild(child3);
        setEnabledAndVisible(child3, false);

        assertTrue(shouldUpdateAndRender(node1));
        assertTrue(shouldUpdateAndRender(child1));
        assertTrue(shouldUpdateAndRender(child2));
        assertFalse(shouldUpdateAndRender(child3));
    }

    @Test
    void nodeShouldNotUpdateAndRenderCorrect() {
        node1.addChild(child1);
        child1.addChild(child2);
        child1.addChild(child3);
        setEnabledAndVisible(child1, false);
        setEnabledAndVisible(child3, false);

        assertTrue(shouldUpdateAndRender(node1));
        assertFalse(shouldUpdateAndRender(child1));
        assertFalse(shouldUpdateAndRender(child2));
        assertFalse(shouldUpdateAndRender(child3));
    }

    // Helper Methods

    private static void setEnabledAndVisible(Node node, boolean on) {
        node.setEnabled(on);
        node.setVisible(on);
    }

    private static boolean shouldUpdateAndRender(Node node) {
        return node.shouldUpdate() && node.shouldRender();
    }

}