package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.components.Camera;
import slavsquatsuperstar.mayonez.physics2d.Physics2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * A collection of {@link GameObject}s that represents an in-game world.
 *
 * @author SlavSquatSuperstar
 */
// TODO coordinate system
public abstract class Scene {

    protected String name;
    protected int width, height;

    protected boolean bounded;
    protected Color background;
    // Object Fields
    protected ArrayList<GameObject> objects, toRemove;
    private boolean started;
    private Renderer renderer;
    private Physics2D physics; // rbs are being updated twice
    private Camera camera;

    public Scene(String name) {
        this(name, 0, 0);
        bounded = false;
    }

    public Scene(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        bounded = true;

        objects = new ArrayList<>();
        toRemove = new ArrayList<>();

        camera = new Camera(width, height);
        renderer = new Renderer(camera);
        physics = new Physics2D(Preferences.GRAVITY);
    }

    // Game Methods

    /**
     * Add necessary objects.
     */
    protected void init() {}

    public final void start() {
        if (started)
            return;

        addObject(new GameObject("Camera", new Vector2()) {
            @Override
            protected void init() {
                keepInScene = true;
                addComponent(camera);
            }

            // Don't want to get rid of the camera!
            @Override
            public void destroy() {
                return;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }
        });
        init();
        started = true;
    }

    public final void update(float dt) {
        if (!started)
            return;

        // Update Objects and Camera
        objects.forEach(o -> {
            o.update(dt);
            // Flag objects for destruction
            if (o.isDestroyed())
                removeObject(o);
        });
        physics.update(dt);

        // Remove destroyed objects at the end of the frame
        toRemove.forEach(o -> {
            objects.remove(o);
            renderer.remove(o);
            physics.remove(o);
        });
        toRemove.clear();
    }

    public final void render(Graphics2D g2) {
        if (!started)
            return;

        g2.setColor(background);
        g2.fillRect(0, 0, width, height);
        renderer.render(g2);

        // g.drawImage(background, 0, 0, height, width, camera.getXOffset(),
        // camera.getYOffset(), background.getWidth(), background.getHeight(), null);
    }

    // Object Methods

    public void addObject(GameObject obj) {
        obj.setScene(this);
        obj.start(); // add components so renderer and physics can access it
        objects.add(obj);
        renderer.add(obj);
        physics.add(obj);
        Logger.log("Scene: Added GameObject \"%s\"", obj.name);
    }

    public void removeObject(GameObject obj) {
        obj.destroy();
        toRemove.add(obj);
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    public <T extends GameObject> ArrayList<T> getObjects(Class<T> cls) {
        ArrayList<T> found = new ArrayList<>();
        objects.forEach(o -> {
            if (cls == null || cls.isInstance(o))
                found.add(cls.cast(o));
        });
        return found;
    }

    // TODO use hash map or bin search?
    public GameObject getObject(String name) {
        for (GameObject o : objects)
            if (o.name.equalsIgnoreCase(name))
                return o;
        return null;
    }

    // Getters and Setters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public boolean isBounded() {
        return bounded;
    }

    public Camera camera() {
        return camera;
    }

    public void setGravity(Vector2 gravity) {
        physics.gravity = gravity;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, getClass().isAnonymousClass() ?
                "Scene" : getClass().getSimpleName());
    }
}
