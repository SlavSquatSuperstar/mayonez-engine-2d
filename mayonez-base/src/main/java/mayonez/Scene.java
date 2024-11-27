package mayonez;

import mayonez.graphics.*;
import mayonez.graphics.camera.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.renderer.*;
import mayonez.util.*;

import java.awt.Graphics2D;
import java.util.*;

/**
 * An in-game world or level that holds multiple {@link mayonez.GameObject}s. Each scene
 * can  be given a background image or color.
 * <p>
 * Usage: Create a scene by instantiating a subclass or anonymous instance of
 * {@link mayonez.Scene}. Add objects to the scene by calling {@link #addObject}
 * inside the {@link #init()} method. Scenes may also define custom game logic
 * and graphics inside {@link #onUserUpdate} and {@link #onUserRender}.
 * <p>
 * See {@link mayonez.GameObject} and {@link mayonez.SceneManager} for more information.
 *
 * @author SlavSquatSuperstar
 */
// TODO current cursor object
public abstract class Scene {

    // Static Fields
    private static final boolean LOG_SCENE_CHANGES = false;
    private static int sceneCounter = 0; // total number of scenes created

    // Scene Information
    final int sceneID; // UUID for this scene
    private final String name;
    private final float scale; // scene scale, or how many pixels correspond to a world unit
    private final Vec2 size; // scene size, or zero if unbounded
    private SceneState state; // if paused or running

    // Scene Objects
    private final BufferedList<GameObject> objects;
    private final SceneLayer[] layers;

    // Renderers
    private Camera camera;
    protected final Sprite background;
    private final RenderLayer renderLayer;

    // Physics
    private final PhysicsWorld physics;

    /**
     * Creates an empty scene with size of (0, 0) and a scale of 1.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this(name, 0, 0, 1);
    }

    /**
     * Creates an empty scene with a size of (0, 0) sets the scale.
     *
     * @param name  the name of the scene
     * @param scale the scale of the scene
     */
    public Scene(String name, float scale) {
        this(name, 0, 0, scale);
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
        state = SceneState.STOPPED;
        background = Sprites.createSprite(Colors.WHITE);

        // Initialize layers
        objects = new BufferedList<>();
        layers = new SceneLayer[SceneLayer.NUM_LAYERS];
        renderLayer = RendererFactory.createRenderLayer(Mayonez.getUseGL(), background, size, scale);
        physics = new DefaultPhysicsWorld();
    }

    // Initialization Methods

    /**
     * Initialize all objects and begin updating the scene. Calls {@link GameObject#start()}
     * for all objects added on start.
     */
    final void start() {
        // Set up layers
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new SceneLayer(i);
        }

        // Add camera
        camera = CameraFactory.createCamera();
        camera.setCameraScale(scale);
        addObject(CameraFactory.createCameraObject(camera));

        // Add objects
        init();

        // Start objects
        state = SceneState.RUNNING;
        objects.forEach(this::startObject);
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
     * Moves everything in the scene forward in time by a small increment, including physics, scripts, and UI.
     *
     * @param dt seconds since the last frame
     */
    final void update(float dt) {
        onUserUpdate(dt);
        if (isRunning()) updateSceneObjects(dt);
        objects.processBuffer();
    }

    /**
     * Provide user-defined update behavior for this scene.
     *
     * @param dt seconds since the last frame
     */
    protected void onUserUpdate(float dt) {
    }

    private void updateSceneObjects(float dt) {
        objects.forEach(obj -> {
            obj.update(dt);
            if (obj.isDestroyed()) removeObject(obj);
        });
        physics.step(dt);
        camera.gameObject.update(dt); // Update camera last
    }

    // Render Methods

    /**
     * Redraws everything in the current scene, including backgrounds, sprites, and UI.
     *
     * @param g2 the window's graphics object
     */
    final void render(Graphics2D g2) {
        onUserRender();
        objects.forEach(GameObject::debugRender);
        renderLayer.render(g2);
    }

    /**
     * Provide user-defined draw behavior for this scene.
     */
    protected void onUserRender() {
    }

