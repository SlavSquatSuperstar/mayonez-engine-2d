package mayonez.graphics.camera;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.math.FloatMath;
import mayonez.math.Vec2;

/**
 * The viewport into the scene. The camera may be adjusted through scripts
 * by accessing {@code Script.getScene().getCamera()}. This class should not be
 * directly instantiated. Instead, call {@link CameraFactory#createCamera}.
 * <p>
 * See {@link CameraFactory} for more information.
 *
 * @author SlavSquatSuperstar
 */
// TODO elastic/smooth movement
// TODO allow drag, but snap back to subject
public abstract class Camera extends Script {

    // Camera Fields
    final Vec2 screenSize;
    final float sceneScale;

    // Camera Movement
    private boolean followAngle;
    private Script keepInScene, dragAndDrop; // Reference to parent scripts
    private CameraMode mode;
    private GameObject subject;

    // Camera Effects
    private float zoom, rotation; // magnification of objects

    public Camera(Vec2 screenSize, float sceneScale) {
        this.screenSize = screenSize;
        this.sceneScale = sceneScale;
        mode = CameraMode.FIXED;
        subject = null;
        followAngle = false;
        zoom = 1f;
        rotation = 0f;
    }

    @Override
    public void start() {
        resetZoom();
        resetRotation();
    }

    @Override
    public void update(float dt) {
        if (getSubject() != null) {
            transform.setPosition(getSubject().transform.getPosition());
            if (followAngle) rotation = getSubject().transform.getRotation();
        }
    }

    // Factory Method

    // Camera Location

    /**
     * The position of the camera's center in the scene in world units.
     *
     * @return the position
     */
    public final Vec2 getPosition() {
        return new Vec2(transform.getPosition());
    }

    /**
     * Sets the position in world units of the camera's center in the scene.
     *
     * @param position the position
     */
    public final void setPosition(Vec2 position) {
        transform.setPosition(position);
    }

    /**
     * The position in screen units of the camera's bottom left corner (the canvas origin).
     *
     * @return the camera's offset
     */
    public final Vec2 getScreenOffset() {
        // (position * scene_scale) - (0.5 * screen_size / zoom)
        return getPosition().mul(sceneScale).sub(screenSize.mul(0.5f / getZoom()));
    }

    /**
     * Transform screen coordinates into world coordinates.
     *
     * @param screen a pixel on the screen
     * @return the world position of the pixel
     */
    public abstract Vec2 toWorld(Vec2 screen);

    // Camera Effects

    /**
     * Get the magnification of the camera, or how larger objects appear to be.
     *
     * @return the zoom
     */
    public float getZoom() {
        return zoom;
    }

    /**
     * Zoom the camera in or out by the given factor.
     *
     * @param zoom the magnification
     */
    public void zoom(float zoom) {
        if (FloatMath.equals(zoom, 0f)) return; // don't set zoom to zero
        this.zoom *= zoom;
        transform.scale(new Vec2(1f / zoom));
    }

    /**
     * Reset the camera's zoom back to 1x.
     */
    public void resetZoom() {
        zoom = 1f;
        transform.setScale(new Vec2(1f));
    }

    /**
     * Get the camera's rotation in degrees, or how tilted objects appear to be.
     *
     * @return the rotation
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Rotate the camera by a given angle.
     *
     * @param rotation the angle in degrees
     */
    public void rotate(float rotation) {
        this.rotation += rotation;
//        transform.rotate(rotation);
    }

    /**
     * Reset the camera's rotation back to 0 degrees.
     */
    public void resetRotation() {
        rotation = 0f;
    }

    // Camera Movement

    /**
     * Toggles whether the camera should rotate with the subject.
     *
     * @param enabled rotate the camera with the subject
     * @return the camera
     */
    public Camera setFollowAngle(boolean enabled) {
        followAngle = enabled;
        if (!followAngle) resetRotation();
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
     * Tell this camera to start following a subject, or disables subject following. If the camera mode
     * is changed, the camera will remember the last subject.
     *
     * @param subject a {@link mayonez.GameObject} in the scene
     * @return this object
     */
    public final Camera setSubject(GameObject subject) {
        this.subject = subject;
        resetRotation();
        setMode(CameraMode.FOLLOW);
        return this;
    }

    // Script Setters

    Script  setDragAndDropScript(Script dragAndDrop) {
        this.dragAndDrop = dragAndDrop;
        return dragAndDrop;
    }

    Script setKeepInSceneScript(Script keepInScene) {
        this.keepInScene = keepInScene;
        return keepInScene;
    }

}
