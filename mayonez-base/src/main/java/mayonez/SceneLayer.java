package mayonez;

import mayonez.util.*;
import org.jspecify.annotations.Nullable;

/**
 * A grouping of collidable objects inside a scene with common properties.
 * Layers contain a {@link mayonez.util.Bitmask} that defines which layers
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
     * Constructs a scene layer with an index that interacts with all layers.
     *
     * @param index the layer index
     */
    public SceneLayer(int index) {
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
     * Changes the name of the layer to something more human-readable.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    // Mask Methods

    /**
     * Whether this layer should interact with another. Two layers interact if
     * at least one has set the other's index to "true" in its layer mask, or
     * if at least one is null.
     *
     * @param other the other layer
     * @return if the layers interact
     */
    public boolean canInteract(@Nullable SceneLayer other) {
        return (other == null) || this.getLayerInteract(other.index) || other.getLayerInteract(this.index);
    }

    /**
     * Get whether this layer is allowed to interact with another layer. Note
     * that it is possible for the other layer to interact with this one, or
     * a layer to ont interact with itself.
     *
     * @param index the layer's index
     * @return whether this layer interacts with the other
     */
    public boolean getLayerInteract(int index) {
        return mask.getBit(index);
    }

    /**
     * Set whether this layer is allowed to interact with another layer. Note
     * that it is possible for the other layer to interact with this one, or
     * a layer to not interact with itself.
     *
     * @param index    the layer's index
     * @param interact whether this layer interacts with the other
     */
    public void setLayerInteract(int index, boolean interact) {
        mask.setBit(index, interact);
    }

    /**
     * Get the layer's raw bitmask value.
     *
     * @return the 32-bit bitmask value
     */
    public int getMaskValue() {
        return mask.getValue();
    }

    /**
     * Set the layer's raw bitmask value.
     *
     * @param value the 32-bit bitmask value
     */
    public void setMaskValue(int value) {
        mask.setValue(value);
    }

    @Override
    public String toString() {
        return "SceneLayer (%s)".formatted(name);
    }

}
