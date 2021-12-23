package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics2d.CollisionManifold;
import slavsquatsuperstar.mayonez.physics2d.colliders.Collider2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of {@link Component}s representing an in-game entity.
 *
 * @author SlavSquatSuperstar
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GameObject {

    // Object Information
    public String name;
    public final Transform transform;

    // Scene Information
    private Scene scene;
    private boolean destroyed = false;

    // Component Fields
    private final List<Component> components;
    private List<Class> updateOrder = null;

    public GameObject(String name, Vec2 position) {
        this.name = name;
        this.transform = new Transform(position);
        components = new ArrayList<>();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        components = new ArrayList<>();
    }

    public GameObject(String name) {
        this(name, new Vec2());
    }

    // Game Methods

    /**
     * Add necessary components and define custom user initialization behavior.
     */
    protected void init() {
    }

    /**
     * Initializes all components.
     */
    public final void start() {
        init();
        setUpdateOrder(Component.class, Script.class);
        components.forEach(Component::start);
    }

    /**
     * Updates all components.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        components.forEach(c -> {
            if (c.isEnabled()) c.update(dt);
        });
        onUserUpdate(dt);
    }

    /**
     * An overridable draw method for custom update behavior.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {}

    /**
     * Renders all components.
     *
     * @param g2 the window's graphics object (pass <code>null</code> for LWJGL instances)
     */
    public final void render(Graphics2D g2) {
        components.forEach(c -> {
            if (c.isEnabled()) c.render(g2);
        });
        onUserRender(g2);
    }

    /**
     * An overridable draw method for custom render behavior.
     *
     * @param g2 the window's graphics object
     */
    protected void onUserRender(Graphics2D g2) {}

    // Component Methods

    public final void addComponent(Component comp) {
        // maybe make annotation (multiple scripts should suppress warning)
//		if (null != getComponent(comp.getClass()))
//			Logger.log("GameObject: Adding multiple components of the same type is not recommended");
        components.add(comp.setParent(this));
    }

    public <T extends Component> void removeComponent(Class<T> cls) {
        for (int i = 0; i < components.size(); i++) { // Use indexing loop to avoid concurrent errors
            Component c = components.get(i);
//			if (cls.isAssignableFrom(c.getClass())) {
            if (cls.isInstance(c)) {
                components.remove(i);
                return;
            }
        }
    }

    /**
     * Finds all components with the specified class.
     *
     * @param cls a {@link Component} subclass (use null to get all objects)
     * @param <T> the components type
     * @return the list of components
     */
    public <T extends Component> List<T> getComponents(Class<T> cls) {
        return components.stream().filter(o -> cls == null || cls.isInstance(o)).map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * Finds the first component of the specified class.
     *
     * @param cls a {@link Component} subclass
     * @param <T> the component type
     * @return the component
     */
    public <T extends Component> T getComponent(Class<T> cls) {
        try {
            for (Component c : components)
                if (cls.isAssignableFrom(c.getClass()))
                    return cls.cast(c);
        } catch (ClassCastException e) { // This shouldn't happen!
            Logger.warn("GameObject: Error accessing %s component", cls.getSimpleName());
            Logger.log(e.getStackTrace());
        }
        return null;
    }

    /**
     * Sets the update order of this object's components. A subclass's order will override the order of its superclass.
     *
     * @param order an array of component subclasses
     */
    public void setUpdateOrder(Class... order) {
        if (updateOrder == null) {
            updateOrder = new ArrayList<>();
            updateOrder.addAll(Arrays.asList(order));
        }
        components.sort(Comparator.comparingInt(c -> getUpdateOrder(c.getClass())));
    }

    @SuppressWarnings("rawtypes")
    private int getUpdateOrder(Class componentCls) {
        int i = updateOrder.indexOf(componentCls);
        if (i > -1) return i;
        i = getUpdateOrder(componentCls.getSuperclass());
        return (i > -1) ? i : updateOrder.size();
    }

    // Callback Methods

    /**
     * What to do after making contact with another physical object
     *
     * @param collision the collision information
     */
    public void onCollision(CollisionManifold collision) {
    }

    /**
     * What to do after passing through a trigger area
     *
     * @param trigger the trigger collider
     */
    public void onTrigger(Collider2D trigger) {
    }

    // Getters and Setters

    public float getX() {
        return transform.position.x;
    }

    public void setX(float x) {
        transform.position.x = x;
    }

    public float getY() {
        return transform.position.y;
    }

    public void setY(float y) {
        transform.position.y = y;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Destroy this game object and remove it from the scene.
     */
    public void destroy() {
        destroyed = true;
    }

    public Scene getScene() {
        return scene;
    }

    public GameObject setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, getClass().isAnonymousClass() ?
                "GameObject" : getClass().getSimpleName());
    }

}
