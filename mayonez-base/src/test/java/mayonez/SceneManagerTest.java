package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.SceneManager} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneManagerTest {

    private Scene scene1, scene2;

    @BeforeEach
    void getScenes() {
        SceneManager.clearScenes();
        scene1 = new Scene("Test Scene 1") {
        };
        scene2 = new Scene("Test Scene 2") {
        };
    }

    @Test
    void addSceneChangesNumScenes() {
        assertEquals(0, SceneManager.numScenes());

        SceneManager.addScene(scene1);
        assertEquals(1, SceneManager.numScenes());

        SceneManager.addScene(scene2);
        assertEquals(2, SceneManager.numScenes());
    }

    @Test
    void cannotAddNullScene() {
        SceneManager.addScene(null);
        assertEquals(0, SceneManager.numScenes());
    }

    @Test
    void getSceneByNameSuccess() {
        SceneManager.addScene(scene1);
        SceneManager.addScene(scene2);

        assertSame(scene1, SceneManager.getScene("Test Scene 1"));
        assertSame(scene2, SceneManager.getScene("Test Scene 2"));
        assertNull(SceneManager.getScene("Test Scene 3"));
    }

    @Test
    void getSceneByIndexSuccess() {
        SceneManager.addScene(scene1);
        SceneManager.addScene(scene2);

        assertSame(scene1, SceneManager.getScene(0));
        assertSame(scene2, SceneManager.getScene(1));
        assertNull(SceneManager.getScene(2));
    }

    @Test
    void getSceneWithNullNameSuccess() {
        var scene = new Scene(null) {
        };
        SceneManager.addScene(scene);

        assertSame(scene, SceneManager.getScene(null));
        assertSame(scene, SceneManager.getScene("null"));
    }

    @Test
    void addSceneSameNameReplacedExisting() {
        var scene1 = new Scene("Test Scene") {
        };
        var scene2 = new Scene("Test Scene") {
        };

        SceneManager.addScene(scene1);
        assertEquals(1, SceneManager.numScenes());
        assertSame(scene1, SceneManager.getScene("Test Scene"));
        assertSame(scene1, SceneManager.getScene(0));

        SceneManager.addScene(scene2);
        assertEquals(1, SceneManager.numScenes());
        assertSame(scene2, SceneManager.getScene("Test Scene"));
        assertSame(scene2, SceneManager.getScene(0));
    }

}
