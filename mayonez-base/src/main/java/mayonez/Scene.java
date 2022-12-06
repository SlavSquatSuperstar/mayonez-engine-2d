package mayonez;

import mayonez.engine.GameLayer;
import mayonez.event.Receivable;
import mayonez.graphics.Color;
import mayonez.graphics.*;
import mayonez.graphics.renderer.GLSceneRenderer;
import mayonez.graphics.renderer.JSceneRenderer;
import mayonez.graphics.renderer.SceneRenderer;
import mayonez.graphics.renderer.debug.DebugRenderer;
import mayonez.graphics.renderer.debug.GLDebugRenderer;
import mayonez.graphics.renderer.debug.JDebugRenderer;
import mayonez.math.Vec2;
import mayonez.physics.PhysicsWorld;

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
// TODO draw background image
public abstract class Scene implements GameLayer {

//    private static int sceneCounter = 0; // total number of game objects created

    // Scene Information
//    final int sceneID;
    private final String name;
    private final float scale; // scene scale, or how many pixels correspond to a world unit
    private final Vec2 size; // scene size, or zero if unbounded
    protected Color background = Colors.WHITE;

    // Scene Layers
    private final List<GameObject> objects;
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
//        sceneID = sceneCounter++;
        this.name = name;
        this.size = new Vec2(width, height).div(scale);
        this.scale = scale;

        objects = new ArrayList<>();
        changesToScene = new LinkedList<>();

        if (Boolean.TRUE.equals(Mayonez.getUseGL())) {
            renderer = new GLSceneRenderer();
            debugRenderer = new GLDebugRenderer();
            camera = new GLCamera(Mayonez.getScreenSize(), this.getScale());
        } else {
            renderer = new JSceneRenderer();
            debugRenderer = new JDebugRenderer();
            camera = new JCamera(Mayonez.getScreenSize(), this.getScale());
        }
        physics = new PhysicsWorld();
    }

    // Game Loop Methods

    /**
     * Initialize all objects and begin updating the scene.
     */
    @Override
    public final void start() {
        if (!started) {
            addObject(Camera.createCameraObject(camera));
            init();
            // Start Layers
            renderer.start();
            debugRenderer.start();
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
                g2.setColor(background.toAWT());
                g2.fillRect(0, 0, (int) ((size.x + 1) * scale), (int) ((size.y + 1) * scale));
                onUserRender(g2);
            }
            // Render objects
            renderer.render(g2);
            debugRenderer.render(g2);
        }
    }

    /**
     * Pauses the scene but does not destroy any objects.
     */
    final void unload() {
        if (loaded) loaded = false;
    }

    @Override
    public final void stop() {
        if (started) {
            // Destroy objects
            objects.forEach(GameObject::onDestroy);
            objects.clear();
            // Clear layers
            renderer.stop();
            debugRenderer.stop();
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
        // Handle duplicate names
        int count = 0;
        String fmtName = obj.getName();
        for (GameObject found = getObject(obj.getName()); found != null; found = getObject(fmtName))
            fmtName = String.format("%s (%d)", obj.getName(), ++count);
        obj.setName(fmtName);

        if (started) { // Dynamic add: add to scene and layers in next frame
            changesToScene.offer((_args) -> {
                objects.add(obj.setScene(this));
                obj.start(); // add object components so renderer and physics can access it
                if (started) { // Dynamic add
                    renderer.addObject(obj);
                    physics.addObject(obj);
                }
                Logger.debug("Game: Added object \"%s\" to scene \"%s\"", obj.getName(), this.name);
            });
        } else { // Static add: add to scene, and add to layers on start
            objects.add(obj.setScene(this));
            obj.start(); // add object components so renderer and physics can access it
            Logger.debug("Game: Added object \"%s\" to scene \"%s\"", obj.getName(), this.name);
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
            renderer.removeObject(obj);
            physics.removeObject(obj);
            obj.onDestroy();
            Logger.debug("Game: Removed object \"%s\" from scene \"%s\"", obj.getName(), this.name);
        });
    }

    /**
     * Finds the {@link GameObject} with the given name. For consistent results, avoid duplicate names.
     *
     * @param name a unique object identifier
     * @return the object
     */
    public GameObject getObject(String name) {
        for (GameObject o : objects) {
            if (o.getName().equalsIgnoreCase(name)) return o;
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
    public void setBackground(Color background) {
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
    public String toString() {
        return String.format("%s (%s)", name, getClass().isAnonymousClass() ?
                "Scene" : getClass().getSimpleName());
    }

}
