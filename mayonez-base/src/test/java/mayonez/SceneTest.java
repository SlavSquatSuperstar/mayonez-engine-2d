package mayonez;

import org.junit.jupiter.api.*;

import static mayonez.ECSTestUtils.TestScene;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Scene} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneTest {

    private Scene scene1, scene2;

    @BeforeEach
    void setUp() {
        scene1 = new TestScene("Test Scene");
        scene2 = new TestScene("Test Scene");
        scene1.createRootNode();
        scene2.createRootNode();
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

        scene1.uniqueObjectNames = true;
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

        scene1.uniqueObjectNames = true;
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

        scene1.uniqueObjectNames = true;
        scene1.addObject(obj1);
        scene1.addObject(obj2);
        scene1.addObject(obj3);
        scene1.addObject(obj4);

        assertEquals("Test Object", obj1.getName());
        assertEquals("Test Object (1)", obj2.getName());
        assertEquals("Test Object (3)", obj3.getName());
        assertEquals("Test Object (2)", obj4.getName());
    }

}
