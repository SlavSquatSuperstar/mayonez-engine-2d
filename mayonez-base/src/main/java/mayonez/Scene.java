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

/**
 * A collection of {@link GameObject}s representing an in-game world.
 *
 * @author SlavSquatSuperstar
 */
// TODO current cursor object
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
    private Camera camera;

    // Scene State
//    private final Consumer<GameObject> addObject, removeObject;
    private final Queue<SceneChange> changesToScene; // Use a separate list to avoid concurrent exceptions
    private SceneState state; // if paused or running

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

        // Scene state
        state = SceneState.STOPPED;

        // Initialize layers
        objects = new ArrayList<>();
        renderer = Mayonez.getUseGL() ? new GLDefaultRenderer() : new JDefaultRenderer();
        debugRenderer = (DebugRenderer) renderer;
        physics = new PhysicsWorld();

        // Scene changes
        changesToScene = new LinkedList<>();
    }

    // Initialization Methods

    /**
     * Initialize all objects and begin updating the scene.
     */
    final void start() {
        addCameraToScene();
        init();
        startSceneLayers();
        state = SceneState.RUNNING;
        addObjectsToLayers();
    }

    private void addCameraToScene() {
        camera = createCamera();
        addObject(Camera.createCameraObject(camera));
        Logger.log("Camera = %s", camera);
    }

    private Camera createCamera() {
        if (Mayonez.getUseGL()) return new GLCamera(Mayonez.getScreenSize(), scale);
        else return new JCamera(Mayonez.getScreenSize(), scale);
    }

    private void addObjectsToLayers() {
        renderer.setScene(this);
        physics.setScene(this);
    }

    private void startSceneLayers() {
        renderer.start();
        if (separateDebugRenderer()) debugRenderer.start();
        physics.start();
    }

    // Game Loop

    /**
     * Moves everything in the scene forward in time by a small increment, including physics, scripts, and UI.
     *
     * @param dt seconds since the last frame
     */
    public final void update(float dt) {
        if (isRunning()) updateSceneObjects(dt);
        onUserUpdate(dt);
        processSceneChanges();
    }

    private void updateSceneObjects(float dt) {
        objects.forEach(o -> {
            o.update(dt);
            if (o.isDestroyed()) removeObject(o);
        });
        physics.step(dt);
        camera.gameObject.update(dt); // Update camera last
    }

    private void processSceneChanges() {
        // Remove destroyed objects or add new
        while (!changesToScene.isEmpty()) changesToScene.poll().change();
    }

    /**
     * Redraws everything in the current scene, including backgrounds, sprites, and UI.
     *
     * @param g2 the window's graphics object
     */
    public final void render(Graphics2D g2) {
        renderer.render(g2);
        if (separateDebugRenderer()) debugRenderer.render(g2);
        onUserRender();
    }

    // Stop Methods

    /**
     * Destroy all updates and stop updating the scene.
     */
    final void stop() {
        destroySceneObjects();
        clearSceneLayers();
    }

    private void clearSceneLayers() {
        renderer.stop();
        if (separateDebugRenderer()) debugRenderer.stop();
        physics.stop();
        state = SceneState.STOPPED;
    }

    private void destroySceneObjects() {
        camera.setSubject(null);
        objects.forEach(GameObject::destroy);
        objects.clear();
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
     */
    protected void onUserRender() {
    }

    // Object Methods

    /**
     * Adds an object to this scene and initializes it. Also adds the object to the renderer and physics engine.
     *
     * @param obj a {@link GameObject}
     */
    public final void addObject(GameObject obj) {
        if (obj == null) return;
        if (isRunning()) {
            var addObjectLater = new SceneChange(obj, this::addObjectToRunningScene);
            changesToScene.offer(addObjectLater); // Dynamic add: add to scene and layers in next frame
        } else {
            addObjectToStoppedScene(obj);
        }
    }

    private void addObjectToStoppedScene(GameObject o) {
        objects.add(o.setScene(this));
        o.start(); // add components first so renderer and physics can access it
        Logger.debug("Added object \"%s\" to scene \"%s\"", o.getNameAndID(), this.name);
    }

    private void addObjectToRunningScene(GameObject o) {
        objects.add(o.setScene(this));
        o.start(); // add components first so renderer and physics can access it
        renderer.addObject(o);
        physics.addObject(o);
        Logger.debug("Added object \"%s\" to scene \"%s\"", o.getNameAndID(), this.name);
    }

    /**
     * Removes an object to this scene and destroys it. Also removes the object from the renderer and physics engine.
     *
     * @param obj a {@link GameObject}
     */
    public final void removeObject(GameObject obj) {
        if (obj == null) return;
        var removeObjectLater = new SceneChange(obj, this::removeObjectFromScene);
        changesToScene.offer(removeObjectLater);
    }

    private void removeObjectFromScene(GameObject o) {
        objects.remove(o);
        renderer.removeObject(o);
        physics.removeObject(o);
        o.destroy();
        Logger.debug("Removed object \"%s\" from scene \"%s\"", o.getNameAndID(), this.name);
    }

    /**
     * Finds the first {@link GameObject} with the given name, or null if none exists.
     *
     * @param name the object name
     * @return the object
     */
    public GameObject getObject(String name) {
        for (var obj : objects) {
            if (obj.getName().equals(name)) return obj;
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
        return Random.randomVector(getSize().mul(-0.5f), getSize().mul(0.5f));
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
     * @return the {@link Camera} instance
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

    SceneState getState() {
        return state;
    }

    public boolean isRunning() {
        return state == SceneState.RUNNING;
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

    // Helper Class

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
