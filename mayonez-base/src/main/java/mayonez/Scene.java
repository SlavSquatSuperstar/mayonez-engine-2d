package mayonez;

import com.uber.nullaway.annotations.Initializer;
import mayonez.graphics.camera.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.renderer.*;
import mayonez.util.*;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * An in-game world or level that holds multiple {@link mayonez.GameObject}s. Each scene
 * can  be given a background image or color.
 * <p>
 * Usage: Create a scene by instantiating a subclass or anonymous instance of
 * {@link mayonez.Scene}. Add objects to the scene by calling {@link #addObject}
 * inside the {@link #init()} method. Scenes may also define custom game logic
 * and graphics inside {@link #onUserUpdate} and {@link #onUserRender}. To remove an
 * object from the scene, call {@link GameObject#setDestroyed()}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.SceneManager} for more information.
 *
 * @author SlavSquatSuperstar
 */
// TODO current cursor object
public abstract class Scene {

    // Static Fields
    private static int sceneCounter = 0; // total number of scenes created

    // Scene Information
    final int sceneID; // UUID for this scene
    private final String name;
    private SceneState state; // if paused or running

    // Scene Objects
    protected boolean uniqueObjectNames = false;
    private final BufferedList<GameObject> objects;
    // TODO add/remove, queue callbacks
    private final SceneLayer[] layers;

    // Renderers
    private Camera camera;
    private final RenderLayer renderLayer;

    // Physics
    private final PhysicsWorld physics;

    /**
     * Creates an empty scene with a name. If the name is {@code null}, it will
     * default to the class name.
     *
     * @param name the scene name
     */
    public Scene(@Nullable String name) {
        sceneID = sceneCounter++;
        this.name = Objects.requireNonNullElse(name, StringUtils.getObjectClassName(this));
        state = SceneState.STOPPED;

        // Initialize layers
        objects = new BufferedList<>();
        layers = new SceneLayer[SceneLayer.NUM_LAYERS];
        renderLayer = RendererFactory.createRenderLayer(Mayonez.getUseGL());
        physics = new DefaultPhysicsWorld();
    }

    // Initialization Methods

    /**
     * Initialize all objects and begin updating the scene. Calls {@link GameObject#start()}
     * for all objects added on start.
     */
    @Initializer
    final void start() {
        // Set up layers
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new SceneLayer(i);
        }

        // Add camera
        camera = CameraFactory.createCamera();
        addObject(CameraFactory.createCameraObject(camera));

        // Add objects
        init();

        // Start objects
        state = SceneState.RUNNING;
        objects.forEach(this::startObject);

        // Add objects added through child init/start
        objects.processBuffer();
    }

    /**
     * Add game objects to this scene before the scene starts or initialize fields
     * after this scene has been loaded.
     * The method {@link #getCamera()} is accessible here.
     * <p>
     * Usage: Subclasses may override this method and can also call {@code super.init()}.
     * <p>
     * Warning: Calling {@code init()} at any other point in time may lead to unintended errors
     * and should be avoided!
     */
    protected void init() {
    }

    // Update Methods

    /**
     * Processes physics and updates all objects on a fixed tick.
     *
     * @param dt seconds between fixed ticks
     */
    final void fixedUpdate(float dt) {
        if (isRunning()) {
            physics.step(dt);
            objects.forEach(obj -> {
                if (obj.isEnabled()) obj.doFixedUpdate(dt);
            });
        }
    }

    // Input and UI need to run when paused
    // TODO process mode: enable/disable
    // Rendering needs to continue when paused
    // TODO have batch draw last data

    /**
     * Updates all objects on a render frame.
     *
     * @param dt seconds since the last frame
     */
    final void update(float dt) {
        if (!isStopped()) {
            onUserUpdate(dt); // TODO move input to window
        }
        if (isRunning()) {
            objects.forEach(obj -> {
                if (obj.isEnabled()) {
                    obj.doUpdate(dt);
                    if (obj.isDestroyed()) removeObject(obj); // Check for removals
                }
            });
        }
        // Add or remove objects
        objects.processBuffer();
        if (isDestroyed()) stop();
    }

    /**
     * Provide user-defined update behavior for this scene.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {
    }

    // Render Methods

    /**
     * Redraws everything in the current scene, including backgrounds, sprites, and UI.
     *
     * @param g2 the window's graphics object, if using the AWT engine
     */
    final void render(@Nullable Graphics2D g2) {
        if (!isStopped()) {
            onUserRender();
            objects.forEach(obj -> {
                if (obj.isVisible()) obj.doDebugRender();
            });
            renderLayer.render(g2);
        }
    }

    /**
     * Provide user-defined draw behavior for this scene.
     */
    protected void onUserRender() {
    }

    // Stop Methods

    /**
     * Signal the scene to stop updating and destroy all objects after this frame.
     */
    final void destroy() {
        // Make sure the scene finishes updating so component transforms aren't null
        state = SceneState.DESTROYED;
    }

    /**
     * Destroy all objects in the scene immediately.
     */
    final void stop() {
        // Destroy all objects
        camera.setSubject(null);
        objects.forEach(GameObject::onDestroy);

        // Clear all objects
        objects.clear();
        renderLayer.clear();
        physics.clear();

        state = SceneState.STOPPED;
    }

    // Object Methods

    /**
     * Adds an object to this scene and initializes the object if the scene is
     * running. The object will not be added if it already has a parent scene.
     *
     * @param obj a {@link GameObject}
     */
    public final void addObject(@Nullable GameObject obj) {
        if (obj == null || obj.getScene() != null) return;
        if (isStopped()) { // Static add: when not loaded
            objects.addUnbuffered(obj);
            addObjectToScene(obj);
        } else { // Dynamic add: when loaded (running or paused)
            objects.addBuffered(obj, () -> this.addObjectToScene(obj));
        }
    }

    private void addObjectToScene(GameObject obj) {
        if (uniqueObjectNames) renameObjectUnique(obj);
        obj.setScene(this);
        if (!isStopped()) startObject(obj);
        Logger.trace("Added object \"%s\" to scene \"%s\"",
                obj, this.name);
    }

    private void renameObjectUnique(GameObject obj) {
        // Rename object to "Name (n)"
        // If sequence numbers are not contiguous, then choose the least free number
        // Make sure not to count the object being added
        var sameNames = objects.stream()
                .filter(o -> !o.equals(obj) && o.getName().startsWith(obj.getName()))
                .map(GameObject::getName)
                .toList();
        var newName = obj.getName();
        var count = 0;
        while (sameNames.contains(newName)) {
            count++;
            newName = "%s (%d)".formatted(obj.getName(), count);
        }
        obj.setName(newName);
    }

    private void startObject(GameObject obj) {
        obj.doStart(); // Add components first so renderer and physics can access it
        for (var comp : obj.getComponents()) {
            if (comp instanceof Renderable r) renderLayer.addRenderable(r);
            if (comp instanceof PhysicsBody b) physics.addPhysicsBody(b);
            if (comp instanceof CollisionBody b) physics.addCollisionBody(b);
        }
    }

    /**
     * Removes an object from this scene and destroys it.
     *
     * @param obj a {@link GameObject}
     */
    final void removeObject(@Nullable GameObject obj) {
        if (obj == null) return;
        objects.removeBuffered(obj, () -> this.removeObjectFromScene(obj));
    }

    private void removeObjectFromScene(GameObject obj) {
        for (var comp : obj.getComponents()) {
            if (comp instanceof Renderable r) renderLayer.removeRenderable(r);
            if (comp instanceof PhysicsBody b) physics.removePhysicsBody(b);
            if (comp instanceof CollisionBody b) physics.removeCollisionBody(b);
        }
        obj.onDestroy(); // TODO should move elsewhere
        Logger.trace("Removed object \"%s\" from scene \"%s\"",
                obj, this.name);
    }

    /**
     * Finds the first {@link GameObject} with the given name (case-sensitive), or null if none exists.
     *
     * @param name the object's name
     * @return the object
     */
    public @Nullable GameObject getObject(@Nullable String name) {
        if (name == null) return null;
        return objects.stream()
                .filter(obj -> obj.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a copy of the all objects in this scene.
     *
     * @return the list of objects
     */
    public List<GameObject> getObjects() {
        return List.copyOf(objects);
    }

    /**
     * Counts the number of objects in the scene.
     *
     * @return the amount of objects
     */
    public int numObjects() {
        return objects.size();
    }

    // Scene Layer Methods

    /**
     * Get the {@link mayonez.SceneLayer} by its numerical index.
     *
     * @param index the layer index
     * @return the layer, or null if the index is invalid
     */
    public @Nullable SceneLayer getLayer(int index) {
        if (index >= 0 && index < layers.length) return layers[index];
        else return null;
    }

    /**
     * Get the {@link mayonez.SceneLayer} by its name.
     *
     * @param name the layer name
     * @return the layer, or null if the name is invalid
     */
    public @Nullable SceneLayer getLayer(String name) {
        return Arrays.stream(layers)
                .filter(layer -> layer.getName().equals(name))
                .findFirst().orElse(null);
    }

    // Scene Properties

    /**
     * Returns the name of this scene, which is not null and need not be unique.
     *
     * @return the scene name
     */
    public String getName() {
        return name;
    }

    // Getters and Setters

    /**
     * Get the scene's {@link Camera} instance. The camera is initialized before
     * {@link GameObject#start()} is called for all other objects.
     *
     * @return the scene camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Get the scene's {@link  mayonez.graphics.debug.DebugDraw} instance.
     *
     * @return the scene debug draw
     */
    public final DebugDraw getDebugDraw() {
        return renderLayer.getDebugDraw();
    }

    public void setGravity(Vec2 gravity) {
        physics.setGravity(gravity);
    }

    boolean isRunning() {
        return state == SceneState.RUNNING;
    }

    boolean isPaused() {
        return state == SceneState.PAUSED;
    }

    boolean isDestroyed() {
        return state == SceneState.DESTROYED;
    }

    boolean isStopped() {
        return state == SceneState.STOPPED;
    }

    /**
     * Pauses the scene but does not destroy any game objects. While paused,
     * objects do not move or update but key inputs can still be polled through onUserUpdate().
     */
    final void pause() {
        state = SceneState.PAUSED;
    }

    /**
     * Resumes the scene but does not reinitialize any game objects.
     */
    final void resume() {
        state = SceneState.RUNNING;
    }

    // Object Overrides

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Scene s) && (s.sceneID == this.sceneID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sceneID, name);
    }

    @Override
    public String toString() {
        return String.format(
                "%s [%d] (%s)",
                name, sceneID,
                StringUtils.getObjectClassName(this)
        );
    }

}
