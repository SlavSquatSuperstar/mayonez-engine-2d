package mayonez;

import mayonez.util.StringUtils;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * An entity possessing properties and behaviors that belongs to a scene.
 * Nodes are structured in a tree, and nodes can be created and destroyed while
 * the scene is initializing or running.
 * <p>
 * Generally, a pure entity-component-system (ECS) architecture favors
 * composition over inheritance, keeping classes simple and modular. Under pure
 * ECS, entities are a single ID and only components can provide data and
 * functionality. Such implementations have higher performance but lead to more
 * boilerplate.
 * <p>
 * In the Unity Engine, {@code GameObjects} are entities that store a transform
 * and multiple {@code Components} and scripts. {@code GameObjects} may also be
 * nested arbitrarily inside each other or saved to instantiable prefabs.
 * <p>
 * Meanwhile, in the Godot Engine, a single {@code Node} class that serves as
 * both entity and component and may be extended with a script. Any tree of
 * nodes is considered a scene and may be saved to a file for reuse.
 * {@code Nodes} provide greater scene readability and organization at the cost
 * of making inheritance trees larger.
 * <p>
 * Mayonez Engine uses a one {@code Node} class for greater flexibility while
 * keeping the {@link Scene} class for global resources.
 * <p>
 * Usage: Nodes can be created by instantiating a subclass of {@code #Node}.
 * A node's transform, parent, and scene can be accessed through
 * {@link #getTransform}, {@link #getParent}, and {@link #getScene}. Usually,
 * Nodes will add child nodes by calling {@link addChild} inside {@link #init}.
 * Further initialization requiring child or sibling nodes can be done inside
 * {@link #start}. User behavior can be defined in {@link #update},
 * {@link #fixedUpdate}, or {@link #debugRender}. To remove a node and its tree
 * from the scene, call {@link #removeChild} from the parent or
 * {@link #setDestroyed} through the target node.
 * <p>
 * See {@link mayonez.Scene} for more information.
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
    private final List<Node> children; // Don't need to buffer since scene updates nodes
    private boolean childrenChanged; // Garbage collect destroyed children
    private Transform transform;
    private final Transform originalTransform;
    private boolean useParentTransform;

    // Node Behavior
    private boolean destroyed, enabled, visible;
    private int updateOrder, zIndex;

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
        this(name, new Transform());
    }

    /**
     * Create an empty node with a name and transform. If the name is
     * {@code null} or blank, it will default to the class name.
     *
     * @param name the node name
     */
    public Node(@Nullable String name, @Nullable Transform transform) {
        nodeID = nodeCounter++;
        this.name = validateName(name);
        tags = new HashSet<>();

        scene = null;
        parent = null;
        children = new ArrayList<>();
        childrenChanged = false;
        originalTransform = transform != null ? transform : new Transform();
        this.transform = originalTransform;
        useParentTransform = false;

        destroyed = false;
        enabled = true;
        visible = true;
        updateOrder = 0;
        zIndex = 0;
    }

    // Node Game Loop Methods

    /**
     * Add child components and initializes fields after this node has been
     * added to the scene or parent node. This method is called before
     * {@link #start} and after {@code parent.init}. The {@link #getTransform},
     * {@link #getParent}, and {@link #getScene} properties will return
     * non-null here. Subclasses may override this method and can also call
     * {@code super.init()}.
     * <p>
     * Warning: Calling {@code init()} at any other point in time may lead to
     * unintended errors and should be avoided!
     */
    protected void init() {
    }

    /**
     * Initialize fields after all components have been added to the parent
     * node. The {@link #getTransform}, {@link #getParent} {@link #getScene}
     * properties and {@link #getChild}/{@link #getSibling} methods are
     * accessible here. This method will be called even if this node has been
     * disabled through {@link #setEnabled}.
     * <p>
     * Usage: Subclasses may override this method and can also call
     * {@code super.start()}.
     * <p>
     * Warning: Calling {@code start()} at any other point in time may lead to
     * unintended errors and should be avoided!
     */
    protected void start() {
    }

    /**
     * Refresh this node's state and game logic. This method is called each
     * fixed tick, between physics and {@link #update}, and {@code dt} is
     * generally consistent. The {@code fixedUpdate} method should be used for
     * frame rate-sensitive behavior, such as movement, collision, and AI.
     * <p>
     * Usage: Subclasses may override this method and can also call
     * {@code super.fixedUpdate()}.
     *
     * @param dt seconds between fixed ticks
     */
    protected void fixedUpdate(float dt) {
    }

    /**
     * Refresh this node's state and game logic. This method is called each
     * drawn frame, between {@link #fixedUpdate} and rendering, and {@code dt}
     * may vary. The {@code update} method may be used for general behavior,
     * such as input, timers, and animations.
     * <p>
     * Usage: Subclasses may override this method and can also call
     * {@code super.update()}.
     *
     * @param dt seconds since the last frame
     */
    protected void update(float dt) {
    }

    /**
     * Draw debug information for this node to the screen. This method is
     * called each drawn frame, between {@link update} and rendering. Any calls
     * to {@link mayonez.graphics.debug.DebugDraw} should be made here. This
     * method is called even if the scene is paused or the component is not
     * enabled.
     * <p>
     * Usage: Subclasses may override this method and can also call
     * {@code super.debugRender()}.
     */
    protected void debugRender() {
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
     * Set this node's name, which does not need to be unique. If the parameter
     * is null or blank, then the name will be set to the node's class name.
     *
     * @param name the node's name
     */
    public void setName(@Nullable String name) {
        this.name = validateName(name);
        if (parent != null) parent.renameChildUnique(this);
    }

    String validateName(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return StringUtils.getObjectClassName(this);
        } else {
            return name;
        }
    }

    void renameChildUnique(Node child) {
        // Rename node to "Name (n)"
        // If sequence numbers are not contiguous, then choose the least free number
        // Make sure not to count the node being added
        var sameNames = getChildren().stream()
                .filter(o -> !o.equals(child)
                        && o.getName().startsWith(child.getName()))
                .map(Node::getName)
                .toList();
        var newName = child.getName();
        var count = 0;
        while (sameNames.contains(newName)) {
            count++;
            newName = "%s (%d)".formatted(child.getName(), count);
        }
        child.name = newName; // Don't use setter
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
     * Add a tag to this node. If the tag is a duplicate or null, it will not
     * be added.
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
     * Get the {@link mayonez.Scene} that contains this node. The scene will be
     * non-null from the start of {@link init} to the end of {@link onDestroy}.
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
        if (useParentTransform && parent != null) {
            this.transform = parent.transform;
        } else {
            this.transform = originalTransform;
        }
    }

    /**
     * Whether this is the root of the scene hierarchy, i.e., it belongs to a
     * scene and has no parent.
     *
     * @return if this node is top-level
     */
    public boolean isRoot() {
        return scene != null && parent == null;
    }

    /**
     * Whether this node is at the top level of the scene hierarchy, i.e., it
     * belongs to a scene and its parent is the scene root.
     *
     * @return if this node is top-level
     */
    public boolean isTopLevel() {
        return parent != null && parent.isRoot();
    }

    /**
     * Get the depth of this node in the scene tree, or the number of
     * ancestors, including the scene root. If the node is not part of a scene,
     * or it is the scene root, then the depth is zero. If {@link #isTopLevel}
     * is true, then the depth is one.
     *
     * @return the scene depth
     */
    public int getSceneDepth() {
        if (scene == null || parent == null) return 0;
        else return 1 + parent.getSceneDepth();
    }

    /**
     * Find the first child node with the specified name (case-sensitive), or
     * null if none exists.
     *
     * @param name the child node's name
     * @return the child node, or null if not present
     */
    public @Nullable Node getChild(@Nullable String name) {
        if (name == null) return null;
        return children.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find the first child node belonging to the specified class or any of its
     * subclasses, or null if none exists.
     *
     * @param cls the child node's class
     * @param <T> the child node type
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
     * Find all child nodes belonging to the specified class or any of its
     * subclasses, or empty if none exists.
     *
     * @param cls the child node's type
     * @param <T> the child node type
     * @return the list of nodes, or empty if not present
     */
    public <T extends Node> List<T> getChildren(@Nullable Class<T> cls) {
        if (cls == null) return List.of();
        return children.stream()
                .filter(cls::isInstance)
                .map(cls::cast)
                .toList();
    }

    /**
     * Get a copy of the list of all this node's child nodes.
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
     * null, already has a parent node, or is already part of a scene.
     *
     * @param child the node
     */
    public void addChild(@Nullable Node child) {
        if (child == null || child.parent != null || child.scene != null) return;

        child.setParent(this);
        child.setScene(scene);
        children.add(child);
        if (scene != null) {
            if (scene.hasUniqueNodeNames()) renameChildUnique(child);
            scene.onNodeAdded(child);
        }
    }

    /**
     * Removes a child component from this node and destroys it. The child will
     * only be removed it if is not null and its parent is this node.
     *
     * @param child the component
     */
    public void removeChild(@Nullable Node child) {
        if (child == null || child.parent != this) return;

        children.remove(child);
        child.setDestroyed();
        if (scene == null) child.setParent(null);
    }

    void setChildrenChanged() {
        this.childrenChanged = true;
    }

    void garbageCollect() {
        if (childrenChanged) {
            var destroyedChildren = children.stream()
                    .filter(Node::isDestroyed)
                    .toList();
            children.removeAll(destroyedChildren);
            childrenChanged = false;
        }
    }

    /**
     * Find the first sibling node with the specified name (case-sensitive), or
     * null if none exists or the parent is null.
     *
     * @param name the sibling node's name
     * @return the sibling node, or null if not present
     */
    public @Nullable Node getSibling(String name) {
        if (parent == null) return null;
        else return parent.getChild(name);
    }

    /**
     * Find the first sibling node belonging to the specified class or any of
     * its subclasses, or null if none exists or the parent is null.
     *
     * @param cls the sibling node's class
     * @param <T> the sibling node's type
     * @return the sibling node, or null if not present
     */
    public <T extends Node> @Nullable T getSibling(@Nullable Class<T> cls) {
        if (parent == null) return null;
        else return parent.getChild(cls);
    }

    /**
     * The node's {@link Transform} that defines its position, rotation, and
     * scale relative to its parent, or the world if no parent exists.
     *
     * @return the transform
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Set the node's {@link Transform} to the value of the given transform.
     * Note that this Node's transform will not point to the parent's
     * transform.
     *
     * @param transform the transform
     */
    public void setTransform(@Nullable Transform transform) {
        if (transform != null) this.transform.set(transform);
    }

    /**
     * Set whether this Node's {@link Transform} should point to the parent
     * node's transform. If {@code true}, then modifying this node's transform
     * will modify the parent's transform. All subclasses of {@link Component}
     * have this property set to {@code true}.
     *
     * @param useParentTransform whether to reference the parent transform
     * @deprecated This method is a leftover from the old GameObject-Component
     * architecture, where Components referenced their GameObject's transforms.
     * The Node whose transform is being updated should be the parent instead.
     */
    @Deprecated
    protected void setUseParentTransform(boolean useParentTransform) {
        this.useParentTransform = useParentTransform;
        if (useParentTransform && parent != null) {
            this.transform = parent.transform;
        } else {
            this.transform = originalTransform;
        }
    }

    /**
     * The node's global transform inside the scene, equal to this node's
     * transform right-concatenated with all ancestor node transforms until the
     * root ancestor. Note that the return value is read-only, and modifying it
     * will not change any ancestor transforms.
     *
     * @return the global transform
     */
    public Transform getGlobalTransform() {
        if (parent == null) return transform;
        else if (useParentTransform) return parent.getGlobalTransform();
        else return parent.getGlobalTransform().combine(transform);
    }

    // Node Behavior Getters and Setters

    /**
     * Whether this node has been removed from the scene tree.
     *
     * @return if the node is destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Delete this node from the scene, removing it from its parent and
     * destroying all its descendants at the end of the current frame. The
     * properties {@link getScene}, {@link getParent}, and {@link getTransform}
     * will return null after the node is destroyed.
     * <p>
     * <b>Warning:</b> Destroying a node is permanent and cannot be reversed!
     */
    public void setDestroyed() {
        if (destroyed) return;
        destroyed = true;
        children.forEach(Node::setDestroyed);
        if (parent != null) parent.setChildrenChanged();
        if (scene != null) scene.onNodeRemoved(this);
        children.clear();
    }

    /**
     * Custom behavior for when this node or any of its ancestors is destroyed.
     * The properties {@link #getScene}, {@link #getParent}, and
     * {@link #getTransform} will still be accessible.
     * <p>
     * Warning: Calling {@code onDestroy} directly can lead to unpredictable
     * behavior. It is better to call {@link #setDestroyed()} instead.
     */
    protected void onDestroy() {
    }

    /**
     * Whether this node and all its descendants should be updated. If any
     * ancestor node is disabled, then this node will not be updated regardless.
     *
     * @return if this node is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set whether this node should be updated. Will not affect whether the
     * parent node is enabled.
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
     * Warning: Calling {@code onEnable()} directly can lead to unpredictable
     * behavior. It is better to call {@code setEnabled(true)} instead.
     */
    protected void onEnable() {
    }

    /**
     * Custom user behavior for when this script is disabled.
     * <p>
     * Warning: Calling {@code onEnable()} directly can lead to unpredictable
     * behavior. It is better to call {@code setEnabled(false)} instead.
     */
    protected void onDisable() {
    }

    /**
     * Whether this node should update, meaning it and all of its ancestors are
     * enabled.
     *
     * @return if the node should update
     */
    public boolean shouldUpdate() {
        if (parent == null) return enabled;
        else return enabled && parent.shouldUpdate();
    }
    // TODO check UI usages
    // TODO check animator usages

    /**
     * Whether this node and all its descendants should be rendered. If any
     * ancestor node is invisible, then this node will not be rendered
     * regardless.
     *
     * @return if this node is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set whether this node should be rendered. Will not affect whether the
     * parent node is visible.
     *
     * @param visible if the node is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Whether this node should render, meaning it and all of its ancestors are
     * visible.
     *
     * @return if the node should render
     */
    public boolean shouldRender() {
        if (parent == null) return visible;
        else return visible && parent.shouldRender();
    }

    /**
     * The update order of this node, or the order in which it will be updated.
     * Nodes with higher lesser update orders will be updated before those with
     * greater update orders.
     *
     * @return the update order
     */
    public int getUpdateOrder() {
        return updateOrder;
    }

    /**
     * Set the node's update order or the order in which it will be updated.
     * Nodes with higher lesser update orders will be updated before those with
     * greater update orders.
     *
     * @param updateOrder the update order
     */
    public void setUpdateOrder(int updateOrder) {
        this.updateOrder = updateOrder;
        if (scene != null) scene.setSceneChanged();
    }

    /**
     * The visual ordering of this node, or the order in which it will be drawn.
     * Nodes with greater z-indexes will be drawn on top of those with lesser
     * z-indexes.
     *
     * @return the z-index
     */
    public int getZIndex() {
        return zIndex;
    }

    /**
     * Set the node's z-index, or the order in which it will be drawn.
     * Nodes with greater z-indexes will be drawn on top of nodes with lower
     * z-indexes.
     *
     * @param zIndex the z-index
     */
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
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
