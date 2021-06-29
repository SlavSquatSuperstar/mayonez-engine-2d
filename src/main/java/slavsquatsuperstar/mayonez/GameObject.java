package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.components.Component;
import slavsquatsuperstar.mayonez.components.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A collection of {@link Component}s representing an in-game object.
 *
 * @author SlavSquatSuperstar
 */
public class GameObject {

    public final String name;
    public Transform transform;

    public boolean keepInScene = false; // TODO convert to KeepInBounds script, and enable/disable
    /**
     * Whether this object can be moved or rotated through the physics engine and collisions.
     * Scripts can still change this object's {@link Transform} regardless.
     */
    public boolean followPhysics = true;
    protected Scene scene;
    private boolean destroyed = false;

    private final ArrayList<Component> components = new ArrayList<>();
    private ArrayList<Class> updateOrder = null;

    public GameObject(String name, Vector2 position) {
        this.name = name;
        this.transform = new Transform(position);
    }

    // Game Methods

    /**
     * Add necessary components.
     */
    protected void init() {}

    /**
     * Initializes all components.
     */
    public final void start() {
        init();
        setUpdateOrder(Component.class, Script.class);
        components.forEach(Component::start);
    }

    /**
     * Updates all components. Make sure to call super() if overriding!
     */
    public void update(float dt) {
        // TODO component call order
        // TODO just make a new collection for scripts
        components.forEach(c -> {
            if (c.enabled) c.update(dt);
        });
    }

    /**
     * Renders all components. Make sure to call super() if overriding!
     */
    public void render(Graphics2D g2) {
        components.forEach(c -> {
            if (c.enabled) c.render(g2);
        });
    }

    // Component Methods

    public void addComponent(Component comp) {
        // maybe make annotation (multiple scripts should suppress warning)
//		if (null != getComponent(comp.getClass()))
//			Logger.log("GameObject: Adding multiple components of the same type is not recommended");

        comp.parent = this;
        components.add(comp);
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

    public ArrayList<Component> getComponents() {
        return components;
    }

    public <T extends Component> ArrayList<T> getComponents(Class<T> componentCls) {
        ArrayList<T> found = new ArrayList<>();
        components.forEach(c -> {
            if (componentCls.isInstance(c))
                found.add(componentCls.cast(c));
        });
        return found;
    }

    public <T extends Component> T getComponent(Class<T> componentCls) {
        try {
            for (Component c : components)
                if (componentCls.isAssignableFrom(c.getClass()))
                    return componentCls.cast(c);
        } catch (ClassCastException e) { // This shouldn't happen!
            Logger.log("GameObject: Error accessing %s component", componentCls.getName());
            Logger.log(e.getStackTrace());
        }
        return null;
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

    public void destroy() {
        destroyed = true;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, getClass().isAnonymousClass() ?
                "GameObject" : getClass().getSimpleName());
    }

    private int getUpdateOrder(ArrayList<Class> classOrder, Class cls) {
        int i = classOrder.indexOf(cls);
        if (i > -1)
            return i;
        i = getUpdateOrder(classOrder, cls.getSuperclass());
        return (i > -1) ? i : classOrder.size();
    }

    public void setUpdateOrder(Class... order) {
        if (updateOrder == null) {
            updateOrder = new ArrayList<>();
            updateOrder.addAll(Arrays.asList(order));
        }
        components.sort(Comparator.comparingInt(c -> getUpdateOrder(updateOrder, c.getClass())));
    }

}
