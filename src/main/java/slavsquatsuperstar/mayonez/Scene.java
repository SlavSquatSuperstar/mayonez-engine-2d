package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.renderer.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of {@link GameObject}s representing an in-game world.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Scene {

    // Object Fields
    protected final List<GameObject> objects;
    private final List<SceneModifier> toModify; // Use a separate list to avoid concurrent exceptions
    private final Camera camera;

    // Scene Information
    private final String name;
    /**
     * How wide (x size) the scene is in world units.
     */
    private final float width;
    /**
     * How tall (y size) the scene is in world units.
     */
    private final float height;
    /**
     * How many pixels correspond to a world unit.
     */
    private final float cellSize;
    private Color background = Colors.WHITE;
    private boolean started;

    public Scene(String name) {
        this(name, 0, 0, 1);
    }

    public Scene(String name, int width, int height, int cellSize) {
        this.name = name;
        this.width = width / cellSize;
        this.height = height / cellSize;
        this.cellSize = cellSize;

        objects = new ArrayList<>();
        toModify = new ArrayList<>();
        camera = new Camera(this.width, this.height, cellSize);
    }

    // Game Loop Methods

    /**
     * Add necessary objects and provide user-defined initialization behavior.
     */
    protected void init() {}

    /**
     * Initialize all objects and begin updating the scene.
     */
    public final void start() {
        if (started) return;
        addObject(Camera.createCameraObject(camera));
        init();
        objects.add(objects.remove(0)); // update the camera last
        started = true;
    }

    /**
     * Refresh the internal states of all objects in the scene.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        if (!started) return;

        // Update Objects and Camera
        objects.forEach(o -> {
            o.update(dt);
            if (o.isDestroyed()) removeObject(o);
        });
        onUserUpdate(dt);

        // Remove destroyed objects or add new ones at the end of the frame
        if (!toModify.isEmpty()) {
            toModify.forEach(SceneModifier::modify);
            toModify.clear();
            objects.add(objects.remove(0)); // move the camera to the last index
        }
    }

    /**
     * Provide user-defined update behavior for this scene.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {}

    /**
     * Draw the background image.
     *
     * @param g2 the window's graphics object
     */
    public final void render(Graphics2D g2) {
        if (!started) return;
        g2.setColor(background);
        g2.fillRect(0, 0, (int) ((width + 1) * cellSize), (int) ((height + 1) * cellSize));
        onUserRender(g2);
    }

    /**
     * Provide user-defined draw behavior for this scene.
     *
     * @param g2 the window's graphics object
     */
    protected void onUserRender(Graphics2D g2) {}

    // Object Methods

    public final void addObject(GameObject obj) {
        // Handle duplicate names
        int count = 0;
        String name = obj.name;
        GameObject found = getObject(obj.name);
        while (found != null) {
            name = String.format("%s (%d)", obj.name, ++count);
            found = getObject(name);
        }
        obj.name = name;

        SceneModifier sm = () -> {
            objects.add(obj.setScene(this));
            obj.start(); // add object components so renderer and physics can access it
            Mayonez.getRenderer().addObject(obj);
            Mayonez.getPhysics().addObject(obj);
            Logger.trace("Added object \"%s\" to scene \"%s\"", obj.name, this.name);
        };
        if (started) toModify.add(sm);
        else sm.modify();
    }

    public final void removeObject(GameObject obj) {
        obj.destroy();
        toModify.add(() -> {
            objects.remove(obj);
            Mayonez.getRenderer().removeObject(obj);
            Mayonez.getPhysics().removeObject(obj);
            Logger.trace("Removed object \"%s\" from scene \"%s\"", obj.name, this.name);
        });
    }

    /**
     * Finds the {@link GameObject} with the given name. For consistent results, avoid duplicate names.
     *
     * @param name a unique object identifier
     * @return the object
     */
    public GameObject getObject(String name) {
        for (GameObject o : objects)
            if (o.name.equalsIgnoreCase(name))
                return o;
        return null;
    }

    /**
     * Finds all objects with the specified class.
     *
     * @param cls a {@link GameObject} subclass (use null to get all objects)
     * @param <T> the object type
     * @return the list of objects
     */
    @SuppressWarnings({"unchecked"})
    public <T extends GameObject> java.util.List<T> getObjects(Class<T> cls) {
        return objects.stream().filter(o -> cls == null || cls.isInstance(o)).map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * Counts the number of objects in the scene.
     *
     * @return the amount
     */
    public int countObjects() {
        return objects.size();
    }

    // Getters and Setters

    /**
     * Returns the name of this scene.
     *
     * @return the scene name.
     */
    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Returns the conversion between pixels on the screen and meters in the world. Defaults to a 1:1 scale.
     *
     * @return how many pixels make up a meter
     */
    public float getCellSize() {
        return cellSize;
    }

    /**
     * Sets the background color of this scene. Defaults to a very light gray.
     *
     * @param background an RGB color
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     * Returns the main camera of the scene.
     *
     * @return the {@link Camera} instance
     */
    public Camera camera() {
        return camera;
    }

    public void setGravity(Vec2 gravity) {
        Mayonez.getPhysics().setGravity(gravity);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, getClass().isAnonymousClass() ?
                "Scene" : getClass().getSimpleName());
    }

    /**
     * Flags a {@link GameObject} to be dynamically added or removed from the scene.
     */
    @FunctionalInterface
    private interface SceneModifier {
        void modify();
    }
}
