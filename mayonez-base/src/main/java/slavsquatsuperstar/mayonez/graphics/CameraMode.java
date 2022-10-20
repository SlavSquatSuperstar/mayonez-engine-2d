package slavsquatsuperstar.mayonez.graphics;

/**
 * What restrictions should be placed on the scene {@link Camera}'s movement.
 */
public enum CameraMode {

    /**
     * Limit the camera to one location.
     */
    FIXED,
    /**
     * Center the camera around an object's position.
     */
    FOLLOW,
    /**
     * Allow the camera to move anywhere.
     */
    FREE,

}
