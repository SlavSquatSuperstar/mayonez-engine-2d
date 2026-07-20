package mayonez;

import mayonez.util.BufferedList;
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
    @Nullable Node parent;
    private final BufferedList<Node> children;

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
        parent = null;
        children = new BufferedList<>();

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
     * Get the {@link mayonez.Scene} that contains this node. The scene
     * will be non-null from the start of {@code init} to the end of {@link onDestroy}.
     *
     * @return the parent scene
     */
    public @Nullable Scene getScene() {
        return scene;
    }

    /**
     * Add this node to a {@link mayonez.Scene} or remove it from one.
     *
     * @param scene a scene
     */
    void setScene(@Nullable Scene scene) {
        this.scene = scene;
    }

    /**
     * Get the parent node this node belongs to.
     *
     * @return the parent node
     */
    public @Nullable Node getParent() {
        return parent;
    }

    /**
     * Add this node to a parent node or remove it from one.
     *
     * @param parent the parent node
     */
    void setParent(@Nullable Node parent) {
        this.parent = parent;
    }

    /**
     * Whether this node is at the top level of the scene hierarchy, i.e., it belongs to a scene
     * and has no parent.
     *
     * @return if this node is top-level
     */
    public boolean isTopLevel() {
        return parent == null && scene != null;
    }

    /**
     * Get the depth of this node in the scene tree, or the number of ancestors, including the scene
     * itself. If the node is not part of a scene, then the depth is zero. If {@link #isTopLevel} is
     * true, then the depth is one.
     *
     * @return the scene depth
     */
    public int getSceneDepth() {
        if (scene == null) return 0;
        else if (parent == null) return 1;
        else return 1 + parent.getSceneDepth();
    }

    /**
     * Find the first child node with the specified name (case-sensitive), or null if none exists.
     *
     * @param name the node's name
     * @return the node, or null if not present
     */
    public @Nullable Node getChild(@Nullable String name) {
        if (name == null) return null;
        return children.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find the first child node belonging to the specified class or any of its subclasses,
     * or null if none exists.
     *
     * @param cls the node's type
     * @param <T> the node type
     * @return the node, or null if not present
     */
    public <T extends Node> @Nullable T getChild(@Nullable Class<T> cls) {
        if (cls == null) return null;
        return children.stream()
                .filter(cls::isInstance)
                .map(cls::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * Find the first child node belonging to the specified class or any of its subclasses,
     * or empty if none exists.
     *
     * @param cls the node's type
     * @param <T> the node type
     * @return the node, or empty if not present
     */
    public <T extends Node> List<T> getChildren(@Nullable Class<T> cls) {
        if (cls == null) return List.of();
        return children.stream()
                .filter(cls::isInstance)
                .map(cls::cast)
                .toList();
    }

    /**
     * Get a copy of the list of all this object's child nodes.
     *
     * @return the list of nodes
     */
    public List<Node> getChildren() {
        return List.copyOf(children);
    }

    /**
     * Count how many child nodes this node has.
     *
     * @return the number of nodes
     */
    public int numChildren() {
        return children.size();
    }

    /**
     * Add a child node to this node. The child will not be added if it is
     * null, already has a parent node, or is part of a scene.
     *
     * @param child the node
     */
    public void addChild(@Nullable Node child) {
        if (child == null || child.parent != null || child.scene != null) return;

        child.setParent(this);
        child.setScene(scene);
        if (scene != null && scene.isRunning()) {
            children.addBuffered(child); // Add child later if scene running
        } else {
            children.addUnbuffered(child); // Add child now otherwise
        }
    }

    // TODO rename child

    /**
     * Removes a child component from this node and destroys it. The child will
     * only be removed it if is not null and its parent is this node.
     *
     * @param child the component
     */
    public final void removeChild(@Nullable Node child) {
        if (child == null || child.parent != this) return;

        child.setDestroyed();
        child.setParent(null);
        child.setScene(null);
        if (scene != null && scene.isRunning()) {
            children.removeBuffered((child)); // Remove component later if scene running
        } else {
            children.removeUnbuffered(child); // Remove component now
        }
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
     * Remove this object its parent and destroy all its descendants. The properties
     * {@link #getScene}, {@link #getParent}, and {@code transform} will return null
     * after the object is destroyed.
     * <p>
     * <b>Warning:</b> Destroying a node is permanent and cannot be reversed!
     */
    public void setDestroyed() {
        if (destroyed) return;
        destroyed = true;
//        onDestroy(); // TODO can't place here
        // TODO destroy children?
        // TODO clear children
    }

    /**
     * Custom behavior for when this node or any of its ancestors is destroyed. The properties
     * {@link #getScene}, {@link #getParent}, and {@code transform} will still be accessible.
     * <p>
     * Warning: Calling {@code onDestroy} directly can lead to unpredictable behavior. It is
     * better to call {@link #setDestroyed()} instead.
     */
    protected void onDestroy() {
    }

    /**
     * Whether this node and all its children should be updated. If any ancestor
     * node is disabled, then this node will not be updated regardless.
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
        if (enabled) onEnable();
        else onDisable();
    }

    /**
     * Custom user behavior for when this script is enabled.
     * <p>
     * Warning: Calling {@code onEnable()} directly can lead to unpredictable behavior.
     * It is better to call {@code setEnabled(true)} instead.
     */
    protected void onEnable() {
    }

    /**
     * Custom user behavior for when this script is disabled.
     * <p>
     * Warning: Calling {@code onEnable()} directly can lead to unpredictable behavior.
     * It is better to call {@code setEnabled(false)} instead.
     */
    protected void onDisable() {
    }

    /**
     * Whether this node should update, meaning it and all of its ancestors are enabled.
     *
     * @return if the node should update
     */
    public boolean shouldUpdate() {
        if (parent == null) return enabled;
        else return enabled && parent.shouldUpdate();
    }

    /**
     * Whether this node and all its children should be rendered. If any ancestor
     * node is invisible, then this node will not be rendered regardless.
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

    /**
     * Whether this node should render, meaning it and all of its ancestors are visible.
     *
     * @return if the node should render
     */
    public boolean shouldRender() {
        if (parent == null) return visible;
        else return visible && parent.shouldRender();
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
