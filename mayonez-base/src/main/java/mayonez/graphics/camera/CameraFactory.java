package mayonez.graphics.camera;

import mayonez.*;
import mayonez.math.*;

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
        var screenSize = new Vec2(Mayonez.getScreenWidth(), Mayonez.getScreenHeight());
        if (Mayonez.getUseGL()) {
            return new GLCamera(screenSize);
        } else {
            return new JCamera(screenSize);
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
            public void setDestroyed() {
                camera.setDestroyed();
            }
        };
    }

}
