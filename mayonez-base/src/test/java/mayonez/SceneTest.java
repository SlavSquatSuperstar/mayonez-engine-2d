package mayonez;

import org.junit.jupiter.api.*;

import java.util.List;

import static mayonez.ECSTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Scene} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneTest {

    private static final String TAG1 = "Tag 1", TAG2 = "Tag 2";

    private Scene scene1, scene2;
    private Node node1, node2, node3, node4, node5, node6;

    @BeforeEach
    void setUp() {
        scene1 = new TestScene("Test Scene");
        scene2 = new TestScene("Test Scene");
        scene1.createRootNode();
        scene2.createRootNode();
        scene1.setUniqueNodeNames(true);

        node1 = new NodeA("Node 1");
        node1.addTag(TAG1);
        node1.addTag(TAG2);
        node2 = new NodeA("Node 2");
        node3 = new NodeA("Node 3");
        node3.addTag(TAG1);
        node4 = new NodeA("Node 4");
        node4.addTag(TAG2);
        node5 = new NodeA("Node 5");
        node5.addTag(TAG1);
        node6 = new NodeA("Node 6");
        node6.addTag(TAG2);

        scene1.addNode(node1);
        scene1.addNode(node2);

        node1.addChild(node3);
        node3.addChild(node4);
        node2.addChild(node5);
        node2.addChild(node6);
    }

    @AfterEach
    void tearDown() {
        scene1.stop();
        scene2.stop();
    }

    // Scene ID

    @Test
    void sceneIDsAreUnique() {
        assertNotEquals(scene1.sceneID, scene2.sceneID);
        assertNotEquals(scene1, scene2);
    }

    // Object Renaming

    @Test
    void objectRenamingContiguousSuccess() {
        var obj1 = new GameObject("Test Object");
        var obj2 = new GameObject("Test Object");
        var obj3 = new GameObject("Test Object");

        scene1.addObject(obj1);
        scene1.addObject(obj2);
        scene1.addObject(obj3);

        assertEquals("Test Object", obj1.getName());
        assertEquals("Test Object (1)", obj2.getName());
        assertEquals("Test Object (2)", obj3.getName());
    }

    @Test
    void objectRenamingMultipleContiguousSuccess() {
        var obj1 = new GameObject("Test Object");
        var obj2 = new GameObject("Test Object");
        var obj3 = new GameObject("Test Object");
        var obj4 = new GameObject("Test Object");

        scene1.addObject(obj1);
        scene1.addObject(obj2);
        scene1.addObject(obj3);
        scene1.addObject(obj4);

        assertEquals("Test Object", obj1.getName());
        assertEquals("Test Object (1)", obj2.getName());
        assertEquals("Test Object (2)", obj3.getName());
        assertEquals("Test Object (3)", obj4.getName());
    }

    @Test
    void objectRenamingNonContiguousSuccess() {
        var obj1 = new GameObject("Test Object");
        var obj2 = new GameObject("Test Object (1)");
        var obj3 = new GameObject("Test Object (3)");
        var obj4 = new GameObject("Test Object");

        scene1.addObject(obj1);
        scene1.addObject(obj2);
        scene1.addObject(obj3);
        scene1.addObject(obj4);

        assertEquals("Test Object", obj1.getName());
        assertEquals("Test Object (1)", obj2.getName());
        assertEquals("Test Object (3)", obj3.getName());
        assertEquals("Test Object (2)", obj4.getName());
    }

    // Get Node

    @Test
    void getNodeSuccess() {
        assertSame(node1, scene1.getNode("Node 1"));
        assertSame(node3, scene1.getNode("Node 3"));
        assertSame(node5, scene1.getNode("Node 5"));
    }

    @Test
    void getNodesByTagSuccess() {
        assertEquals(List.of(node1, node3, node5), scene1.getNodes(TAG1));
        assertEquals(List.of(node1, node4, node6), scene1.getNodes(TAG2));
    }

    @Test
    void numNodesSuccess() {
        // 6 nodes plus root
        assertEquals(7, scene1.numNodes());
    }

    @Test
    void getRootNodeSuccess() {
        var root = scene1.getRootNode();
        assertTrue(root.isRoot());
        assertEquals(0, root.getSceneDepth());
    }

}
