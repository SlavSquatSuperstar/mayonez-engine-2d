package mayonez;

import mayonez.util.*;

/**
 * A grouping of game objects inside a scene with common properties.
 * Layers may be given a {@link mayonez.util.Bitmask}, which defines which layers
 * they can interact with.
 *
 * @author SlavSquatSuperstar
 */
public class SceneLayer {

    public static final int NUM_LAYERS = Bitmask.NUM_BITS;
    private final int index;
    private String name;
    private final Bitmask mask;

    /**
     * Constructs a scene layer with the index that interacts with all other layers.
     *
     * @param index the layer index
     */
    SceneLayer(int index) {
        this.index = index;
        this.name = "Layer %d".formatted(index);
        mask = new Bitmask();
    }

    // Layer Properties

    /**
     * The layer's index within the scene.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * The name of the layer, by default "Layer (index)".
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the layer. Names can be used to describe the layer's function.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    // Mask Methods

    /**
     * Whether this layer should interact with another. Two layers interact if at least one
     * has set the other to "true" in its layer mask, or if at least one is null.
     *
     * @param other the other layer
     * @return if the layers interact
     */
    public boolean canInteract(SceneLayer other) {
        if (other == null) return true;
        return this.getLayerInteract(other.index) || other.getLayerInteract(this.index);
    }

    public boolean getLayerInteract(int layer) {
        return mask.getBit(layer);
    }

    public void setLayerInteract(int layer, boolean interact) {
        mask.setBit(layer, interact);
    }

    public void setLayerMask(int value) {
        mask.setValue(value);
    }

    @Override
    public String toString() {
        return "SceneLayer (%s)".formatted(name);
    }

}
