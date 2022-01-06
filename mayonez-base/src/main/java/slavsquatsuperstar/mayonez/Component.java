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
    /**
     * A reference to the parent object's {@link Transform}.
     */
    protected Transform transform;
    private boolean enabled = true;

    // Game Loop Methods

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

    /**
     * Destroy this component and and free up system resources once the parent {@link GameObject} is destroyed.
     */
    public void destroy() {}

    // Getters and Setters

    public GameObject getParent() {
        return parent;
    }

    public Component setParent(GameObject parent) {
        this.parent = parent;
        this.transform = parent.transform;
        return this;
    }

    /**
     * @return The {@link Scene} the parent object belongs to.
     */
    public Scene getScene() {
        return parent.getScene();
    }

    public boolean isInScene(Scene scene) {
        return scene.equals(getScene()); // This could cause a NPE
    }

    public Transform getTransform() {
        return transform;
    }

    /**
     * Whether this component should be updated.
     *
     * @return if this component is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Component> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getClass().isAnonymousClass() ?
                "Component" : getClass().getSimpleName(), parent.name);
    }
}
