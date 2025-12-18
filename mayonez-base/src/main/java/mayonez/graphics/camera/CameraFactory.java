package mayonez.graphics.camera;

import mayonez.*;
import mayonez.application.*;

/**
 * A factory class that constructs {@link mayonez.graphics.camera.Camera} objects
 * depending on the engine type.
 *
 * @author SlavSquatSuperstar
 */
public final class CameraFactory {

    private CameraFactory() {
    }

    /**
     * Creates a new {@link mayonez.graphics.camera.Camera} object.
     *
     * @return the game engine
     */
    public static Camera createCamera() {
        if (Mayonez.getUseGL()) {
            return new GLCamera(WindowProperties.getScreenSize());
        } else {
            return new JCamera(WindowProperties.getScreenSize());
        }
    }

    /**
     * Creates a container {@link mayonez.GameObject} to hold a scene's main camera.
     *
     * @param camera the camera instance
     * @return the camera object
     */
    public static GameObject createCameraObject(Camera camera) {
        return new GameObject("Camera") {
            @Override
            protected void init() {
                addComponent(camera);
            }

            // Don't want to get rid of the camera!
            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void destroy() {
            }
        };
    }

}
