package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.SceneLayer} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneLayerTest {

    private SceneLayer layer0, layer1;

    @BeforeEach
    void setUp() {
        layer0 = new SceneLayer(0);
        layer1 = new SceneLayer(1);
    }

    @Test
    void canInteractWithNullLayer() {
        assertTrue(layer0.canInteract(null));
    }

    @Test
    void doesInteractWithSelf() {
        layer0.setLayerInteract(0, true);
        assertTrue(layer0.canInteract(layer0));
    }

    @Test
    void doesNotInteractWithSelf() {
        layer0.setLayerInteract(0, false);
        assertFalse(layer0.canInteract(layer0));
    }

    @Test
    void doesBothInteractWithOther() {
        layer0.setLayerInteract(1, true);
        layer1.setLayerInteract(0, true);

        assertTrue(layer0.canInteract(layer1));
        assertTrue(layer1.canInteract(layer0));
    }

    @Test
    void doesOneInteractWithOther() {
        layer0.setLayerInteract(1, false);
        layer1.setLayerInteract(0, true);

        assertTrue(layer0.canInteract(layer1));
        assertTrue(layer1.canInteract(layer0));
    }

    @Test
    void doesNotInteractWithOther() {
        layer0.setLayerInteract(1, false);
        layer1.setLayerInteract(0, false);

        assertFalse(layer0.canInteract(layer1));
        assertFalse(layer1.canInteract(layer0));
    }

}