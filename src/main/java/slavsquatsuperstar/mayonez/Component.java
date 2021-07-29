package slavsquatsuperstar.mayonez;

import java.awt.*;

/**
 * A data structure representing traits and behaviors of a {@link GameObject}.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Component {

    /**
     * The {@link GameObject} this component belongs to.
     */
    protected GameObject parent;
    private boolean enabled = true;

    /**
     * Initialize any fields needed for subclasses or scripts.
     */
    public void start() {}

    /**
     * Refresh the component's game logic.
     *
     * @param dt seconds since the last frame
     */
    public void update(float dt) {}

    /**
     * Draw the component on the screen.
     *
     * @param g2 the window's graphics object
     */
    public void render(Graphics2D g2) {}

    // Getters and Setters

    public Component setParent(GameObject parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @return The {@link Scene} the parent object belongs to.
     */
    public Scene getScene() {
        return parent.getScene();
    }

    public boolean isInScene(Scene scene) {
        // This could cause a NPE
        return scene.equals(getScene());
    }

    /**
     * Whether this component should be updated.
     *
     * @return if this component is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    public Component setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getClass().isAnonymousClass() ?
                "Component" : getClass().getSimpleName(), parent.name);
    }
}
