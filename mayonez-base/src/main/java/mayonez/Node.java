package mayonez;

import mayonez.util.StringUtils;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

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

    public Node(@Nullable String name) {
        nodeID = nodeCounter++;
        this.name = validateName(name);
    }

    // Property Getters and Setters

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
    }

    String validateName(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return StringUtils.getObjectClassName(this);
        } else {
            return name;
        }
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
