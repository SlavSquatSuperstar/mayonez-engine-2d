package mayonez;

import mayonez.util.StringUtils;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An object possessing properties and behaviors that belongs within a scene. Each node
 * has a name and transform and has update methods. Nodes are structured in a tree,
 * and nodes can be created and destroyed in a scene as needed.
 * <p>
 * Generally, a pure entity-component-system (ECS) architecture favors composition over
 * inheritance, allowing reuse while keeping classes simple and modular. Under pure ECS,
 * entities are a single ID and only components provide data and functionality. Such
 * implementations have high performance but lead to more boilerplate.
 * <p>
 * In the Unity Engine, {@code GameObjects} are entities that store a transform, {@code Components},
 * and user defined scripts. {@code GameObjects} may also be nested arbitrarily under each other
 * inside each other or saved to instantiable prefabs.
 * <p>
 * Meanwhile, in the Godot Engine, there is a single {@code Node} class that serves as both entity
 * and component and may be extended with a script. Any tree of nodes is considered a scene and may
 * be saved to a packed scene file for reuse. {@code Nodes} provide greater scene readability and
 * organization at the cost of making inheritance trees larger.
 * <p>
 * Mayonez Engine uses a one {@code Node} class, similar to Godot, while still keeping the
 * {@link Scene} class.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Node {

    private static long nodeCounter = 0L; // Node UUID

    // Node Information
    final long nodeID;
    private String name;
    private final Set<String> tags;

    // Node Hierarchy
    @Nullable Scene scene;

    // Node State
    private boolean destroyed;
    private boolean enabled, visible;

    /**
     * Create an empty node with name defaulting to the class name.
     */
    public Node() {
        this(null);
    }

    /**
     * Create an empty node with a name. If the name is {@code null} or blank,
     * it will default to the class name.
     *
     * @param name the node name
     */
    public Node(@Nullable String name) {
        nodeID = nodeCounter++;
        this.name = validateName(name);
        tags = new HashSet<>();

        scene = null;

        destroyed = false;
        enabled = true;
        visible = true;
    }

    // Node Information Getters and Setters

    /**
     * Get this node's name, which is non-null and does not need to be unique.
     *
     * @return the node's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set this node's name, which does not need to be unique. If the parameter is null
     * or blank, then the name will be set to the node's class name.
     *
     * @param name the node's name
     */
    public void setName(@Nullable String name) {
        this.name = validateName(name);
        // TODO notify parent object
    }

    String validateName(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return StringUtils.getObjectClassName(this);
        } else {
            return name;
        }
    }

    /**
     * Get a copy of the set of tags present in this node.
     *
     * @return the set of tags
     */
    public Set<String> getTags() {
        return Set.copyOf(tags);
    }

    /**
     * Check if a tag is present in this node.
     *
     * @param tag the tag
     * @return if the tag is present
     */
    public boolean hasTag(@Nullable String tag) {
        return tags.contains(tag);
    }

    /**
     * Add a tag to this node. If the tag is a duplicate or null, it will not be added.
     *
     * @param tag the tag
     */
    public void addTag(@Nullable String tag) {
        if (tag != null) tags.add(tag);
    }

    /**
     * Remove a tag from this node, if the tag is present.
     *
     * @param tag the tag
     */
    public void removeTag(@Nullable String tag) {
        tags.remove(tag);
    }

    /**
     * Remove all tags from this node.
     */
    public void clearTags() {
        tags.clear();
    }

    // Node Hierarchy Getters and Setters

    /**
     * Get the {@link mayonez.Scene} that contains this node. The parent scene
     * will be non-null from the start of {@code init} to the end of {@code destroy}.
     *
     * @return the parent scene
     */
    public @Nullable Scene getScene() {
        return scene;
    }

    /**
     * Add this node to a parent {@link mayonez.Scene}.
     *
     * @param scene a scene
     */
    void setScene(Scene scene) {
        this.scene = scene;
    }

    // Node State Getters and Setters

    /**
     * Whether this node has been removed from the scene tree.
     *
     * @return if the node is destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Remove this object its parent and destroy all its children. The {@link #getScene}
     * method will return after the object is destroyed.
     * <p>
     * <b>Warning:</b> Destroying a node is permanent and cannot be reversed!
     */
    public void setDestroyed() {
        this.destroyed = true;
    }

    /**
     * Get whether this node and all its children should be updated. If any ancestor
     * node is disabled, then this node will not be enabled.
     *
     * @return if this node is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set whether this node should be updated. Will not affect whether the parent
     * node is enabled.
     *
     * @param enabled if the node is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Get whether this node and all its children should be rendered. If any ancestor
     * node is invisible, then this node will not be visible.
     *
     * @return if this node is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set whether this node should be rendered. Will not affect whether the parent
     * node is visible.
     *
     * @param visible if the node is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // Object Overrides

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Node n) && (n.nodeID == this.nodeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeID, name);
    }

    @Override
    public String toString() {
        return String.format("%s [%d]", name, nodeID);
    }

}
