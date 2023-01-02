package mayonez;

import mayonez.graphics.Color;
import mayonez.graphics.*;
import mayonez.graphics.renderer.DebugRenderer;
import mayonez.graphics.renderer.GLDefaultRenderer;
import mayonez.graphics.renderer.JDefaultRenderer;
import mayonez.graphics.renderer.SceneRenderer;
import mayonez.graphics.sprites.Sprite;
import mayonez.io.image.Texture;
import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.physics.PhysicsWorld;
import mayonez.util.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.function.Consumer;
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
    private final SceneRenderer renderer;
    private final DebugRenderer debugRenderer;
    private final PhysicsWorld physics;
    private final Camera camera; // TODO probably want to sort this to last

    // Scene State
//    private final Consumer<GameObject> addObject, removeObject;
    private final Queue<SceneChange> changesToScene; // Use a separate list to avoid concurrent exceptions
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

        // Initialize layers
        objects = new ArrayList<>();
        if (Mayonez.getUseGL()) {
            renderer = new GLDefaultRenderer();
            camera = new GLCamera(Mayonez.getScreenSize(), this.getScale());
        } else {
            renderer = new JDefaultRenderer();
            camera = new JCamera(Mayonez.getScreenSize(), this.getScale());
        }
        debugRenderer = (DebugRenderer) renderer;
        physics = new PhysicsWorld();

        // Scene changes
        changesToScene = new LinkedList<>();
//        addObject = (obj) -> {
//            objects.add(obj.setScene(this));
//            obj.start(); // add components first so renderer and physics can access it
//            if (started) {
//                renderer.addObject(obj);
//                physics.addObject(obj);
//            }
//            Logger.debug("Added object \"%s\" to scene \"%s\"", obj.getNameAndID(), this.name);
//        };
//        removeObject = (obj) -> {
//            objects.remove(obj);
//            renderer.removeObject(obj);
//            physics.removeObject(obj);
//            obj.onDestroy();
//            Logger.debug("Removed object \"%s\" from scene \"%s\"", obj.getNameAndID(), this.name);
//        };
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
            if (separateDebugRenderer()) debugRenderer.start();
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
            // Update scene and objects
            objects.forEach(o -> {
                o.update(dt);
                if (o.isDestroyed()) removeObject(o);
            });
            onUserUpdate(dt);
            physics.step(dt); // Update physics
            camera.gameObject.update(dt); // Update camera last
            // Remove destroyed objects or add new
            while (!changesToScene.isEmpty()) changesToScene.poll().change();
        }
    }

    /**
     * Draw the background image and all the objects in the scene.
     *
     * @param g2 the window's graphics object
     */
    public final void render(Graphics2D g2) {
        if (started && loaded) {
            renderer.render(g2);
            if (separateDebugRenderer()) debugRenderer.render(g2);
            onUserRender(g2);
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
            if (separateDebugRenderer()) debugRenderer.stop();
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
    public final void addObject(GameObject obj) {
        if (obj == null) return;
        SceneChange addObject = new SceneChange(obj, o -> {
            objects.add(o.setScene(this));
            o.start(); // add components first so renderer and physics can access it
            if (started) { // dynamic add
                renderer.addObject(o);
                physics.addObject(o);
            }
            Logger.debug("Added object \"%s\" to scene \"%s\"", o.getNameAndID(), this.name);
        });
        if (started) changesToScene.offer(addObject); // Dynamic add: add to scene and layers in next frame
        else addObject.change();
    }

    /**
     * Removes an object to this scene and destroys it. Also removes the object from the renderer and physics engine.
     *
     * @param obj a {@link GameObject}
     */
    public final void removeObject(GameObject obj) {
        if (obj == null) return;
        SceneChange removeObject = new SceneChange(obj, o -> {
            objects.remove(o);
            renderer.removeObject(o);
            physics.removeObject(o);
            o.onDestroy();
            Logger.debug("Removed object \"%s\" from scene \"%s\"", o.getNameAndID(), this.name);
        });
        changesToScene.offer(removeObject);
    }

    /**
     * Finds the {@link GameObject} with the given name (case-insensitive). For consistent results, avoid duplicate names.
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
    public int numObjects() {
        return objects.size();
    }

    // Scene Properties

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
     * Returns a random vector within the scene's bounds.
     *
     * @return a random position
     */
    public Vec2 getRandomPosition() {
        return Random.randomVector(-getWidth(), getWidth(), -getHeight(), getHeight()).mul(0.5f);
    }

    /**
     * Returns the number pixels on the screen for every unit in the world. Defaults to a 1:1 scale.
     *
     * @return the scene scale
     */
    public float getScale() {
        return scale;
    }

    // Getters and Setters

    public Sprite getBackground() {
        return background;
    }

    /**
     * Sets the background color of this scene. Defaults to white.
     *
     * @param background a color
     */
    public void setBackground(Color background) {
        this.background = Sprite.create(background);
    }

    /**
     * Sets the background image of this scene.
     *
     * @param background a texture
     */
    public void setBackground(Texture background) {
        this.background = Sprite.create(background);
    }

    /**
     * Returns the main camera of the scene.
     *
     * @return the {@link GLCamera} instance
     */
    public Camera getCamera() {
        return camera;
    }

    final DebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public boolean separateDebugRenderer() { // if scene renderer is also debug renderer
        return renderer != debugRenderer;
    }

    public void setGravity(Vec2 gravity) {
        physics.setGravity(gravity);
    }

    // Object Overrides

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

    // Helper Interface

    /**
     * Allows objects to be dynamically added and removed from the scene.
     *
     * @author SlavSquatSuperstar
     */
    private record SceneChange(GameObject obj, Consumer<GameObject> func) {

        public void change() {
            func.accept(obj);
        }
    }

}
