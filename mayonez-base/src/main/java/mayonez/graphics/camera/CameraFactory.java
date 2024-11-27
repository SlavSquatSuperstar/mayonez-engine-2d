package mayonez.graphics.camera;

import mayonez.*;
import mayonez.application.*;
import mayonez.scripts.camera.*;

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
    public static Camera createCamera(float sceneScale) {
        if (Mayonez.getUseGL()) {
            return new GLCamera(WindowProperties.getScreenSize(), sceneScale);
        } else {
            return new JCamera(WindowProperties.getScreenSize(), sceneScale);
        }
    }

    /**
     * Creates a container {@link mayonez.GameObject} to hold a scene's main camera.
     *
     * @param camera the camera instance
     * @return the camera object
     */
    public static GameObject createCameraObject(Camera camera) {
        var keepInScene = camera.setKeepInSceneScript(new CameraKeepInScene()
                .setEnabled(false));

        return new GameObject("Camera") {
            @Override
            protected void init() {
                addComponent(camera);
                addComponent(keepInScene);
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
