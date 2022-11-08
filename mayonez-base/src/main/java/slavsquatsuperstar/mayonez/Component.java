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
    protected GameObject gameObject;

    /**
     * A reference to the parent object's {@link Transform}.
     */
    protected Transform transform = new Transform(); // use blank transform in case no parent

    private boolean enabled = true;

    // Game Loop Methods

    /**
     * Initialize any fields needed for subclasses or scripts.
     */
    public void start() {
    }

    /**
     * Refresh the component's game logic.
     *
     * @param dt seconds since the last frame
     */
    public void update(float dt) {
    }

    /**
     * Draw the component on the screen.
     *
     * @param g2 the window's graphics object
     */
    public void render(Graphics2D g2) {
    }

    /**
     * Destroy this component and free up system resources once the parent {@link GameObject} is destroyed.
     */
    final void destroy() {
        gameObject = null;
        onDestroy();
    }

    /**
     * Custom behavior for when this component is destroyed.
     */
    public void onDestroy() {
    }

    // Getters and Setters

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

    /**
     * Returns the parent {@link GameObject} this Component is attached to.
     *
     * @return the game object
     */
    public GameObject getObject() {
        return gameObject;
    }

    // should only be used by Scene class

    /**
     * Adds this Component to a parent {@link GameObject}. Should only be used by the {@link Scene} class.
     *
     * @param gameObject a game object
     * @return this component
     */
    final Component setObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.transform;
        return this;
    }

    /**
     * A reference to the {@link Scene} the parent object belongs to.
     *
     * @return the parent scene
     */
    public Scene getScene() {
        return gameObject.getScene();
    }

    public boolean isInScene(Scene scene) {
        return scene != null && scene.equals(getScene()); // This could cause a NPE
    }

    public Transform getTransform() {
        return transform;
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Component> T setTransform(Transform transform) {
        this.transform = (transform != null) ? transform : new Transform();
        return (T) this;
    }

    @Override
    public String toString() {
        return String.format(
                "%s (%s)",
                getClass().isAnonymousClass() ? "Component" : getClass().getSimpleName(),
                gameObject == null ? "<No Parent>" : gameObject.name
        );
        // Use Component for class if anonymous instance
    }
}
