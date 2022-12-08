package mayonez;

import mayonez.event.Receivable;
import mayonez.graphics.Camera;
import mayonez.graphics.Colors;
import mayonez.graphics.GLCamera;
import mayonez.graphics.JCamera;
import mayonez.graphics.renderer.*;
import mayonez.graphics.sprite.Sprite;
import mayonez.math.Vec2;
import mayonez.physics.PhysicsWorld;
import mayonez.util.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A collection of {@link GameObject}s representing an in-game world.
 *
 * @author SlavSquatSuperstar
 */
// TODO current cursor object
// TODO draw background image
public abstract class Scene {

    private static int sceneCounter = 0; // total number of game objects created

    // Scene Information
    final int sceneID; // UUID for this scene
    private final String name;
    private final float scale; // scene scale, or how many pixels correspond to a world unit
    private final Vec2 size; // scene size, or zero if unbounded
    protected Sprite background;

    // Scene Layers
    private final List<GameObject> objects;
    private final boolean useGL;
    private final SceneRenderer renderer;
    private final DebugRenderer debugRenderer;
    private final PhysicsWorld physics;
    private final Camera camera; // TODO probably want to sort this to last

    // Scene State
    private final Queue<Receivable> changesToScene; // Use a separate list to avoid concurrent exceptions
    private boolean started = false; // if initialized
    private boolean loaded = false; // if currently active

    /**
     * Creates an empty scene with size of 0x0 and a scale of 1.
     */
    public Scene(String name) {
        this(name, 0, 0, 1);
    }

    /**
     * Creates an empty scene and sets the bounds and scale.
     *
     * @param name   the name of the scene
     * @param width  the width of the scene, in pixels
     * @param height the height of the scene, in pixels
     * @param scale  the scale of the scene
     */
    public Scene(String name, int width, int height, float scale) {
        sceneID = sceneCounter++;
        this.name = name;
        this.size = new Vec2(width, height).div(scale);
        this.scale = scale;
        background = Sprite.create(Colors.WHITE);

        objects = new ArrayList<>();
        changesToScene = new LinkedList<>();

        useGL = Boolean.TRUE.equals(Mayonez.getUseGL());
        if (useGL) {
            renderer = new GLSceneRenderer();
            debugRenderer = new GLDebugRenderer();
            camera = new GLCamera(Mayonez.getScreenSize(), this.getScale());
        } else {
            renderer = new JDefaultRenderer();
            debugRenderer = (DebugRenderer) renderer;
            camera = new JCamera(Mayonez.getScreenSize(), this.getScale());
        }
        physics = new PhysicsWorld();
    }

    // Game Loop Methods

    /**
     * Initialize all objects and begin updating the scene.
     */
    public final void start() {
        if (!started) {
            addObject(Camera.createCameraObject(camera));
            init();
            // Start Layers
            renderer.start();
            if (useGL) debugRenderer.start();
            physics.start();
            // Add to Layers
            renderer.setScene(this);
            physics.setScene(this);
            started = true;
            loaded = true;
        }
    }

    /**
     * Resumes the scene but does not reinitialize any objects.
     */
    final void load() {
        if (!loaded) loaded = true;
    }

    /**
     * Refresh the internal states of all objects in the scene.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        if (started && loaded) {
            // Update objects and camera
            objects.forEach(o -> {
                o.update(dt);
                if (o.isDestroyed()) removeObject(o);
            });
            camera.gameObject.update(dt);
            onUserUpdate(dt);
            physics.physicsUpdate(dt); // Update physics
            // Remove destroyed objects or add new ones at the end of the frame
            while (!changesToScene.isEmpty()) changesToScene.poll().onReceive();
        }
    }

    /**
     * Draw the background image and all the objects in the scene.
     *
     * @param g2 the window's graphics object
     */
    public final void render(Graphics2D g2) {
        if (started && loaded) {
            // Render background
            if (g2 != null) {
                g2.setColor(background.getColor().toAWT());
                g2.fillRect(0, 0, (int) ((size.x + 1) * scale), (int) ((size.y + 1) * scale));
                onUserRender(g2);
            }
            // Render objects
            renderer.render(g2);
            if (useGL) debugRenderer.render(g2);
        }
    }

    /**
     * Pauses the scene but does not destroy any objects.
     */
    final void unload() {
        if (loaded) loaded = false;
    }

    public final void stop() {
        if (started) {
            // Destroy objects
            objects.forEach(GameObject::onDestroy);
            objects.clear();
            // Clear layers
            renderer.stop();
            if (useGL) debugRenderer.stop();
            physics.stop();
            started = false;
            loaded = false;
        }
    }

    // User-Defined Methods

    /**
     * Provide user-defined initialization behavior, such as adding necessary game objects or setting gravity.
     */
    protected void init() {
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
        if (obj == null) return;
        Receivable addObject = (_args) -> {
            objects.add(obj.setScene(this));
            obj.start(); // add object components so renderer and physics can access it
            if (started) { // Dynamic add
                renderer.addObject(obj);
                physics.addObject(obj);
            }
            Logger.debug("Game: Added object \"%s [ID %d]\" to scene \"%s\"", obj.name, obj.objectID, this.name);
        };
        if (started) changesToScene.offer(addObject); // Dynamic add: add to scene and layers in next frame
        else addObject.onReceive();
    }

    /**
     * Removes an object to this scene and destroys it. Also removes the object from the renderer and physics engine.
     *
     * @param obj a {@link GameObject}
     */
    public final void removeObject(GameObject obj) {
        if (obj == null) return;
        changesToScene.offer((_args) -> {
            objects.remove(obj);
            renderer.removeObject(obj);
            physics.removeObject(obj);
            obj.onDestroy();
            Logger.debug("Game: Removed object \"%s [ID %d]\" from scene \"%s\"", obj.name, obj.objectID, this.name);
        });
    }

    /**
     * Finds the {@link GameObject} with the given name. For consistent results, avoid duplicate names.
     *
     * @param name the object name
     * @return the object
     */
    public GameObject getObject(String name) {
        for (GameObject o : objects) {
            if (o.name.equalsIgnoreCase(name)) return o;
        }
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

    public Vec2 getSize() {
        return size;
    }

    public float getWidth() {
        return size.x;
    }

    public float getHeight() {
        return size.y;
    }

    /**
     * Returns the main camera of the scene.
     *
     * @return the {@link JCamera} instance
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Sets the background color of this scene. Defaults to white.
     *
     * @param background an RGB color
     */
    public void setBackground(Sprite background) {
        this.background = background;
    }

    public void setGravity(Vec2 gravity) {
        physics.setGravity(gravity);
    }

    final DebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    /**
     * Returns the number pixels on the screen for every unit in the world. Defaults to a 1:1 scale.
     *
     * @return the scene scale
     */
    public float getScale() {
        return scale;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sceneID, name);
    }

    @Override
    public String toString() {
        // Use Scene for class name if anonymous instance
        return String.format(
                "%s [ID = %d] (%s)",
                name, sceneID,
                StringUtils.getClassName(this, "Scene")
        );

    }

}