    // Stop Methods

    /**
     * Destroys all objects and stop updating the scene.
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
    public final void addObject(GameObject obj) {
        if (obj == null || obj.getScene() != null) return;
        if (isStopped()) { // Static add: when not loaded
            objects.addUnbuffered(obj);
            addObjectToScene(obj);
        } else { // Dynamic add: when loaded (running or paused)
            objects.add(obj, () -> this.addObjectToScene(obj));
        }
    }

    private void addObjectToScene(GameObject obj) {
        obj.setScene(this);
        if (!isStopped()) startObject(obj);
        if (LOG_SCENE_CHANGES) {
            Logger.debug("Added object \"%s\" to scene \"%s\"",
                    obj.getNameAndID(), this.name);
        }
    }

    private void startObject(GameObject obj) {
        obj.start(); // Add components first so renderer and physics can access it
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
    final void removeObject(GameObject obj) {
        if (obj == null) return;
        objects.remove(obj, () -> this.removeObjectFromScene(obj));
    }

    private void removeObjectFromScene(GameObject obj) {
        for (var comp : obj.getComponents()) {
            if (comp instanceof Renderable r) renderLayer.removeRenderable(r);
            if (comp instanceof PhysicsBody b) physics.removePhysicsBody(b);
            if (comp instanceof CollisionBody b) physics.removeCollisionBody(b);
        }
        obj.onDestroy();
        if (LOG_SCENE_CHANGES) {
            Logger.debug("Removed object \"%s\" from scene \"%s\"",
                    obj.getNameAndID(), this.name);
        }
    }

    /**
     * Finds the first {@link GameObject} with the given name, or null if none exists.
     *
     * @param name the object name
     * @return the object
     */
    public GameObject getObject(String name) {
        return objects.find(obj -> obj.getName().equals(name));
    }

    /**
     * Returns a copy of the all objects in this scene.
     *
     * @return the list of objects
     */
    public List<GameObject> getObjects() {
        return objects.copy();
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
    public SceneLayer getLayer(int index) {
        if (index >= 0 && index < layers.length) return layers[index];
        else return null;
    }

    /**
     * Get the {@link mayonez.SceneLayer} by its name.
     *
     * @param name the layer name
     * @return the layer, or null if the name is invalid
     */
    public SceneLayer getLayer(String name) {
        return Arrays.stream(layers)
                .filter(layer -> layer.getName().equals(name))
                .findFirst().orElse(null);
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

    /**
     * Get the size of the scene, useful for setting an arbitrary boundary.
     *
     * @return the scene size
     */
    public Vec2 getSize() {
        return size;
    }

    /**
     * Get the half size of the scene, useful for setting an arbitrary boundary.
     *
     * @return the scene half size
     */
    public Vec2 getHalfSize() {
        return size.mul(0.5f);
    }

    /**
     * Get the width of the scene, useful for setting an arbitrary boundary.
     *
     * @return the scene width.
     */
    public float getWidth() {
        return size.x;
    }

    /**
     * Get the height of the scene, useful for setting an arbitrary boundary.
     *
     * @return the scene height.
     */
    public float getHeight() {
        return size.y;
    }

    /**
     * Returns a random vector within the scene's bounds.
     *
     * @return a random position
     */
    public Vec2 getRandomPosition() {
        var halfSize = getHalfSize();
        return Random.randomVector(halfSize.mul(-1f), halfSize);
    }

    // Getters and Setters

    /**
     * Sets the background color of this scene, white by default.
     *
     * @param background a color
     */
    public void setBackground(Color background) {
        this.background.setColor(background);
        this.background.setTexture(null);
    }

    /**
     * Sets the background image of this scene, none by default.
     *
     * @param background a texture
     */
    public void setBackground(Texture background) {
        this.background.setColor(null);
        this.background.setTexture(background);
    }

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
        // Use Scene for class name if anonymous instance
        return String.format(
                "%s [ID = %d] (%s)",
                name, sceneID,
                StringUtils.getObjectClassName(this)
        );
    }

}
