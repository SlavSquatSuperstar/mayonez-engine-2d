package slavsquatsuperstar.mayonez.graphics;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;

/**
 * The viewport into the world.
 *
 * @author SlavSquatSuperstar
 */
public interface Camera {

    // Camera Location

    /**
     * The position of the camera's center in the scene.
     *
     * @return the position
     */
    Vec2 getPosition();

    /**
     * Sets the position of the camera's center in the scene.
     *
     * @param position the position
     */
    void setPosition(Vec2 position);

    /**
     * The read only position of the camera's bottom left corner used by the Renderer.
     *
     * @return the camera's offset
     */
    Vec2 getOffset();

    // Camera Movement

    /**
     * The movement mode of the camera, which is fixed in one place by default.
     *
     * @return the mode
     */
    CameraMode getMode();

    /**
     * Sets the movement mode of the camera.
     *
     * @param mode the mode
     * @return this camera
     */
    Camera setMode(CameraMode mode);

    /**
     * The object that the camera is following, or null if none is set.
     *
     * @return the subject
     */
    GameObject getSubject();

    /**
     * Sets a subject for this camera to follow.
     *
     * @param subject A {@link GameObject} in the scene.
     * @return this camera
     */
    Camera setSubject(GameObject subject);

}
