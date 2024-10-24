package mayonez.graphics.camera;

/**
 * What restrictions should be placed on the scene {@link Camera}'s movement.
 */
public enum CameraMode {

    /**
     * Limit the camera to one location or control its movement with a script.
     */
    FIXED,
    /**
     * Center the camera around a subject's position.
     */
    FOLLOW,
    /**
     * Allow the camera to move anywhere.
     */
    FREE

}
