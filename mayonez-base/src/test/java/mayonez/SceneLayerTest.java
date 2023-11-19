package mayonez;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link mayonez.SceneLayer} class.
 *
 * @author SlavSquatSuperstar
 */
class SceneLayerTest {

    @Test
    void canInteractWithNullLayer() {
        var layer = new SceneLayer(0);
        assertTrue(layer.canInteract(null));
    }

    @Test
    void doesInteractWithSelf() {
        var layer = new SceneLayer(0);
        layer.setLayerInteract(0, true);
        assertTrue(layer.canInteract(layer));
    }

    @Test
    void doesNotInteractWithSelf() {
        var layer = new SceneLayer(0);
        layer.setLayerInteract(0, false);
        assertFalse(layer.canInteract(layer));
    }

    @Test
    void doesInteractWithOther() {
        var layer0 = new SceneLayer(0);
        var layer1 = new SceneLayer(1);

        layer0.setLayerInteract(1, true);
        layer1.setLayerInteract(0, true);

        assertTrue(layer0.canInteract(layer1));
        assertTrue(layer1.canInteract(layer0));
    }

    @Test
    void doesNotInteractWithOther() {
        var layer0 = new SceneLayer(0);
        var layer1 = new SceneLayer(1);

        layer0.setLayerInteract(1, false);
        layer1.setLayerInteract(0, false);

        assertFalse(layer0.canInteract(layer1));
        assertFalse(layer1.canInteract(layer0));
    }

}