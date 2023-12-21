package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Scene} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneTest {

    private Scene scene;

    @BeforeEach
    void getScene() {
        scene = new Scene("Test Scene") {
        };
    }

    @Test
    void sceneIDsAreUnique() {
        var scene2 = new Scene("Test Scene") {
        };
        assertNotEquals(scene.sceneID, scene2.sceneID);
    }

    @Test
    void addObjectChangesNumObjects() {
        var obj1 = new GameObject("");
        scene.addObject(obj1);

        var obj2 = new GameObject("");
        scene.addObject(obj2);

        assertEquals(2, scene.numObjects());
    }

    @Test
    void cannotAddObjectTwice() {
        var obj = new GameObject("");
        scene.addObject(obj);
        scene.addObject(obj);

        assertEquals(1, scene.numObjects());
    }

    @Test
    void cannotAddObjectToDifferentScenes() {
        var scene2 = new Scene("Test Scene") {
        };
        var obj = new GameObject("");

        scene2.addObject(obj);
        scene.addObject(obj);

        assertEquals(0, scene.numObjects());
    }

    @Test
    void getObjectByNameSuccess() {
        assertNull(scene.getObject("Test Object 1"));

        var obj1 = new GameObject("Test Object 1");
        scene.addObject(obj1);

        var obj2 = new GameObject("Test Object 2");
        scene.addObject(obj2);

        assertSame(obj1, scene.getObject("Test Object 1"));
        assertNull(scene.getObject("Test Object"));
    }

}
