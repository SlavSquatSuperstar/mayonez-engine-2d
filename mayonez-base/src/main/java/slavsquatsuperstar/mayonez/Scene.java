package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.event.Receivable;
import slavsquatsuperstar.mayonez.graphics.Camera;
import slavsquatsuperstar.mayonez.graphics.JCamera;
import slavsquatsuperstar.util.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * A collection of {@link GameObject}s representing an in-game world.
 *
 * @author SlavSquatSuperstar
 */
// TODO current cursor object
public abstract class Scene {

    // Object Fields
    private final List<GameObject> objects;
    private final Queue<Receivable> changesToScene; // Use a separate list to avoid concurrent exceptions

    private final JCamera camera;

    // Scene Information
    private final String name;
    /**
     * The scale of the scene, or how many pixels correspond to a world unit.
     */
    private final float cellSize;
    /**
     * The size of the scene in world units, or zero if unbounded.
     */
    private final Vec2 size;
    private Color background = Colors.WHITE;
    private boolean started;

    /**
     * Creates an empty scene with size of 0x0 and a scale of 1.
     */
    public Scene(String name) {
        this(name, 0, 0, 1);
    }

    /**
     * Creates an empty scene and sets the bounds and cell size.
     *
     * @param name     the name of the scene
     * @param width    the width of the scene, in pixels
     * @param height   the height of the scene, in pixels
     * @param cellSize the scale of the scene
     */
    public Scene(String name, int width, int height, float cellSize) {
        this.name = name;
        this.size = new Vec2(width, height).div(cellSize);
        this.cellSize = cellSize;

        objects = new ArrayList<>();
        changesToScene = new LinkedList<>();
        camera = new JCamera(new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight()), cellSize);
    }

    // Game Loop Methods

    /**
     * Add necessary objects and provide user-defined initialization behavior.
     */
    protected void init() {
    }

    /**
     * Initialize all objects and begin updating the scene.
     */
    public final void start() {
        if (!started) {
            addObject(JCamera.createCameraObject(camera, this));
            init();
            started = true;
        }
    }

    /**
     * Refresh the internal states of all objects in the scene.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        if (started) {
            // Update Objects and Camera
            objects.forEach(o -> {
                o.update(dt);
                if (o.isDestroyed()) removeObject(o);
            });
            camera.gameObject.update(dt);
            onUserUpdate(dt);

            // Remove destroyed objects or add new ones at the end of the frame
            while (!changesToScene.isEmpty()) {
                changesToScene.poll().onReceive();
            }
        }
    }

    /**
     * Draw the background image.
     *
     * @param g2 the window's graphics object
     */
    public final void render(Graphics2D g2) {
        if (started) {
            g2.setColor(background);
            g2.fillRect(0, 0, (int) ((size.x + 1) * cellSize), (int) ((size.y + 1) * cellSize));
            onUserRender(g2);
        }
    }

    /**
     * Provide user-defined update behavior for this scene.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {
    }

    /**
     * Provide user-defined draw behavior for this scene.
     *
     * @param g2 the window's graphics object
     */
    protected void onUserRender(Graphics2D g2) {
    }

    // Object Methods

    /**
     * Adds an object to this scene and initializes it. Also adds the object to the renderer and physics engine.
     *
     * @param obj a {@link GameObject}
     */
    // TODO use events
    public final void addObject(GameObject obj) {
        // Handle duplicate names
        int count = 0;
        String fmtName = obj.name;
        for (GameObject found = getObject(obj.name); found != null; found = getObject(fmtName))
            fmtName = String.format("%s (%d)", obj.name, ++count);
        obj.name = fmtName;

        if (started) { // Dynamic add: add to scene and layers
            changesToScene.offer((_args) -> {
                objects.add(obj.setScene(this));
                obj.start(); // add object components so renderer and physics can access it
                if (started) { // Dynamic add
                    Mayonez.getRenderer().addObject(obj);
                    Mayonez.getPhysics().addObject(obj);
                }
                Logger.debug("Game: Added object \"%s\" to scene \"%s\"", obj.name, this.name);
            });
        } else { // Init add: add to scene, and add to layers on start
            objects.add(obj.setScene(this));
            obj.start(); // add object components so renderer and physics can access it
            Logger.debug("Game: Added object \"%s\" to scene \"%s\"", obj.name, this.name);
        }
    }

    /**
     * Removes an object to this scene and destroys it. Also removes the object from the renderer and physics engine.
     *
     * @param obj a {@link GameObject}
     */
    public final void removeObject(GameObject obj) {
        changesToScene.offer((_args) -> {
            objects.remove(obj);
            Mayonez.getRenderer().removeObject(obj);
            Mayonez.getPhysics().removeObject(obj);
            obj.onDestroy();
            Logger.debug("Game: Removed object \"%s\" from scene \"%s\"", obj.name, this.name);
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
     * Returns a copy of the all objects in this scene.
     *
     * @return the list of objects
     */
    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    /**
     * Finds all objects in the scene with the specified class.
     *
     * @param cls a {@link GameObject} subclass
     * @param <T> the object type
     * @return the list of objects
     */
    @SuppressWarnings({"unchecked"})
    public <T extends GameObject> List<T> getObjects(Class<T> cls) {
        return objects.stream().filter(o -> cls != null && cls.isInstance(o)).map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * Counts the number of objects in the scene.
     *
     * @return the amount of objects
     */
    public int countObjects() {
        return objects.size();
    }

    // Getters and Setters

    /**
     * Returns the name of this scene.
     *
     * @return the scene name
     */
    public String getName() {
        return name;
    }

    public float getWidth() {
        return size.x;
    }

    public float getHeight() {
        return size.y;
    }

    public Vec2 getSize() {
        return size;
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
     * @return the {@link JCamera} instance
     */
    public Camera getCamera() {
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

}
