package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;

/**
 * A scene camera for the Java engine.
 *
 * @author SlavSquatSuperstar
 */
// TODO elastic/smooth movement
// TODO allow drag, but snap back to subject
@UsesEngine(EngineType.AWT)
public final class JCamera extends Script implements Camera {

    private final Vec2 size;  // In world units

    // Camera Movement
    private CameraMode mode = CameraMode.FIXED;
    private GameObject subject = null;
    private Script keepInScene, dragAndDrop; // Reference to parent scripts

    public JCamera(Vec2 screenSize, float sceneScale) {
        size = screenSize.div(sceneScale);
    }

    // Static (Factory) Methods

    /**
     * Creates a container {@link GameObject} to hold a scene's Camera object.
     *
     * @param camera the camera instance
     * @param scene  the camera's scene
     * @return the camera object
     */
    public static GameObject createCameraObject(JCamera camera, Scene scene) {
        return new GameObject("Camera", new Vec2(0, 0)) {
            @Override
            protected void init() {
                addTag("Ignore Collisions");
                addComponent(camera);
                // Allow camera to be moved with mouse
                addComponent(camera.dragAndDrop = new DragAndDrop("right mouse", true, true) {
                    // Reset camera position with double click
                    @Override
                    public void onMouseDown() {
                        if (MouseInput.getClicks() == 2) camera.setPosition(new Vec2(0, 0));
                    }
                }.setEnabled(false));
                // Keep camera inside scene and add camera collider
                addComponent(new BoxCollider(camera.size).setTrigger(true));
                addComponent(camera.keepInScene = new KeepInScene(KeepInScene.Mode.STOP).setEnabled(false));
            }

//            @Override
//            protected void onUserUpdate(float dt) {
//                Logger.log("camera = %s", transform.position);
//            }

            // Don't want to get rid of the camera!
            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void setDestroyed() {
            }
        };
    }

    @Override
    public void update(float dt) {
        if (subject != null) transform.position.set(subject.transform.position);
    }

    // Camera Position Methods

    public Vec2 getPosition() {
        return transform.position;
    }

    public void setPosition(Vec2 position) {
        transform.position.set(position);
    }

    @Override
    public Vec2 getOffset() {
        return getPosition().sub(size.mul(0.5f));
    }

    // Camera Movement Methods

    @Override
    public CameraMode getMode() {
        return mode;
    }

    @Override
    public JCamera setMode(CameraMode mode) {
        this.mode = mode;
        dragAndDrop.setEnabled(mode == CameraMode.FREE);
        return this;
    }

    @Override
    public GameObject getSubject() {
        return subject;
    }

    /**
     * Sets a subject for this camera to follow and sets the camera mode to Follow. If the mode
     * is changed, the camera will remember the last subject.
     *
     * @param subject A {@link GameObject} in the scene.
     * @return this object
     */
    public JCamera setSubject(GameObject subject) {
        this.subject = subject;
        setMode(CameraMode.FOLLOW);
        return this;
    }

    /**
     * Toggles whether the camera should stay within the scene bounds.
     *
     * @param enabled keep the camera inside the scene
     * @return the camera
     */
    public JCamera setKeepInScene(boolean enabled) {
        keepInScene.setEnabled(enabled);
        return this;
    }

}
