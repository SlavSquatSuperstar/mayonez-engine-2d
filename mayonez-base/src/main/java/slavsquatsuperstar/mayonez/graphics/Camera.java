package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Script;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.DragAndDrop;
import slavsquatsuperstar.mayonez.scripts.KeepInScene;

/**
 * The viewport into the scene.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Camera extends Script {

    // Camera Fields
    final Vec2 screenSize;
    final float sceneScale;

    // Camera Movement
    private CameraMode mode = CameraMode.FIXED;
    private GameObject subject = null;
    Script keepInScene, dragAndDrop; // Reference to parent scripts

    public Camera(Vec2 screenSize, float sceneScale) {
        this.screenSize = screenSize;
        this.sceneScale = sceneScale;
    }

    // Factory Method

    /**
     * Creates a container {@link GameObject} to hold a scene's Camera object.
     *
     * @param camera the camera instance
     * @return the camera object
     */
    public static GameObject createCameraObject(Camera camera) {
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
                addComponent(new BoxCollider(camera.screenSize.div(camera.sceneScale)).setTrigger(true));
                addComponent(camera.keepInScene = new KeepInScene(KeepInScene.Mode.STOP).setEnabled(false));
            }

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

    // Camera Location

    /**
     * The position of the camera's center in the scene in world units.
     *
     * @return the position
     */
    public final Vec2 getPosition() {
        return new Vec2(transform.position);
    }

    /**
     * Sets the position in world units of the camera's center in the scene.
     *
     * @param position the position
     */
    public final void setPosition(Vec2 position) {
        transform.position = position;
    }

    /**
     * The read only position in screen units of the camera's bottom left corner.
     *
     * @return the camera's offset
     */
    public final Vec2 getOffset() {
        return getPosition().mul(sceneScale).sub(screenSize.mul(0.5f));
    }

    // Camera Movement

    /**
     * The movement mode of the camera, which is fixed in one place by default.
     *
     * @return the mode
     */
    public final CameraMode getMode() {
        return mode;
    }

    /**
     * Sets the movement mode of the camera.
     *
     * @param mode the mode
     * @return this camera
     */
    public final Camera setMode(CameraMode mode) {
        this.mode = mode;
        dragAndDrop.setEnabled(mode == CameraMode.FREE);
        return this;
    }

    /**
     * The object that the camera is following, or null if none is set.
     *
     * @return the subject
     */
    public final GameObject getSubject() {
        return subject;
    }

    /**
     * Sets a subject for this camera to follow and enables subject following. If the mode
     * is changed, the camera will remember the last subject.
     *
     * @param subject a {@link GameObject} in the scene
     * @return this object
     */
    public final Camera setSubject(GameObject subject) {
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
    public final Camera setKeepInScene(boolean enabled) {
        keepInScene.setEnabled(enabled);
        return this;
    }

}
