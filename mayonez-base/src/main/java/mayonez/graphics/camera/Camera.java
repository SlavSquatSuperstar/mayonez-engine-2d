package mayonez.graphics.camera;

import mayonez.*;
import mayonez.math.*;
import mayonez.renderer.*;
import mayonez.util.*;

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
    private float cameraScale, invCameraScale; // Pixels per unit

    // Camera Movement
    private GameObject subject;
    private final BufferedList<Component> cameraScripts;

    // Camera Effects
    private float zoom, rotation;

    protected Camera(Vec2 screenSize) {
        super(UpdateOrder.PRE_RENDER);
        this.screenSize = screenSize;

        cameraScale = 1f;
        invCameraScale = 1f;

        subject = null;
        cameraScripts = new BufferedList<>();

        zoom = 1f;
        rotation = 0f;
    }

    @Override
    protected void init() {
        cameraScripts.processBuffer();
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
        if (getSubject() != null) {
            transform.setPosition(getSubject().transform.getPosition());
        }
    }

    // Camera Position Methods

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
    public final Vec2 getScreenCenter() {
        return getPosition().mul(cameraScale);
    }

    /**
     * The position in screen units of the camera's bottom left corner (the canvas origin).
     *
     * @return the camera's offset
     */
    @Override
    public abstract Vec2 getScreenOffset(); // zoom offset

    // Camera Rotation Methods

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

    // Camera Zoom Methods

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

    // Camera Scale Methods

    @Override
    public float getCameraScale() {
        return cameraScale;
    }

    public void setCameraScale(float cameraScale) {
        this.cameraScale = cameraScale;
        invCameraScale = 1f / cameraScale;
    }

    protected float getInvCameraScale() {
        return invCameraScale;
    }

    /**
     * Get the size bounding box of the camera in the world.
     *
     * @return the camera size
     */
    public Vec2 getSize() {
        return screenSize.div(cameraScale * zoom);
    }

    // Camera Subject Methods

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
     */
    public final void setSubject(GameObject subject) {
        this.subject = subject;
    }

    // Camera Script Methods

    /**
     * Add a script to this camera's object that may be used to control camera movement.
     *
     * @param script the script
     */
    public final void addCameraScript(Script script) {
        cameraScripts.add(script, () -> getGameObject().addComponent(script));
    }

}
