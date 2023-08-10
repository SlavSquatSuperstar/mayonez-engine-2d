package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Scene} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneTest {

    @Test
    void sceneIDsAreUnique() {
        var scene1 = new Scene("Test Scene") {
        };
        var scene2 = new Scene("Test Scene") {
        };
        assertNotEquals(scene1.sceneID, scene2.sceneID);
    }

    @Test
    void addObjectChangesObjectCount() {
        var scene = new Scene("Test Scene") {
        };

        var obj1 = new GameObject("");
        scene.addObject(obj1);

        var obj2 = new GameObject("");
        scene.addObject(obj2);

        assertEquals(2, scene.numObjects());
    }

    @Test
    void getObjectByNameSuccess() {
        var scene = new Scene("Test Scene") {
        };
        assertNull(scene.getObject("Test Object 1"));

        var obj1 = new GameObject("Test Object 1");
        scene.addObject(obj1);

        var obj2 = new GameObject("Test Object 2");
        scene.addObject(obj2);

        assertSame(obj1, scene.getObject("Test Object 1"));
        assertNull(scene.getObject("Test Object"));
    }

}
