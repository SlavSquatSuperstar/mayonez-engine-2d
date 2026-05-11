package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Scene} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneTest {

    private Scene scene1, scene2;
    private GameObject obj1, obj2;

    @BeforeEach
    void getScenes() {
        scene1 = new Scene("Test Scene") {
        };
        scene2 = new Scene("Test Scene") {
        };
    }

    @BeforeEach
    void getObjects() {
        obj1 = new GameObject("Test Object 1");
        obj2 = new GameObject("Test Object 2");
    }

    @Test
    void sceneIDsAreUnique() {
        assertNotEquals(scene1.sceneID, scene2.sceneID);
        assertNotEquals(scene1, scene2);
    }

    @Test
    void addObjectChangesNumObjects() {
        assertEquals(0, scene1.numObjects());

        scene1.addObject(obj1);
        assertEquals(1, scene1.numObjects());

        scene1.addObject(obj2);
        assertEquals(2, scene1.numObjects());
    }

    @Test
    void cannotAddNullObject() {
        scene1.addObject(null);
        assertEquals(0, scene1.numObjects());
    }

    @Test
    void cannotAddObjectTwice() {
        scene1.addObject(obj1);
        scene1.addObject(obj1);

        assertSame(scene1, obj1.getScene());
        assertEquals(1, scene1.numObjects());
    }

    @Test
    void cannotAddObjectToDifferentScenes() {
        scene1.addObject(obj1);
        scene2.addObject(obj1);

        assertSame(scene1, obj1.getScene());
        assertEquals(1, scene1.numObjects());
        assertEquals(0, scene2.numObjects());
    }

    @Test
    void getObjectByNameSuccess() {
        assertNull(scene1.getObject("Test Object 1"));

        scene1.addObject(obj1);
        scene1.addObject(obj2);

        assertSame(obj1, scene1.getObject("Test Object 1"));
        assertSame(obj2, scene1.getObject("Test Object 2"));
        assertNull(scene1.getObject("Test Object 3"));
    }

    @Test
    void getObjectWithNullNameSuccess() {
        var obj = new GameObject(null);
        scene1.addObject(obj);

        assertSame(obj, scene1.getObject(null));
        assertSame(obj, scene1.getObject("null"));
    }

}
