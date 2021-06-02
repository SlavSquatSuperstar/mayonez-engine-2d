package com.slavsquatsuperstar.mayonez;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.slavsquatsuperstar.game.PlayerController;
import com.slavsquatsuperstar.mayonez.components.Component;
import com.slavsquatsuperstar.mayonez.components.Script;

/**
 * A collection of {@link Component}s representing an in-game object.
 *
 * @author SlavSquatSuperstar
 */
public class GameObject {

    private String name;
    private boolean destroyed;
    protected boolean keepInScene;

    public Transform transform;
    private ArrayList<Component> components;
    private ArrayList<Script> scripts;
    protected Scene scene;

    public GameObject(String name, Vector2 position) {
        this.name = name;
        this.transform = new Transform(position);
        components = new ArrayList<>();
        scripts = new ArrayList<>(4);
    }

    // Game Methods

    /**
     * Add necessary components.
     */
    protected void init() {}

    /**
     * Initialize all components. Make sure to call super() if overriding!
     */
    protected void start() {
        init();
        components.forEach(c -> c.start());
        scripts.forEach(s -> s.start());
    }

    /**
     * Update all components. Make sure to call super() if overriding!
     */
    protected void update(float dt) {
        // TODO component call order
        // TODO just make a new collection for scripts
        components.forEach(c -> c.update(dt));
        scripts.forEach(s -> {
            if (s.enabled)
                s.update(dt);
        });
        components.stream().filter(Script.class::isInstance).forEachOrdered(s -> {
            if (((Script) s).enabled)
                s.update(dt);
        });
    }

    /**
     * Render all components. Make sure to call super() if overriding!
     */
    protected void render(Graphics2D g2) {
        components.forEach(c -> c.render(g2));
    }

    // Component Methods

    public void addComponent(Component comp) {
        // maybe make annotation (multiple scripts should suppress warning)
//		if (null != getComponent(comp.getClass()));
//			Logger.log("GameObject: Adding multiple components of the same type is not recommended");

        comp.parent = this;
        if (comp instanceof Script)
            scripts.add((Script) comp);
        else
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

    public String getName() {
        return name;
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

}
