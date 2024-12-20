package mayonez.graphics.camera;

import mayonez.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.renderer.*;

/**
 * The main viewport into the scene. The camera may be adjusted through scripts
 * by accessing {@code Script.getScene().getCamera()}. This class should not be
 * directly instantiated. Instead, call {@link CameraFactory#createCamera}.
 * <p>
 * See {@link CameraFactory} for more information.
 *
 * @author SlavSquatSuperstar
 */
public abstract class Camera extends Component implements Viewport {

    // Size Fields
    protected final Vec2 screenSize;
    protected final float sceneScale;

    // Camera Movement
    private CameraMode mode; // TODO remove
    private GameObject subject;
    private boolean followAngle;
    private Script keepInScene; // Reference to parent scripts

    // Camera Effects
    private float zoom, rotation;

    protected Camera(Vec2 screenSize, float sceneScale) {
        super(UpdateOrder.PHYSICS);
        this.screenSize = screenSize;
        this.sceneScale = sceneScale;

        mode = CameraMode.FIXED;
        subject = null;
        followAngle = false;

        zoom = 1f;
        rotation = 0f;
    }

    @Override
    protected void start() {
        resetZoom();
        resetRotation();
    }

    @Override
    protected void update(float dt) {
        // Follow subject
        // TODO smooth follow
        if (getSubject() != null && mode == CameraMode.FOLLOW) {
            transform.setPosition(getSubject().transform.getPosition());
            if (followAngle) rotation = getSubject().transform.getRotation();
        }
    }

    // Camera Location Methods

    /**
     * The position of the camera's center in the scene in world units.
     *
     * @return the position
     */
    @Override
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
     * The position in pixels of the camera's center position.
     *
     * @return the camera's screen position
     */
    @Override
    public final Vec2 getViewCenter() {
        return getPosition().mul(sceneScale);
    }

    /**
     * The position in screen units of the camera's bottom left corner (the canvas origin).
     *
     * @return the camera's offset
     */
    @Override
    public abstract Vec2 getScreenOffset(); // zoom offset

    // Camera Effects

    /**
     * Get the magnification of the camera, or how larger objects appear to be.
     *
     * @return the zoom
     */
    @Override
    public float getZoom() {
        return zoom;
    }

    /**
     * Zoom the camera in or out by the given factor.
     *
     * @param zoom the magnification
     */
    public void zoom(float zoom) {
        if (MathUtils.equals(zoom, 0f)) return; // don't set zoom to zero
        this.zoom *= zoom;
    }

    /**
     * Reset the camera's zoom back to 1x.
     */
    public void resetZoom() {
        zoom = 1f;
    }

    /**
     * Get the camera's rotation in degrees, or how tilted objects appear to be.
     *
     * @return the rotation
     */
    @Override
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
    }

    /**
     * Reset the camera's rotation back to 0 degrees.
     */
    public void resetRotation() {
        rotation = 0f;
    }

    // Camera Collision Methods

    /**
     * Gets the bounding box of the camera in the world.
     *
     * @return the camera bounds
     */
    public BoundingBox getBounds() {
        return new BoundingBox(getPosition(), screenSize.div(sceneScale * zoom));
    }

    // Camera Movement Methods

    /**
     * Sets whether the camera should rotate with the subject.
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
     * Sets whether the camera should stay within the scene bounds.
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

    Script setKeepInSceneScript(Script keepInScene) {
        this.keepInScene = keepInScene;
        return keepInScene;
    }

}
