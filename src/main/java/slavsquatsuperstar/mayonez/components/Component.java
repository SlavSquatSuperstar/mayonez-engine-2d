package slavsquatsuperstar.mayonez.components;

import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;

import java.awt.*;

/**
 * A data structure representing traits and behaviors of a {@link GameObject}.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Component {

    /**
     * Whether this component should be updated.
     */
    public boolean enabled = true;

    /**
     * The {@link GameObject} this component belongs to.
     */
    protected GameObject parent;

    /**
     * Initialize any fields needed for subclasses or scripts.
     */
    public void start() {}

    /**
     * Refresh the component state in the world.
     */
    public void update(float dt) {}

    /**
     * Draw the component on the screen.
     */
    public void render(Graphics2D g2) {}

    // Getters and Setters

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    /**
     * @return The {@link Scene} the parent object belongs to.
     */
    public Scene scene() {
        return parent.getScene();
    }

    public boolean isInScene(Scene scene) {
        // This could cause a NPE
        return scene.equals(scene());
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getClass().isAnonymousClass() ?
                "Component" : getClass().getSimpleName(), parent.name);
    }
}
